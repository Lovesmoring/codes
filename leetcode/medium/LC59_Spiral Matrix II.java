class Solution {
    public int[][] generateMatrix(int n) {
        if(n == 0) return new int[0][0];
        int[][] res = new int[n][n];
        int i = 0, j = 0;
        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int count = 1;
        int dirCnt = 0;
        int[] dir = dirs[0];
        while(count <= n * n){
            if(i < 0 || i >= n || j < 0 || j >= n || res[i][j] > 0){
                i -= dir[0];
                j -= dir[1]; 
                dir = dirs[++dirCnt % 4];
                i += dir[0];
                j += dir[1];
            }
            res[i][j] = count++;
            i += dir[0];
            j += dir[1];
        }
        return res;
    }
}