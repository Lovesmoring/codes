class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> hmap = new HashMap<>();
        for(int j = 0; j < nums.length; j++) {
            int i = hmap.getOrDefault(target - nums[j], -1);
            if(i != -1) return new int[]{i, j};
            hmap.put(nums[j], j);
        }
        return new int[]{-1, -1};
    }
}