package algorithms.searching;

import java.util.*;

/**
 * Spiral Matrix Problems in Java
 */
public class MatrixSpiral {

    // 1. Spiral traversal — O(m*n) time, O(1) space
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 0) return result;
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++)  result.add(matrix[top][c]);
            top++;
            for (int r = top; r <= bottom; r++)  result.add(matrix[r][right]);
            right--;
            if (top <= bottom) { for (int c = right; c >= left; c--) result.add(matrix[bottom][c]); bottom--; }
            if (left <= right) { for (int r = bottom; r >= top; r--) result.add(matrix[r][left]); left++; }
        }
        return result;
    }

    // 2. Generate n×n spiral matrix
    public static int[][] generateSpiral(int n) {
        int[][] matrix = new int[n][n];
        int top = 0, bottom = n-1, left = 0, right = n-1, num = 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++)  matrix[top][c] = num++;
            top++;
            for (int r = top; r <= bottom; r++)  matrix[r][right] = num++;
            right--;
            if (top <= bottom) { for (int c = right; c >= left; c--) matrix[bottom][c] = num++; bottom--; }
            if (left <= right) { for (int r = bottom; r >= top; r--) matrix[r][left] = num++; left++; }
        }
        return matrix;
    }

    // 3. Rotate matrix 90 degrees clockwise — in-place
    public static void rotate90(int[][] matrix) {
        int n = matrix.length;
        // Step 1: Transpose
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++) { int t = matrix[i][j]; matrix[i][j] = matrix[j][i]; matrix[j][i] = t; }
        // Step 2: Reverse each row
        for (int[] row : matrix) {
            int l = 0, r = n-1;
            while (l < r) { int t = row[l]; row[l++] = row[r]; row[r--] = t; }
        }
    }

    // 4. Diagonal traversal
    public static int[] findDiagonalOrder(int[][] matrix) {
        if (matrix.length == 0) return new int[0];
        int m = matrix.length, n = matrix[0].length;
        int[] result = new int[m * n];
        int idx = 0, d = 1; // d=1 going up-right, d=-1 going down-left
        int r = 0, c = 0;
        while (idx < m * n) {
            result[idx++] = matrix[r][c];
            int nr = r - d, nc = c + d;
            if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                if (d == 1) { r += (c == n-1 ? 1 : 0); c += (c < n-1 ? 1 : 0); }
                else        { c += (r == m-1 ? 1 : 0); r += (r < m-1 ? 1 : 0); }
                d = -d;
            } else { r = nr; c = nc; }
        }
        return result;
    }

    // 5. Search in 2D sorted matrix — O(m+n)
    public static boolean searchMatrixII(int[][] matrix, int target) {
        int r = 0, c = matrix[0].length - 1;
        while (r < matrix.length && c >= 0) {
            if (matrix[r][c] == target) return true;
            if (matrix[r][c] > target) c--;
            else r++;
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] m = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println(spiralOrder(m)); // [1,2,3,6,9,8,7,4,5]

        int[][] spiral = generateSpiral(4);
        for (int[] row : spiral) System.out.println(Arrays.toString(row));

        rotate90(m);
        for (int[] row : m) System.out.println(Arrays.toString(row)); // rotated
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Spiral Matrix Problems in Java
 */
public class MatrixSpiral {

    // 1. Spiral traversal — O(m*n) time, O(1) space
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 0) return result;
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++)  result.add(matrix[top][c]);
            top++;
            for (int r = top; r <= bottom; r++)  result.add(matrix[r][right]);
            right--;
            if (top <= bottom) { for (int c = right; c >= left; c--) result.add(matrix[bottom][c]); bottom--; }
            if (left <= right) { for (int r = bottom; r >= top; r--) result.add(matrix[r][left]); left++; }
        }
        return result;
    }

    // 2. Generate n×n spiral matrix
    public static int[][] generateSpiral(int n) {
        int[][] matrix = new int[n][n];
        int top = 0, bottom = n-1, left = 0, right = n-1, num = 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++)  matrix[top][c] = num++;
            top++;
            for (int r = top; r <= bottom; r++)  matrix[r][right] = num++;
            right--;
            if (top <= bottom) { for (int c = right; c >= left; c--) matrix[bottom][c] = num++; bottom--; }
            if (left <= right) { for (int r = bottom; r >= top; r--) matrix[r][left] = num++; left++; }
        }
        return matrix;
    }

    // 3. Rotate matrix 90 degrees clockwise — in-place
    public static void rotate90(int[][] matrix) {
        int n = matrix.length;
        // Step 1: Transpose
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++) { int t = matrix[i][j]; matrix[i][j] = matrix[j][i]; matrix[j][i] = t; }
        // Step 2: Reverse each row
        for (int[] row : matrix) {
            int l = 0, r = n-1;
            while (l < r) { int t = row[l]; row[l++] = row[r]; row[r--] = t; }
        }
    }

    // 4. Diagonal traversal
    public static int[] findDiagonalOrder(int[][] matrix) {
        if (matrix.length == 0) return new int[0];
        int m = matrix.length, n = matrix[0].length;
        int[] result = new int[m * n];
        int idx = 0, d = 1; // d=1 going up-right, d=-1 going down-left
        int r = 0, c = 0;
        while (idx < m * n) {
            result[idx++] = matrix[r][c];
            int nr = r - d, nc = c + d;
            if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                if (d == 1) { r += (c == n-1 ? 1 : 0); c += (c < n-1 ? 1 : 0); }
                else        { c += (r == m-1 ? 1 : 0); r += (r < m-1 ? 1 : 0); }
                d = -d;
            } else { r = nr; c = nc; }
        }
        return result;
    }

    // 5. Search in 2D sorted matrix — O(m+n)
    public static boolean searchMatrixII(int[][] matrix, int target) {
        int r = 0, c = matrix[0].length - 1;
        while (r < matrix.length && c >= 0) {
            if (matrix[r][c] == target) return true;
            if (matrix[r][c] > target) c--;
            else r++;
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] m = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println(spiralOrder(m)); // [1,2,3,6,9,8,7,4,5]

        int[][] spiral = generateSpiral(4);
        for (int[] row : spiral) System.out.println(Arrays.toString(row));

        rotate90(m);
        for (int[] row : m) System.out.println(Arrays.toString(row)); // rotated
    }
}
