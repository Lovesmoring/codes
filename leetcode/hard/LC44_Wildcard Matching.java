class Solution {
    public boolean isMatch(String str, String pattern) {
         int m = str.length(), n = pattern.length();
        // boolean[][] mem = new boolean[m+1][n+1];
        // mem[0][0] = true;
        // for(int i = 0; i < n; i++){
        //     if(p.charAt(i) == '*') mem[0][i+1] = mem[0][i]; 
        // }
        // for(int i = 0; i < m; i++){
        //     for(int j = 0; j < n; j++){
        //         if(p.charAt(j) == '?' || p.charAt(j) == s.charAt(i)){
        //             mem[i+1][j+1] = mem[i][j];
        //         }
        //         if(p.charAt(j) == '*'){
        //             mem[i+1][j+1] = mem[i][j] || mem[i+1][j] || mem[i][j+1];
        //         }
        //     }
        // }
        // return mem[m][n];
        int s = 0, p = 0, match = 0, starIdx = -1;
        while(s < m){
            if(p < n && (pattern.charAt(p) == '?' || str.charAt(s) == pattern.charAt(p))){
                s++;
                p++;
            } else if(p<n && pattern.charAt(p) == '*'){
                starIdx = p;
                match = s;
                p++;
            }else if(starIdx >= 0){
                p = starIdx +1;
                match++;
                s = match;
            }
            else return false;
        }
        while(p < n && pattern.charAt(p) == '*'){
            p++;
        }
        return p == n;
    }
}