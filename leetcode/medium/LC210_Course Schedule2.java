class Solution {
    public int[] findOrder(int n, int[][] pres) {
        if(n == 0){
            return new int[0];
        }
        List<List<Integer>> adjs = new ArrayList<>();
        int[] degree = new int[n];
        for(int i = 0; i < n; i++){
            adjs.add(new ArrayList<>());
        }
        //indegree
        for(int[] pre : pres){
            degree[pre[0]]++;
            adjs.get(pre[1]).add(pre[0]);
        }
        return solveByBFS(adjs, degree);
    }
    private int[] solveByBFS(List<List<Integer>> adjs, int[] degree){
        int n = degree.length;
        int[] res = new int[n];
        int index = 0;
        Queue<Integer> q = new LinkedList<>();
        for(int i = 0; i < n; i++){
            if(degree[i] == 0){
                q.offer(i);
            }
        }
        while(!q.isEmpty()){
            int pre = q.poll();
            res[index++] = pre;
            for(int  course : adjs.get(pre)){
                degree[course]--;
                if(degree[course] == 0)
                    q.offer(course);
            }
        }
        if(index != n) return new int[0];
        return res;
    }
}
