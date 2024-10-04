package org.crypography_tools;

public class LinearAlgebra {

    public static int[][] _2x2ID = new int[][] {{1, 0}, {0, 1}};

    public static int[][] MxM(int[][] a, int[][] b) {

        if (a[0].length != b.length) {
            return _2x2ID;
        }

        int n = a.length;
        int p = b[0].length;
        int m = a[0].length;

        int[][] output = new int[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                int sum = 0;

                for (int k = 0; k < m; k++) {
                    sum += a[i][k] * b[k][j];
                }

                output[i][j] = sum;
            }
        }

        return output;

    }
}
