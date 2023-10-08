package org.url_fuzzer_403_bypass;

public class PermutationCalculator {
    static int fact(int number) {
        int f = 1;
        int j = 1;
        while(j <= number) {
            f = f * j;
            j++;
        }
        return f;
    }
}
