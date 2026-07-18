package arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
    The overall run time complexity should be O(log (m+n)).
 */
public class MedianSortedArrays {

    public static void main(String[] args) {
        findMedianSortedArrays(new int[]{1, 3}, new int[]{2});
    }

    /*
        Example 1:

        Input: nums1 = [1,3], nums2 = [2]
        Output: 2.00000
        Explanation: merged array = [1,2,3] and median is 2.
     */
    public static void findMedianSortedArrays(int[] nums1, int[] nums2) {
        List<Integer> merged = new ArrayList<>();

        for (int j : nums1) {
            merged.add(j);
        }

        for (int j : nums2) {
            merged.add(j);
        }

        Collections.sort(merged);
        int divisor = merged.size() % 2;

        if (divisor == 0) {

        }
    }
}
