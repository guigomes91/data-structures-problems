package arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TwoSumArrayTest {

    private TwoSumArray twoSumArray;

    public TwoSumArrayTest() {
        this.twoSumArray = new TwoSumArray();
    }

    /*
        Example 1:

        Input: nums = [2,7,11,15], target = 9
        Output: [0,1]
        Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
        Example 2:

        Input: nums = [3,2,4], target = 6
        Output: [1,2]
        Example 3:

        Input: nums = [3,3], target = 6
        Output: [0,1]
     */
    @Test
    void testTwoSumOne() {
        int[] result = {0, 1};
        Assertions.assertArrayEquals(result,
                twoSumArray.calculate(new int[]{2, 7, 11, 15}, 9));
    }
}