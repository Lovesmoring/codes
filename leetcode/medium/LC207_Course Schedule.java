class Solution {
    // public boolean canFinish(int numCourses, int[][] prerequisites) {
    //     Map<Integer, Set<Integer>> map = new HashMap<>();
    //     for(int[] edge : prerequisites){
    //         if(!map.containsKey(edge[0])){
    //             Set<Integer> set = new HashSet<>();
    //             map.put(edge[0], set);
    //         }
    //         map.get(edge[0]).add(edge[1]);
    //     }
    //     Set<Integer> noLoops = new HashSet<>();
    //     for(int key : map.keySet()){
    //         if(findLoop(map, key, noLoops, new HashSet<>()))
    //             return false;
    //         noLoops.add(key);
    //     }
    //     return true;
    // }
    // private boolean findLoop(Map<Integer, Set<Integer>> map, int course, Set<Integer> noLoops, Set<Integer> visited){
    //     if(noLoops.contains(course) || !map.containsKey(course)) return false;
    //     if(visited.contains(course)) return true;
    //     visited.add(course);
    //     Set<Integer> set = map.get(course);
    //     for(int next : set){
    //         if(findLoop(map, next, noLoops, visited))
    //             return true;
    //     }
    //     return false;
    // }
    public boolean canFinish(int numCourses, int[][] pre){
        int n = numCourses;
        int[][] mat = new int[n][n];
        int[] indegree = new int[n];
        for(int[] edge : pre){
            int c = edge[0];
            int pr = edge[1];
            if(mat[pr][c] == 0){
                mat[pr][c] = 1;
                indegree[c]++;
            }   
        }
        Queue<Integer> q = new LinkedList<>();
        for(int i = 0; i < n; i++){
            if(indegree[i] == 0){
                q.offer(i);
            }
        }
        int cnt = 0;
        while(!q.isEmpty()){
            int c = q.poll();
            cnt++;
            for(int i = 0; i < n; i++){
                if(mat[c][i] > 0){
                    if(--indegree[i] == 0){
                        q.offer(i);
                    }
                }
            }
        }
        return cnt == n;
    }
}