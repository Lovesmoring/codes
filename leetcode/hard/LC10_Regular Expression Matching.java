class Solution {
    //s 中没有* 和.
    public boolean isMatch(String s, String p) {
        if(s == null || p == null) return false;
        int m = s.length(), n = p.length();
        boolean[][] mem = new boolean[m+1][n+1];
        mem[0][0] = true;
        for(int i = 0; i < n; i++){
            if(p.charAt(i) == '*' && mem[0][i-1])
                mem[0][i+1] = true;
        }
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(p.charAt(j) == s.charAt(i)){
                    mem[i+1][j+1] = mem[i][j];
                } else{
                    if(p.charAt(j) == '.'){
                        mem[i+1][j+1] = mem[i][j];
                    } else if(p.charAt(j) == '*'){
                        if(p.charAt(j-1) == s.charAt(i) || p.charAt(j-1) == '.'){
                            mem[i+1][j+1] = mem[i+1][j] || mem[i][j+1] || mem[i+1][j-1]; 
                        } else{
                            mem[i+1][j+1] = mem[i+1][j-1];
                        }
                    }
                }
            }
        }
        return mem[m][n];
    }
}