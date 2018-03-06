/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return add2(l1, l2, 0);
        
    }
    private ListNode add2(ListNode l1, ListNode l2, int c){
        if(l1 == null && l2 == null){
            if(c > 0){
                return new ListNode(c);
            }
            return null;
        }
        if(l1 == null){
            int res = l2.val + c;
            ListNode node = new ListNode(res % 10);
            node.next = add2(null, l2.next, res / 10);
            return node;
        }
        if(l2 == null){
            int res = l1.val + c;
            ListNode node = new ListNode(res % 10);
            node.next = add2(l1.next, null, res / 10);
            return node;
        }
        int res = l1.val + l2.val + c;
        ListNode node = new ListNode(res % 10);
        node.next = add2(l1.next, l2.next, res / 10);
        return node;
    }
}