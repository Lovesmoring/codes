/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if(lists.length == 0)
            return null;
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(new Comparator<ListNode>(){
            @Override
            public int compare(ListNode n1, ListNode n2) {
                return n1.val - n2.val;
            }   
        });
        for(ListNode node : lists) {
            if(node != null) {
                minHeap.offer(node);
            }
        }
        ListNode head = minHeap.poll();
        ListNode prev = head;
        ListNode cur = null;
        while(!minHeap.isEmpty()) {
            if(prev.next != null)
                minHeap.offer(prev.next);
            cur = minHeap.poll();
            prev.next = cur;
            prev =cur;
        }
        return head;
    }

}