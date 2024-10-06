package org.crypography_tools;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LinearAlgebra {

    public static double[][] _2x2ID = new double[][] {{1, 0}, {0, 1}};


    public static double[][] MxS(double[][] matrix, double scalar) {
        return Map(matrix, x -> x * scalar);
    }

    public static double[][] MxM(double[][] a, double[][] b) {

        if (a[0].length != b.length) {
            return _2x2ID;
        }

        int n = a.length;
        int p = b[0].length;
        int m = a[0].length;

        double[][] output = new double[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                double sum = 0;

                for (int k = 0; k < m; k++) {
                    sum += a[i][k] * b[k][j];
                }

                output[i][j] = sum;
            }
        }

        return output;

    }

    public static double[][] MxMod(double[][] a, double[][] b, int mod) {

        if (a[0].length != b.length) {
            return _2x2ID;
        }

        int n = a.length;
        int p = b[0].length;
        int m = a[0].length;

        double[][] output = new double[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                int sum = 0;

                for (int k = 0; k < m; k++) {
                    sum += a[i][k] * b[k][j];
                }

                output[i][j] = Math.floorMod(sum, mod);
            }
        }

        return output;
    }

    public static double[][] Map(double[][] matrix, LambdaDouble app) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = app.ly(matrix[i][j]);
            }
        }

        return matrix;
    }

    public static int[][] ToInt(double[][] matrix) {
        int[][] output = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                output[i][j] = (int) matrix[i][j];
            }
        }

        return output;
    }

    public static double _2x2Det(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
    }

    public static double[][] _2x2Adj(double[][] matrix) {
        return new double[][] {{matrix[1][1], -matrix[0][1]}, {-matrix[1][0], matrix[0][0]}};
    }

    public static double[][] _2x2Inverse(double[][] matrix) {
        double inverse_det = 1D / _2x2Det(matrix);
        double[][] adjugate = _2x2Adj(matrix);

        return MxS(adjugate, inverse_det);
    }

    public static double[][] _2x2InverseMod(double[][] matrix, int mod) {
        double inverse_det = Tools.ModularInverse((int) _2x2Det(matrix), mod);
        double[][] adjugate = _2x2Adj(matrix);

        double[][] inverse = MxS(adjugate, inverse_det);

        return Map(inverse, x -> Math.floorMod((int) x, mod));
    }

    public static void DisplayMatrix (double[][] matrix) {
        for (double[] row : matrix) {
            String row_string = Arrays.stream(row).mapToObj(String::valueOf).collect(Collectors.joining(" "));
            System.out.printf("| %s |\n", row_string);
        }
    }

    public static String MatrixToString(double[][] matrix) {
        String output = "{";

        for (double[] row : matrix) {
            output += Arrays.toString(row).replaceAll("\\[", "{").replaceAll("]", "}");
            output += ", ";
        }

        output = output.substring(0, output.length()-2);

        output += "}";
        return output;
    }
    public static String MatrixToString(double[][] matrix, boolean isInt) {
        String output = "{";

        int[][] intMatrix = ToInt(matrix);

        for (int[] row : intMatrix) {
            output += Arrays.toString(row).replaceAll("\\[", "{").replaceAll("]", "}");
            output += ", ";
        }

        output = output.substring(0, output.length()-2);

        output += "}";
        return output;
    }
}
