package com.el.uso.onethreethreeseven.web;

import com.el.uso.onethreethreeseven.web.model.ErrorData;

public class Result<T> {
    private ErrorData mError;
    private T mResult;

    public ErrorData error() {
        return mError;
    }

    public void setError(ErrorData err) {
        mError = err;
    }

    public T result() {
        return mResult;
    }

    public void setResult(T result) {
        mResult = result;
    }

    public Result() {
        mError = new ErrorData();
    }

    public void setCode(int code) {
        mError.setCode(code);
    }

    public void setMsg(String msg) {
        mError.setMessage(msg);
    }
}
