package com.el.uso.onethreethreeseven.leet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cash on 24/01/2018.
 *
 */

public class Solution {

    private static Solution sSolution;

    public class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int x) {
            val = x;
        }

        public String dumpNode() {
            StringBuilder sb = new StringBuilder("" + val);
            while (this.next != null) {
                sb.append(" -> " + this.next.val);
                this.next = this.next.next;
            }
            return sb.toString();
        }
    }

    private Solution() {

    }

    public static Solution getInstance() {
        if (sSolution == null) {
            sSolution = new Solution();
        }
        return sSolution;
    }

    public String printArray(int[] nums) {
        StringBuilder sb = new StringBuilder("[" + nums[0]);
        for (int i = 1; i < nums.length; i++) {
            sb.append(", " + i);
        }
        sb.append("]");
        return sb.toString();
    }

    public int[] getNRandomNumbers(int max, int n) {
        int[] numbers = new int[n];
        List<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            randomList.add(i);
        }
        Collections.shuffle(randomList);
        for (int i = 0; i < n; i++) {
            numbers[i] = randomList.get(i);
        }
        return numbers;
    }

    // nums = [2, 7, 11, 15], target = 9
    // return [0, 1]
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> twoSum = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            twoSum.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int sub = target - nums[i];
            if (twoSum.containsValue(sub) && twoSum.get(sub) != i) {
                return new int[] {i, twoSum.get(sub)};
            }
        }
        throw new IllegalArgumentException("No solution");
    }

    public String printNode(int num) {
        ListNode node = generateNode(num);
        StringBuilder sb = new StringBuilder("" + node.val);
        while (node.next != null) {
            sb.append(" -> " + node.next.val);
            node.next = node.next.next;
        }
        return sb.toString();
    }

    public ListNode generateNode(int i) {
        ListNode node = new ListNode(i % 10);
        ListNode tmp = node;
        while ((i / 10) != 0) {
            i = i / 10;
            tmp.next = new ListNode(i % 10);
            tmp = tmp.next;
        }
        return node;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode sum = new ListNode(0);
        ListNode tmp = sum;
        do {
            int lOneValue = (l1 == null) ? 0 : l1.val;
            int lTwoValue = (l2 == null) ? 0 : l2.val;
            tmp.val += lOneValue + lTwoValue;
            l1 = (l1 != null) ? l1.next : null;
            l2 = (l2 != null) ? l2.next : null;
            if (l1 != null || l2 != null || tmp.val >= 10) {
                tmp.next = new ListNode(tmp.val / 10);
            }
            tmp.val = tmp.val % 10;
            tmp = tmp.next;
        } while (l1 != null || l2 != null);
        return sum;
    }

    public int findMaxForm(String[] strs, int m, int n) {
        int[][] counts = getCounts(strs);
        Arrays.sort(counts, getGreedyComparator(m, n));   //greedy order
        int max = 0;
        for (int i = 0; i < counts.length; i++) {   //greedy
            if (counts.length - i < max) return max;
            int zero = m;
            int one = n;
            int j = i;
            while (j < counts.length) {
                if (counts[j][0] <= zero && counts[j][1] <= one) {
                    zero -= counts[j][0];
                    one -= counts[j][1];
                } else {
                    break;
                }
                j++;
            }
            max = Math.max(j-i, max);
        }
        return max;
    }

    public int[][] getCounts(String[] input) {
        int[][] counts = new int[input.length][2];
        for (int i = 0; i < input.length; i++) {
            for (int j= 0; j < input[i].length(); j++) {
                if (input[i].charAt(j) == '0') counts[i][0]++;
                if (input[i].charAt(j) == '1') counts[i][1]++;
            }
        }
        return counts;
    }

    public Comparator<int[]> getGreedyComparator(final int zeroes, final int ones) {
        return new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (zeroes == ones) {
                    if (o1[0] + o1[1] - o2[0] - o2[1] == 0) {
                        return Math.min(o1[0], o1[1]) - Math.min(o2[0], o2[1]);
                    }
                    return o1[0] + o1[1] - o2[0] - o2[1];
                } else if (zeroes < ones) {
                    if (o1[0] == o2[0]) {
                        return o1[1] - o2[1];
                    }
                    return o1[0] - o2[0];
                } else {
                    if (o1[1] == o2[1]) {
                        return o1[0] - o2[0];
                    }
                    return o1[1] - o2[1];
                }
            }
        };
    }

}
