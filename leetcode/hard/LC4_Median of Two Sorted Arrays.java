// 414. Median of Two Sorted Arrays
// DescriptionHintsSubmissionsDiscussSolution
      
// Very concise O(log(min(M,N))) iterative solution with detailed explanation
// Very concise O(log(min(M,N))) iterative solution with detailed explanation
// 96.1K
// 463
// stellari
// stellari
//  2147
// Last Edit: 2 days ago

// This problem is notoriously hard to implement due to all the corner cases. Most implementations consider odd-lengthed and even-lengthed arrays as two different cases and treat them separately. As a matter of fact, with a little mind twist. These two cases can be combined as one, leading to a very simple solution where (almost) no special treatment is needed.

// First, let’s see the concept of ‘MEDIAN’ in a slightly unconventional way. That is:

// “if we cut the sorted array to two halves of EQUAL LENGTHS, then
// median is the AVERAGE OF Max(lower_half) and Min(upper_half), i.e. the
// two numbers immediately next to the cut”.

// For example, for [2 3 5 7], we make the cut between 3 and 5:

// [2 3 / 5 7]
// then the median = (3+5)/2. Note that I’ll use ‘/’ to represent a cut, and (number / number) to represent a cut made through a number in this article.

// for [2 3 4 5 6], we make the cut right through 4 like this:

// [2 3 (4/4) 5 7]

// Since we split 4 into two halves, we say now both the lower and upper subarray contain 4. This notion also leads to the correct answer: (4 + 4) / 2 = 4;

// For convenience, let’s use L to represent the number immediately left to the cut, and R the right counterpart. In [2 3 5 7], for instance, we have L = 3 and R = 5, respectively.

// We observe the index of L and R have the following relationship with the length of the array N:

// N        Index of L / R
// 1               0 / 0
// 2               0 / 1
// 3               1 / 1  
// 4               1 / 2      
// 5               2 / 2
// 6               2 / 3
// 7               3 / 3
// 8               3 / 4
// It is not hard to conclude that index of L = (N-1)/2, and R is at N/2. Thus, the median can be represented as

// (L + R)/2 = (A[(N-1)/2] + A[N/2])/2
// To get ready for the two array situation, let’s add a few imaginary ‘positions’ (represented as #'s) in between numbers, and treat numbers as ‘positions’ as well.

// [6 9 13 18]  ->   [# 6 # 9 # 13 # 18 #]    (N = 4)
// position index     0 1 2 3 4 5  6 7  8     (N_Position = 9)
		  
// [6 9 11 13 18]->   [# 6 # 9 # 11 # 13 # 18 #]   (N = 5)
// position index      0 1 2 3 4 5  6 7  8 9 10    (N_Position = 11)
// As you can see, there are always exactly 2*N+1 ‘positions’ regardless of length N. Therefore, the middle cut should always be made on the Nth position (0-based). Since index(L) = (N-1)/2 and index® = N/2 in this situation, we can infer that index(L) = (CutPosition-1)/2, index® = (CutPosition)/2.

// Now for the two-array case:

// A1: [# 1 # 2 # 3 # 4 # 5 #]    (N1 = 5, N1_positions = 11)

// A2: [# 1 # 1 # 1 # 1 #]     (N2 = 4, N2_positions = 9)
// Similar to the one-array problem, we need to find a cut that divides the two arrays each into two halves such that

// “any number in the two left halves” <= “any number in the two right
// halves”.

// We can also make the following observations：

// There are 2N1 + 2N2 + 2 position altogether. Therefore, there must be exactly N1 + N2 positions on each side of the cut, and 2 positions directly on the cut.

// Therefore, when we cut at position C2 = K in A2, then the cut position in A1 must be C1 = N1 + N2 - k. For instance, if C2 = 2, then we must have C1 = 4 + 5 - C2 = 7.

//  [# 1 # 2 # 3 # (4/4) # 5 #]    

//  [# 1 / 1 # 1 # 1 #]   
// When the cuts are made, we’d have two L’s and two R’s. They are

//  L1 = A1[(C1-1)/2]; R1 = A1[C1/2];
//  L2 = A2[(C2-1)/2]; R2 = A2[C2/2];
// In the above example,

//     L1 = A1[(7-1)/2] = A1[3] = 4; R1 = A1[7/2] = A1[3] = 4;
//     L2 = A2[(2-1)/2] = A2[0] = 1; R2 = A1[2/2] = A1[1] = 1;
// Now how do we decide if this cut is the cut we want? Because L1, L2 are the greatest numbers on the left halves and R1, R2 are the smallest numbers on the right, we only need

// L1 <= R1 && L1 <= R2 && L2 <= R1 && L2 <= R2
// to make sure that any number in lower halves <= any number in upper halves. As a matter of fact, since
// L1 <= R1 and L2 <= R2 are naturally guaranteed because A1 and A2 are sorted, we only need to make sure:

// L1 <= R2 and L2 <= R1.

// Now we can use simple binary search to find out the result.

// If we have L1 > R1, it means there are too many large numbers on the left half of A1, then we must move C1 to the left (i.e. move C2 to the right); 
// If L2 > R1, then there are too many large numbers on the left half of A2, and we must move C2 to the left.
// Otherwise, this cut is the right one. 
// After we find the cut, the medium can be computed as (max(L1, L2) + min(R1, R2)) / 2;
// Two side notes:

// A. Since C1 and C2 can be mutually determined from each other, we can just move one of them first, then calculate the other accordingly. However, it is much more practical to move C2 (the one on the shorter array) first. The reason is that on the shorter array, all positions are possible cut locations for median, but on the longer array, the positions that are too far left or right are simply impossible for a legitimate cut. For instance, [1], [2 3 4 5 6 7 8]. Clearly the cut between 2 and 3 is impossible, because the shorter array does not have that many elements to balance out the [3 4 5 6 7 8] part if you make the cut this way. Therefore, for the longer array to be used as the basis for the first cut, a range check must be performed. It would be just easier to do it on the shorter array, which requires no checks whatsoever. Also, moving only on the shorter array gives a run-time complexity of O(log(min(N1, N2))) (edited as suggested by @baselRus)

// B. The only edge case is when a cut falls on the 0th(first) or the 2Nth(last) position. For instance, if C2 = 2N2, then R2 = A2[2*N2/2] = A2[N2], which exceeds the boundary of the array. To solve this problem, we can imagine that both A1 and A2 actually have two extra elements, INT_MAX at A[-1] and INT_MAX at A[N]. These additions don’t change the result, but make the implementation easier: If any L falls out of the left boundary of the array, then L = INT_MIN, and if any R falls out of the right boundary, then R = INT_MAX.

// I know that was not very easy to understand, but all the above reasoning eventually boils down to the following concise code:

//  double findMedianSortedArrays(vector<int>& nums1, vector<int>& nums2) {
//     int N1 = nums1.size();
//     int N2 = nums2.size();
//     if (N1 < N2) return findMedianSortedArrays(nums2, nums1);	// Make sure A2 is the shorter one.
    
//     int lo = 0, hi = N2 * 2;
//     while (lo <= hi) {
//         int mid2 = (lo + hi) / 2;   // Try Cut 2 
//         int mid1 = N1 + N2 - mid2;  // Calculate Cut 1 accordingly
        
//         double L1 = (mid1 == 0) ? INT_MIN : nums1[(mid1-1)/2];	// Get L1, R1, L2, R2 respectively
//         double L2 = (mid2 == 0) ? INT_MIN : nums2[(mid2-1)/2];
//         double R1 = (mid1 == N1 * 2) ? INT_MAX : nums1[(mid1)/2];
//         double R2 = (mid2 == N2 * 2) ? INT_MAX : nums2[(mid2)/2];
        
//         if (L1 > R2) lo = mid2 + 1;		// A1's lower half is too big; need to move C1 left (C2 right)
//         else if (L2 > R1) hi = mid2 - 1;	// A2's lower half too big; need to move C2 left.
//         else return (max(L1,L2) + min(R1, R2)) / 2;	// Otherwise, that's the right cut.
//     }
//     return -1;
// } 
// If you have any suggestions to make the logic and implementation even more cleaner. Please do let me know!

// Comments:  88
// wjq987564321

// Type comment here... (Markdown is supported)
// xcw0754
// xcw0754 36  Nov 16, 2015, 1:51 AMLink CopiedShare
// it’s very hard to understand…

// 42Report post
// hieule246
// hieule246 20  Jul 29, 2015, 1:36 PMLink CopiedShare
// this is a very good explanation, you have a minor typo though : “If we have L1 > R1”

// should it be L1 > R2 ?

// 20Report post
// tju_xu
// tju_xu 240  Aug 8, 2015, 6:06 AMLink CopiedShare
// Elegant code! Perfect explanation! @stellari, Thank you very much!

// The following is python version of your solution:

//  def findMedianSortedArrays(self, nums1, nums2):
//         N1, N2 = len(nums1), len(nums2)
//         if N1 < N2: 
//             nums1, N1, nums2, N2 = nums2, N2, nums1, N1
//         l, r = 0, N2*2
//         while l <= r:
//             j = (l + r) >> 1
//             i = N1 + N2 - j
//             L1 = -sys.maxint-1 if i == 0 else nums1[(i-1)>>1]
//             L2 = -sys.maxint-1 if j == 0 else nums2[(j-1)>>1]
//             R1 = sys.maxint if i == 2*N1 else nums1[i>>1]
//             R2 = sys.maxint if j == 2*N2 else nums2[j>>1]
//             if L1 > R2: l = j + 1
//             elif L2 > R1: r = j - 1
//             else:
//                 return (max(L1, L2) + min(R1, R2))/2.0
// 7Read MoreReport post
// 1337c0d3r
// 1337c0d3r 2065  Jun 23, 2015, 9:32 AMLink CopiedShare
// Thanks for sharing your detailed explanation and solution with us, @stellari. :)

// 7Report post
// stellari
// stellari 2147  Jul 25, 2016, 9:55 PMLink CopiedShare
// @brookc Thank you for your comment. Basically, “L1 = A1[(C1-1)/2]; R1 = A1[C1/2];” means “If a cut is on position C1, then the number immediately on the left of C1 is A1[(C1-1)/2], and the number on the right is A1[C1/2]”. You can easily validate this conclusion. For example, suppose A1 has 5 elements, then there would be in total 6 blank spots + 5 numbers = 11 positions. The blanks are at position 0, 2, 4, 6, 8, 10, and the numbers are at 1, 3, 5, 7, 9. If we make a cut at the 8th position, which is a blank. Then the number to its left (i.e. L) is A1[(8-1)/2] = A1[3] and the one to its right ® = A1[8/2] = A1[4]. This result is correct because 8 happens to be the position between A1[3] and A1[4]. You can similarly validate the case where the cut is made on an odd position (i.e. right through a number, instead of between two numbers), you will get the same result.

// 4Read MoreReport post
// codersx
// codersx 6  Aug 15, 2015, 5:19 AMLink CopiedShare
// if we cut the sorted array to two halves of EQUAL LENGTHS, then median is the AVERAGE OF Min(lowerhalf) and Max(upperhalf), i.e. the two numbers immediately next to the cut
// I think this statement should be -
// if we cut the sorted array to two halves of EQUAL LENGTHS, then median is the AVERAGE OF Max(lowerhalf) and Min(upperhalf), i.e. the two numbers immediately next to the cut

// 4Read MoreReport post
// Lihuas_LC
// Lihuas_LC 8  Jun 29, 2016, 12:19 PMLink CopiedShare
// This a java version based on stellari’s idea

// public class Solution {
// public double findMedianSortedArrays(int[] nums1, int[] nums2) {
//     int N1 = nums1.length, N2 = nums2.length;
//     if (N1 > N2) return findMedianSortedArrays(nums2, nums1);
    
//     int lo = 0, hi = 2 * N1;
//     while (lo <= hi) {
//         int C1 = (lo + hi) / 2;
//         int C2 = N1 + N2 - C1;
        
//         double L1 = (C1 == 0) ? Integer.MIN_VALUE : nums1[(C1-1)/2];
//         double R1 = (C1 == 2*N1) ? Integer.MAX_VALUE : nums1[C1/2];
//         double L2 = (C2 == 0) ? Integer.MIN_VALUE : nums2[(C2-1)/2];
//         double R2 = (C2 == 2*N2) ? Integer.MAX_VALUE : nums2[C2/2];
        
//         if (L1 > R2) hi = C1 - 1;
//         else if (L2 > R1) lo = C1 + 1;
//         else return (Math.max(L1, L2) + Math.min(R1, R2)) / 2;
//     }
//     return -1;
// } ​
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        if(m < n){
            return findMedianSortedArrays(nums2, nums1);
        }
        int lo = 0, hi = 2 * n;
        while(lo <= hi){
            int mid2 = (lo + hi) / 2;
            int mid1 = m + n - mid2;
            double L1 = mid1 == 0 ? Integer.MIN_VALUE : nums1[(mid1-1)/2];
            double L2 = mid2 == 0 ? Integer.MIN_VALUE : nums2[(mid2-1)/2];
            double R1 = mid1 == 2 * m ? Integer.MAX_VALUE : nums1[mid1/2];
            double R2 = mid2 == 2 * n ? Integer.MAX_VALUE : nums2[mid2/2];
            if(L1 > R2){
                lo = mid2 + 1;
            } else if(L2 > R1){
                hi = mid2 - 1;
            } else{
                return (Math.max(L1,L2) + Math.min(R1, R2)) / 2;
            }
        }
        return -1;
    }
}
