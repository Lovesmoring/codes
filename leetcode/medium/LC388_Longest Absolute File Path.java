class Solution {
    public int lengthLongestPath(String input) {
        String[] dirs = input.split("\n");
        int[] stack = new int[dirs.length+1];
        int maxLen = 0;
        for(String dir : dirs){
            int lev = dir.lastIndexOf("\t") + 1;
            int curLen = stack[lev + 1] = stack[lev] + dir.length() - lev + 1;
            if(dir.contains(".")) maxLen = Math.max(maxLen, curLen - 1);
        }
        return maxLen;
    }
}