//Using this file because it is one of my recent homework, in this file
//I created a gameplaying AI to complete a fruit canceling game with each other.
//It is using greedy/or MiniMax pruning to play the game, and win rate is remarkable.

package assignment1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class homework {
	static char[][] map;
	static int p;
	static int alpha;
	static int beta;
	static double totalTime;
	static int width;
	static int score;
	static int totalNumLeft;
	static int[] fixedDepth = new int[]{1, 1, 4, 6, 6, 6, 5, 5, 5, 4, 4,
    		4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 
    		3, 3, 3, 3, 3, 3};
	public static void main(String[] args) {
		String fileName = "input.txt";
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			processFile(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ExecutorService service = Executors.newSingleThreadExecutor();
		try {
		    Runnable r = new Runnable() {
		        @Override
		        public void run() {
		        	long starttime = System.currentTimeMillis();
		        	//processMiniMax(map, fixedDepth[width]);
		        	//playGameTime();
		        	playGameTime();
		        	long endtime = System.currentTimeMillis();
		        	//System.out.println(totalTime);
		        	System.out.println("Total running time is " + (endtime - starttime) / 1000.0 + "s"); 
		        }
		    };
		    Future<?> f = service.submit(r);
		    f.get((int)(totalTime * 1000000), TimeUnit.MICROSECONDS); 
		}
		catch (final TimeoutException e) {
			System.out.println("xxxx");
		    processGreedy(map);
		}
		catch (final ExecutionException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
		    service.shutdown();
		}
	}

	private static void processFile(Stream<String> stream) {
		List<String> list = stream.collect(Collectors.toList());
		if(list.size() <  4)  {
			printError("input file lines less than 4");
			return;
		}
		width = Integer.parseInt(list.get(0));
		p = Integer.parseInt(list.get(1));
		totalTime = Double.parseDouble(list.get(2));
		if(!verifyParas(list)) {
			printError("Wrong parameters");
			return;
		}
		map = new char[width][width];
		totalNumLeft = fillMap(list);
	}
	private static void playGameTime() {
		for(int i = 1; i < 27; i++){
			char[][] cmap = new char[i][i];
			for(int j = 1; j < Math.min(10, i * i); j++) {
				getRandomMap(cmap, j);
				System.out.print("width = " + i + ", " + "fruit types = " + j + ", " + "depth = " + fixedDepth[i] + "; ");
				//System.out.print("width = " + i + ", " + "fruit types = " + j + "; ");
					processMiniMax1(cmap, fixedDepth[i]);
				System.out.println();
			}
			System.out.println();
		}
	}
	private static void playGame() {
//		char[][] cmap = map;
		int width = 10;
		
		int cnt = 0;
//		for(int i = 18; i < 27; i++){
//			for(int j = 1; j < 10; j++) {
//				for(int k = 1; k < 3; k++) {
					char[][] cmap = new char[width][width];
					getRandomMap(cmap, p);
					//System.out.print("width = " + i + ", " + "fruit types = " + j + ", " + "depth = " + k + "; ");
					processMiniMax(cmap, 4);
					int turn = 0;
					//printMap(cmap);
					while(!isEmpty(cmap)){
						cmap = turn++ % 2 == 0 ? processMiniMax1(cmap, 3) : processMiniMax(cmap, 3);
						//System.out.println("Score for now is : " + score);
						//printMap(cmap);
					}
					if(score > 0){
						System.out.println("MiniMax2 win");
						cnt++;
					} else if(score < 0){
						//System.out.println("Greedy win");
						//System.out.println("Random win");
						System.out.println("MiniMax1 win");
					} else{
						System.out.println("Even");
					}
//				}
//			}

//		}
		System.out.println("MiniMax win rate is "  + cnt / 100.0);
	}

	private static void getRandomMap(char[][] cmap, int n) {
		Random rand = new Random();
		for(int i = 0; i < cmap.length; i++){
			for(int j = 0; j < cmap.length; j++){
				cmap[i][j] = (char) ('0' + rand.nextInt(n));
			}
		}
	}

	private static char[][] processGreedy(char[][] map) {
    	long starttime = System.currentTimeMillis();
		char[][] cmap = map;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		Node root = new Node(cmap, 0, true, 0, -1, -1, 1, true, alpha, beta, false, totalNumLeft);
		//System.out.println();
		int val = root.futureVal;
		
		Node res = findNextMove(root, val);
		score -= res.pastVal;
		formOutputFile(res.map, res.row, res.col);
    	long endtime = System.currentTimeMillis();
    	//System.out.println("This turn Greedy running time is " + (endtime - starttime)  + "ms"); 
		//System.out.println("Choose row : " + (res.row + 1) + ", col : " + (res.col + 1));
		return copyMap(res.map);
	}
	private static char[][] processRandom(char[][] map) {
    	long starttime = System.currentTimeMillis();
		char[][] cmap = map;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		Node root = new Node(cmap, 0, true, 0, -1, -1, 1, true, alpha, beta, true, totalNumLeft);
		//System.out.println();
		int val = root.futureVal;
		
		Node res = findNextMove(root, val);
		score -= res.pastVal;
		formOutputFile(res.map, res.row, res.col);
    	long endtime = System.currentTimeMillis();
    	System.out.println("This turn Greedy running time is " + (endtime - starttime)  + "ms"); 
		//System.out.println("Choose row : " + (res.row + 1) + ", col : " + (res.col + 1));
		return copyMap(res.map);
	}

	private static boolean isEmpty(char[][] cmap) {
		for(char[] row : cmap){
			for(char c : row){
				if(c != '*')
					return false;
			}
		}
		return true;
	}

	private static char[][] processMiniMax(char[][] map, int cutOffDepth) {
    	long starttime = System.currentTimeMillis();

		char[][] cmap = map;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		Node root = new Node(cmap, 0, true, 0, -1, -1, cutOffDepth, true, alpha, beta, false, totalNumLeft);
		//System.out.println();
		//int val = maxVal(root, alpha, beta);
		int val = root.futureVal;
		Node res = findNextMove(root, val);
		score += res.pastVal;
		formOutputFile(res.map, res.row, res.col);
    	long endtime = System.currentTimeMillis();
    	System.out.println("This turn Minimax running time is " + (endtime - starttime) / 1000.0 + "s"); 
		//System.out.println("Choose row : " + (res.row + 1) + ", col : " + (res.col + 1));
		return copyMap(res.map);
	}
	private static char[][] processMiniMax1(char[][] map, int cutOffDepth) {
    	long starttime = System.currentTimeMillis();

		char[][] cmap = map;
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		Node root = new Node(cmap, 0, true, 0, -1, -1, cutOffDepth, true, alpha, beta, false, totalNumLeft);
		int val = root.futureVal;
		Node res = findNextMove(root, val);
		score -= res.pastVal;
		//formOutputFile(res.map, res.row, res.col);
    	long endtime = System.currentTimeMillis();
    	System.out.println("This turn Minimax running time is " + (endtime - starttime) / 1000.0 + "s"); 
		return copyMap(res.map);
	}

	private static Node findNextMove(Node root, int val) {
		if(root.children == null) return root;
		for(Node child : root.children){
			if(child.futureVal == root.futureVal)
				return child;
		}
		return root;
	}

	private static int maxVal(Node node, int alpha, int beta) {
		if(cutOff(node))
			return node.futureVal;
		for(Node child : node.children){
			int val = minVal(child, alpha, beta);
			if(val > alpha) alpha = val;
			if(beta <= alpha) break;
		}
		node.futureVal = alpha;
		return alpha;
	}
	private static int minVal(Node node, int alpha, int beta) {
		if(cutOff(node))
			return node.futureVal;
		for(Node child : node.children){
			int val = maxVal(child, alpha, beta);
			if(val < beta) beta = val;
			if(beta <= alpha) break;
		}
		node.futureVal = beta;
		return beta;
	}

	private static boolean cutOff(Node node) {
		return node.children == null;
	}

	private static int fillMap( List<String> list) {
		int cnt = 0;
		for(int i = 3; i < list.size(); i++) {
			for(int j = 0; j < width; j++) {
				map[i - 3][j] = list.get(i).charAt(j);
				if(map[i - 3][j] != '*')
					cnt++;
			}
		}
		return cnt;
	}

	private static boolean verifyParas(List<String> list) {
		return true;
	}
	private static void printMap(char[][] map1){
		for(int i = 0; i < map1.length; i++){
			  for(int j = 0; j < map1[0].length; j++)
				  System.out.print(map1[i][j] + " | ");
			  System.out.println();
		}
		
	}

	private static void printError(String s) {
		System.out.println(s);
	}
	
	private static char[][] copyMap(char[][] map){
		char[][] cmap = new char[map.length][map[0].length];
		for(int i = 0; i < cmap.length; i++)
			  for(int j = 0; j < cmap[0].length; j++)
			    cmap[i][j] = map[i][j];
		return cmap;
	}
	
	private static void formOutputFile(char[][] map2, int row, int col) {

		String firstLine = "" + (char) ( col + 'A') + (row + 1); 
		PrintWriter writer;
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
		    writer.println(firstLine);
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < width; j++) 
					writer.print(map2[i][j]);
				writer.println();
			}

		    writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
class Node{
	char[][] map;
	List<Node> children;
	int pastVal, futureVal; 
	int depth, cutOffDepth;
	int alpha, beta;
	int numLeft;
	boolean max, greedy;
	int row, col;
	boolean random;
	Node(char[][] cmap, int depth, boolean max, int pastVal, int row, int col, int cutOffDepth, boolean greedy, int alpha, int beta, boolean random, int numLeft){
		this.map = copyMap(cmap);
		this.depth = depth;
		this.row = row;
		this.col = col;
		this.pastVal = pastVal;
		this.max = max;
		this.cutOffDepth = cutOffDepth;
		this.greedy = greedy;
		this.alpha = alpha;
		this.beta = beta;
		this.random = random;
		this.numLeft = numLeft;
		if(depth == cutOffDepth){
			this.futureVal = greedy? pastVal : getEstiVal();
			return;
		}
		if(random){
			this.futureVal = getChildrenRandom();
		}
		this.futureVal = max? maxVal() : minVal();
		if(this.children == null)
			this.futureVal = pastVal;
		//System.out.println("depth = " + depth + " max = " + max + " futureVal =" + futureVal +  " pastVal =" + pastVal + "; " + "beta = "+ beta + " alpha = " + alpha);
	}
	private int maxVal() {
		List<Map<Integer, List<Integer>>> ops = findOps(map);
		if(ops == null)
			return pastVal;
		int n = ops.size();
		children = new ArrayList<Node>();
		for(int i = 0; i < n; i++){
			Map<Integer, List<Integer>> hmap = ops.get(i);
			//printHashMap(hmap);
			int num = getNumber(hmap);
			int childVal = pastVal + num * num *(max? 1 : -1);
			//System.out.println("depth = " + depth + " max = " + max + " val =" + futureVal + ", " + " size = " + num + " childVal = " + childVal);
			int row = -1, col = -1;
			for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
				col = entry.getKey();
				row = entry.getValue().get(0);
				break;
			}
			children.add(new Node(play(map, ops.get(i)), this.depth + 1, !(this.max), childVal, row, col, this.cutOffDepth, this.greedy, this.alpha, this.beta, this.random, this.numLeft - num));
			int val = children.get(i).futureVal;
			if(val > alpha) alpha = val;
			if(beta <= alpha) break;
			//System.out.println(i + "," );
			//tempChildren[i].printMap();
		}
		futureVal = alpha;
		return alpha;
	}
	private int minVal() {
		List<Map<Integer, List<Integer>>> ops = findOps(map);
		if(ops == null)
			return pastVal;
		int n = ops.size();
		children = new ArrayList<Node>();
		for(int i = 0; i < n; i++){
			Map<Integer, List<Integer>> hmap = ops.get(i);
			//printHashMap(hmap);
			int num = getNumber(hmap);
			int childVal = pastVal + num * num *(max? 1 : -1);
			//System.out.println("depth = " + depth + " max = " + max + " futureVal =" + futureVal + ", " + " size = " + num + " chilVal = " + childVal);
			int row = -1, col = -1;
			for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
				col = entry.getKey();
				row = entry.getValue().get(0);
				break;
			}
			children.add(new Node(play(map, ops.get(i)), this.depth + 1, !(this.max), childVal, row, col, this.cutOffDepth, this.greedy, this.alpha, this.beta, this.random, this.numLeft - num));
			int val = children.get(i).futureVal;
			if(val < beta) beta = val;
			if(beta <= alpha) break;
			//System.out.println(i + "," );
			//tempChildren[i].printMap();
		}
		futureVal = beta;
		return beta;
	}
	private int getEstiVal() {
		List<Map<Integer, List<Integer>>> ops = findOps(map);
		if(ops == null){
			return pastVal;
		}
		int most = 0;
		//List<Integer> sqrs = new ArrayList<>();
		for(Map<Integer, List<Integer>> hmap : ops){
			int size = 0;
			for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
				size += entry.getValue().size();
			}
			most = Math.max(most, size);
		}
		return pastVal + (max? -1 : 1) * most * most;
	}

//	private Node[] getChildren() {
//		List<Map<Integer, List<Integer>>> ops = findOps(map);
//		if(ops == null)
//			return null;
//		int n = ops.size();
//
//		Node[] tempChildren = new Node[n];
//		for(int i = 0; i < n; i++){
//			Map<Integer, List<Integer>> hmap = ops.get(i);
//			//printHashMap(hmap);
//			int num = getNumber(hmap);
//			int childVal = pastVal + num * num *(max? 1 : -1);
//			//System.out.println("depth = " + depth + " max = " + max + " val =" + val + ", " + " size = " + num + " chilVal = " + childVal);
//			int row = -1, col = -1;
//			for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
//				col = entry.getKey();
//				row = entry.getValue().get(0);
//			}
//			
//			tempChildren[i] = new Node(play(map, ops.get(i)), this.depth + 1, !(this.max), childVal, row, col, this.cutOffDepth, this.greedy);
//			//System.out.println(i + "," );
//			//tempChildren[i].printMap();
//		}
//		return tempChildren;
//	}
	private int getChildrenRandom() {
		List<Map<Integer, List<Integer>>> ops = findOps(map);
		if(ops == null)
			return pastVal;
		int n = ops.size();
		children = new ArrayList<Node>();
		for(int i = 0; i < n; i++){
			Map<Integer, List<Integer>> hmap = ops.get(i);
			int num = getNumber(hmap);
			int childVal = pastVal + num * num *(max? 1 : -1);
			//System.out.println("depth = " + depth + " max = " + max + " val =" + futureVal + ", " + " size = " + num + " childVal = " + childVal);
			int row = -1, col = -1;
			for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
				col = entry.getKey();
				row = entry.getValue().get(0);
				break;
			}
			children.add(new Node(play(map, ops.get(i)), this.depth + 1, !(this.max), childVal, row, col, this.cutOffDepth, this.greedy, this.alpha, this.beta, this.random, this.numLeft - num));
		}
		Random rand = new Random();
		return children.get(rand.nextInt(n)).futureVal;
	}
	private int getNumber(Map<Integer, List<Integer>> hmap) {
		int sum = 0;
		for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
			sum += entry.getValue().size();
		}
	return sum;
}
	private void printHashMap(Map<Integer, List<Integer>> hmap) {
		for(Map.Entry<Integer, List<Integer>> entry : hmap.entrySet()){
			System.out.print(entry.getKey() + "->");
			for(int i : entry.getValue()){
				System.out.print(i + ", ");
			}
			System.out.println();
		}
	
	}
	private char[][] play(char[][] map2, Map<Integer, List<Integer>> map3) {
		char[][] cmap = copyMap(map2);
		for(Map.Entry<Integer, List<Integer>> entry : map3.entrySet()) {
			int max = 0, min = cmap.length - 1;
			int col = entry.getKey();
			for(int row : entry.getValue()){
				cmap[row][col] = '*';
				min = Math.min(min, row);
				max = Math.max(max, row);
			}
			moveEmpty(cmap, col, min, max);
		}
		return cmap;
	}
	private void moveEmpty(char[][] map2, int col, int min, int max) {
		//System.out.println("min = " + min + ", " + "max = " + max);
		int count = max;
		for(int i = max - 1; i >= 0; i--){
			if(map2[i][col] != '*'){
				map2[count--][col] = map2[i][col];
				map2[i][col] = '*';
			}
		}	
	}
	private List<Map<Integer, List<Integer>>> findOps(char[][] map) {
		char[][] map2 = copyMap(map);
		List<Map<Integer, List<Integer>>> res = new ArrayList<>();
		int left = this.numLeft;
		for(int i = 0; i < map2.length; i++){
			for(int j = 0; j < map2[0].length; j++){
				if(left == 0)
					break;
				if(map2[i][j] != '*'){
					Map<Integer, List<Integer>> hmap = new HashMap<>();
					findSame(map2, i, j, map2[i][j], hmap);
					res.add(hmap);
					left -= getNumber(hmap);
				}
			}
		}
		Collections.sort(res, new Comparator<Map<Integer, List<Integer>>>(){
			public int compare(Map<Integer, List<Integer>> hmap1, Map<Integer, List<Integer>> hmap2){
				return getNumber(hmap2) - getNumber(hmap1);
			}
		});
		if(res.size() == 0){
			return null;
		}
		return res;
	}
	private char[][] copyMap(char[][] map){
		char[][] cmap = new char[map.length][map[0].length];
		for(int i = 0; i < cmap.length; i++)
			  for(int j = 0; j < cmap[0].length; j++)
			    cmap[i][j] = map[i][j];
		return cmap;
	}
	private void findSame(char[][] map2, int i, int j, char c, Map<Integer, List<Integer>> hmap) {
		if(i < 0 || i >= map2.length || j < 0 || j >= map2[0].length || map2[i][j] != c)
			return;
		map2[i][j] = '*';
		if(!hmap.containsKey(j)){
			List<Integer> list = new ArrayList<>();
			hmap.put(j, list);
		}
		List<Integer> list = hmap.get(j);
		list.add(i);
		findSame(map2, i - 1, j, c, hmap);
		findSame(map2, i + 1, j, c, hmap);
		findSame(map2, i, j - 1, c, hmap);
		findSame(map2, i, j + 1, c, hmap);
	}
	public String toString(){
		return "depth = " + this.depth + ", " 
				+ "row = " + this.row + ", "  
				+ "col = " + this.col + ", " 
				+ "futureVal = " + this.futureVal + "; ";
		
	}
	public void printMap(){
		for(int i = 0; i < map.length; i++){
			  for(int j = 0; j < map[0].length; j++)
				  System.out.print(map[i][j] + " | ");
			  System.out.println();
		}
		
	}
}
