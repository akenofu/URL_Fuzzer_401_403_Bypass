package org.url_fuzzer_403_bypass;

import java.util.Arrays;

public class CombinationGenerator {
    public static void generatePermutations(char[] charArray, char[] current, int index, int n) {
        if (index == n) {
            // Print the current permutation
            System.out.println(Arrays.toString(current));
            return;
        }

        for (int i = 0; i < charArray.length; i++) {
            current[index] = charArray[i];
            generatePermutations(charArray, current, index + 1, n);
        }
    }

    public static void main(String[] args) {

        char[] charArray = new char[255];

        for(int i = 0 ; i < 10; i++){
            charArray[i] = (char) i;
        }
        int n = 3; // Length of permutations

        char[] current = new char[n];

        generatePermutations(charArray, current, 0, n);
    }
}
