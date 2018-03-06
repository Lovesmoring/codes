public class Solution {
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int res = 0;
        int maxLeft = 0, maxRight = 0;
        while(left <= right){
            maxLeft = Math.max(maxLeft, height[left]);
            maxRight = Math.max(maxRight, height[right]);
            if(maxLeft < maxRight){
                if(height[left] < maxLeft) res += maxLeft - height[left];
                left++;
            } else{
                if(height[right] < maxRight) res += maxRight - height[right];
                right--;
            }
        }
        return res; 
    }
}
