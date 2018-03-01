class Solution {
    public int[] searchRange(int[] nums, int target) {
        double leftTarget = target - 0.5, rightTarget = target + 0.5;
        if(nums.length == 0) return new int[]{-1, -1};
        int lo = 0, hi = nums.length-1;
        while(lo < hi){
            int mid = (lo + hi) / 2;
            if(nums[mid] < leftTarget){
                lo = mid + 1;
            }  else{
                hi = mid - 1;
            }
        }
        int left = nums[lo] == target ? lo : lo+1;
        lo = 0;
        hi = nums.length-1;
        while(lo < hi){
            int mid = (lo + hi) / 2;
            if(nums[mid] < rightTarget){
                lo = mid + 1;
            }  else{
                hi = mid - 1;
            }
        }
        int right = nums[lo] == target ? lo : lo-1;
        if(left > right){
            return new int[]{-1, -1};
        }
        return new int[]{left, right};
        
    }
}