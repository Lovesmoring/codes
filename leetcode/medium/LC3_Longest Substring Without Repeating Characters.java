class Solution {
    public int lengthOfLongestSubstring(String s) {
        if(s == null || s.length() == 0) return 0;
        int max = 0, begin = 0;
        Map<Character, Integer> map = new HashMap<>();
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(map.containsKey(c)){
                int j = map.get(c);
                for(int k = begin; k <= j; k++)
                    map.remove(s.charAt(k));
                begin = j+1;
            }
            map.put(c, i);
            max = Math.max(max, i - begin + 1);
            
        }
        return max;
    }
}