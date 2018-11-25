package com.el.uso.onethreethreeseven.helper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.el.uso.onethreethreeseven.Constants;
import com.el.uso.onethreethreeseven.log.L;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskRunningManager {
    private static final int RUNNING_THREADS_COUNT = 8;
    private static TaskRunningManager mInstance;

    public interface OnTaskListener {
        void onTaskResult(int key, Object result);
    }

    private ArrayList<TaskListenerItem> mListenerList = new ArrayList<>();
    private final ArrayList<Item> mQueueTasks = new ArrayList<>();
    private InnerThread mThread = new InnerThread();
    private boolean mStopSign;
    private final Object mWaitLock = new Object();
    private String mName;
    private TaskHandler mHandler = new TaskHandler();

    public static TaskRunningManager inst() {
        if (mInstance == null)
            mInstance = new TaskRunningManager("[MAIN]");

        return mInstance;
    }

    private TaskRunningManager(String name) {
        mName = name;
        mThread.start();
        mHandler.setListener(mListenerList);
    }

    @SuppressWarnings("unused")
    public void registerListener(OnTaskListener listener) {
        TaskListenerItem listenerItem = null;
        for (TaskListenerItem item : mListenerList) {
            if (item.listener == listener) {
                listenerItem = item;
                break;
            }
        }

        if (listenerItem != null) {
            ++listenerItem.refCount;
        } else {
            mListenerList.add(TaskListenerItem.make(listener));
        }
    }

    @SuppressWarnings("unused")
    public void unregisterListener(OnTaskListener listener) {
        for (TaskListenerItem item : mListenerList) {
            if (item.listener == listener) {
                --item.refCount;
                if (item.refCount <= 0) {
                    mListenerList.remove(item);
                    break;
                }
            }
        }
    }

    public void resetAllTasks() {
        L.w("[TaskRunningMgr] resetAllTasks");
        mListenerList.clear();
        mListenerList = new ArrayList<>();
        mHandler.setListener(mListenerList);
        mQueueTasks.clear();
        mThread.clearTask();
    }

    public boolean pushTask(int key, Callable<Object> callable, boolean runInSingleThread) {
        return pushTask(key, callable, runInSingleThread, null);
    }

    public boolean pushTask(int key, Callable<Object> callable, boolean runInSingleThread, OnTaskListener inThreadListener) {
        if (!mThread.isAlive()) return false;

        L.w("[TaskRunningMgr] pushTask " + key);

        Item item = Item.make(key, callable, runInSingleThread, inThreadListener);
        synchronized (mQueueTasks) {
            mQueueTasks.add(item);
        }

        synchronized (mWaitLock) {
            mWaitLock.notifyAll();
        }

        return true;
    }

    @SuppressWarnings("unused")
    public boolean isRunning(int key) {
        return isInQueue(key) || mThread.isRunning(key);
    }

    @SuppressWarnings("unused")
    public boolean isInQueue(int key) {
        synchronized (mQueueTasks) {
            boolean found = false;
            for (Item item : mQueueTasks) {
                if (item.key == key) {
                    found = true;
                    break;
                }
            }
            return found;
        }
    }

    @SuppressWarnings("unused")
    public void removeFromQueue(int key) {
        synchronized (mQueueTasks) {
            final int size = mQueueTasks.size();
            for (int i = size - 1; i >= 0; --i) {
                Item item = mQueueTasks.get(i);
                if (item.key == key) {
                    mQueueTasks.remove(i);
                }
            }
        }
    }

    public void release() {
        mStopSign = true;
        synchronized (mWaitLock) {
            mWaitLock.notifyAll();
        }
        mInstance = null;
    }


    private class InnerThread extends Thread {

        private RunningThread[] mRunningThreads;

        @Override
        public void run() {
            L.w("[TaskRunningMgr] thread start " + mName);
            try {
                runWithTry();
            } catch (Exception e) {
                L.w("[TaskRunningMgr] exception in inner thread. " + e);
            }
            L.w("[TaskRunningMgr] thread end " + mName);
        }

        private void runWithTry() throws Exception {
            initThreads();

            while (!mStopSign) {
                Item item = null;
                synchronized (mQueueTasks) {
                    if (mQueueTasks.size() > 0) {
                        item = mQueueTasks.get(0);
                        mQueueTasks.remove(0);
                    }
                }

                if (item == null) {
                    synchronized (mWaitLock) {
                        mWaitLock.wait(60000);
                    }
                    continue;
                }

                RunningThread thread = findThread(item);
                thread.push(item);
            }

            releaseThreads();
        }

        public boolean isRunning(int key) {
            if (mRunningThreads == null)
                return false;

            boolean isRunning = false;

            for (RunningThread thread : mRunningThreads) {
                if (thread != null && thread.isRunningOrInQueue(key)) {
                    isRunning = true;
                    break;
                }
            }

            return isRunning;
        }

        public void clearTask() {

            try {
                for (RunningThread rt : mRunningThreads)
                    if (rt != null)
                        rt.clearTask();
            } catch (Exception e) {
                L.e("TaskRunningManager:" + e);
                L.d("TaskRunningManager:" + Utils.getStackTrace(e));
            }

        }

        private void initThreads() {
            mRunningThreads = new RunningThread[RUNNING_THREADS_COUNT];
            for (int i = 0; i < RUNNING_THREADS_COUNT; ++i) {
                mRunningThreads[i] = new RunningThread("TaskThread " + i, mHandler);
                mRunningThreads[i].start();
            }
        }

        private void releaseThreads() {
            for (int i = 0; i < RUNNING_THREADS_COUNT; ++i) {
                RunningThread rt = mRunningThreads[i];
                if (rt != null)
                    rt.release();
            }
        }

        private RunningThread findThread(Item item) {
            RunningThread thread = null;
            if (item.isRunOnSingleThread) {
                for (RunningThread rt : mRunningThreads) {
                    if (rt != null && (rt.findTask(item.key) || rt.mActivatedId == item.key)) {
                        thread = rt;
                        break;
                    }
                }
            }

            if (thread == null) {
                int minTaskCount = 999999;
                for (RunningThread rt : mRunningThreads) {
                    if (rt == null) continue;

                    int size = rt.mQueue.size();
                    if (rt.mActivatedId != 0) ++size;

                    if (size < minTaskCount) {
                        minTaskCount = size;
                        thread = rt;
                    }
                }
            }

            return thread;
        }
    }

    private static class Item {
        public Callable<Object> callable;
        public int key;
        public boolean isRunOnSingleThread;
        public OnTaskListener inThreadListener;

        public static Item make(int key, Callable<Object> callable, boolean isRunOnSingleThread, OnTaskListener inThreadListener) {
            Item item = new Item();
            item.key = key;
            item.callable = callable;
            item.isRunOnSingleThread = isRunOnSingleThread;
            item.inThreadListener = inThreadListener;
            return item;
        }
    }

    private static class TaskHandler extends Handler {
        private ArrayList<TaskListenerItem> mListener;

        public TaskHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {

            try {

                switch (msg.what) {

                    case Constants.HANDLER_ID_TASK_RESULT_CALLBACK:

                        if (mListener != null) {
                            int  n = mListener.size();
                            L.w("[TaskRunningMgr] on task result " + msg.arg1 + "(Listener size is " + n);
                        }
                        else {
                            L.w("[TaskRunningMgr] on task result " + msg.arg1 + "(Listener is null)");
                        }

                        if (mListener != null) {
                            ArrayList<TaskListenerItem> taskListener = mListener;
                            @SuppressWarnings("unchecked")
                            ArrayList<TaskListenerItem> listeners = (ArrayList<TaskListenerItem>) taskListener.clone();
                            for (TaskListenerItem listener : listeners) {
                                listener.listener.onTaskResult(msg.arg1, msg.obj);
                            }
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                }

            }catch (Exception e) {
                L.e("[TaskHandler] " + e + Utils.getStackTrace(e));
            }
        }

        public void setListener(ArrayList<TaskListenerItem> listener) {
            mListener = listener;
        }
    }

    private static class RunningThread extends Thread {
        private Handler mHandler;
        private boolean mFinish;
        private int mActivatedId;
        private final Object mLocker = new Object();
        private final ConcurrentLinkedQueue<Item> mQueue = new ConcurrentLinkedQueue<>();

        public RunningThread(String name, Handler handler) {
            setName(name);
            mHandler = handler;
        }

        public void release() {
            mFinish = true;
            synchronized (mLocker) {
                mLocker.notify();
            }
        }

        public void push(Item item) {
            mQueue.add(item);
            L.w("[TaskRunningMgr] push " + item.key + " in " + getName() + ", size: " + mQueue.size());
            synchronized (mLocker) {
                mLocker.notify();
            }
        }

        public void clearTask() {
            mQueue.clear();
        }

        public boolean isRunningOrInQueue(int id) {
            boolean inRunning = mActivatedId == id;

            if (!inRunning) {
                synchronized (mQueue) {
                    for (Item item : mQueue) {
                        if (item.key == id) {
                            inRunning = true;
                            break;
                        }
                    }
                }
            }

            return inRunning;
        }

        @Override
        public void run() {
            L.w("[TaskRunningMgr] thread " + getName() + " start");
            try {
                runWithTryCatch();
            } catch (Exception e) {
                L.w("[TaskRunningMgr] RunningThread " + getName() + " crash. " + e);
                L.e("[TaskRunningMgr] RunningThread Exception:" + Utils.getStackTrace(e));
            } catch (Error er) {
                L.e("[TaskRunningMgr] RunningThread er:" + er + "," + getName());
                L.e("[TaskRunningMgr] RunningThread er:" + Utils.getStackTrace(er));
            }

            L.w("[TaskRunningMgr] thread " + getName() + " end");
        }

        private void runWithTryCatch() throws Exception {
            while (!mFinish) {
                if (mQueue.size() == 0) {
                    synchronized (mLocker) {
                        mLocker.wait(60000);
                    }
                    continue;
                }

                Item item;
                synchronized (mQueue) {
                    item = mQueue.poll();
                }

                L.w("[TaskRunningMgr] run " + item.key + " in " + getName());
                long t1 = SystemClock.elapsedRealtime();
                mActivatedId = item.key;
                Object result = item.callable.call();
                mActivatedId = Constants.TASK_RUN_NO_RUNNING;
                long t2 = SystemClock.elapsedRealtime();
                L.w("[TaskRunningMgr] task " + item.key + " exe time: " + (t2 - t1));

                if (item.inThreadListener != null) {
                    item.inThreadListener.onTaskResult(item.key, result);
                } else {
                    Message msg = new Message();
                    msg.what = Constants.HANDLER_ID_TASK_RESULT_CALLBACK;
                    msg.arg1 = item.key;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        }

        private boolean findTask(int id) {
            if (mActivatedId == id)
                return true;

            synchronized (mQueue) {
                for (Item item : mQueue) {
                    if (item.key == id)
                        return true;
                }
            }

            return false;
        }
    }

    private static class TaskListenerItem {
        public int refCount;
        public OnTaskListener listener;

        public static TaskListenerItem make(OnTaskListener listener) {
            TaskListenerItem item = new TaskListenerItem();
            item.refCount = 0;
            item.listener = listener;
            return item;
        }
    }
}
