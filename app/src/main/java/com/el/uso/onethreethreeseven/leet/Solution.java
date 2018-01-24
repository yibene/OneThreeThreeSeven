package com.el.uso.onethreethreeseven.leet;

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

        public String printNode() {
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

    public int[] twoSum(int[] nums, int target) {
        int i = 0;
        int j = 0;
        return new int[] {i, j};
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
