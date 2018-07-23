package com.el.uso.onethreethreeseven.leet;

import java.util.ArrayList;
import java.util.Collections;
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



}
