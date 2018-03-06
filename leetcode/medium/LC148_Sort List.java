/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode sortList(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode fast = head.next.next;
        ListNode slow = head.next;
        ListNode prev = head;
        while(fast != null && fast.next != null){
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prev.next = null;
        ListNode list1head = sortList(head);
        ListNode list2head = sortList(slow);
        ListNode dumb = new ListNode(0);
        ListNode begin = dumb;
        while(list1head != null && list2head != null){
            if(list1head.val > list2head.val){
                begin.next = list2head;
                begin = begin.next;
                list2head = list2head.next;
            } else{
                begin.next = list1head;
                begin = begin.next;
                list1head = list1head.next;
            }
        }
        if(list1head == null){
            begin.next = list2head;
        } else{
            begin.next = list1head;
        }
        return dumb.next;
    }
}