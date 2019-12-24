/**
 * Author: Matt Fishel, 2019
 * A Matrix data type with options for construction and methods for various linear algebra
 * operations. Part of an extra credit project for MATH 1554 at Georgia Tech, Summer 2019.
 *
 * NOTE: Row (m) and Column (n) parameters expect to receive indices starting at 1, following
 * linear algebra conventions rather than the typical CS array convention. For example,
 * setEntry(2, 5, 1.0) assigns the element of the 2nd row, 5th column to be 1.0.
 */

public class Matrix {
    public final int M, N;
    private final double[][] A;

    //constructors

    /**
     * Constructs the 2*2 identity matrix.
     */
    public Matrix() {
        this(2, 2);
        double[][] a = {{1, 0},
                        {0, 1}};
        this.setEntries(a);
    }

    /**
     * Constructs a Matrix from a rectangular double[][] array argument.
     * @param A The 2d double[][] array from which to construct this Matrix.
     * @throws IllegalArgumentException if the argument array is jagged.
     */
    public Matrix(double[][] A) {
        for (int i = 1; i < A.length; i++) {
            if (A[i].length != A[0].length) {
                throw new IllegalArgumentException("Cannot construct Matrix from jagged array.");
            }
        }
        this.M = A.length;
        this.N = A[0].length;
        this.A = new double[this.M][this.N];
        this.setEntries(A);
    }

    /**
     * Constructs an m*n Matrix of all 0's.
     * @param M the number of rows for this matrix.
     * @param N the number of columns for this matrix.
     */
    public Matrix(int M, int N) {
        this(new double[M][N]);
    }

    //getters and setters

    /**
     * Sets the element at the ith row and jth column.
     * @param i row of the element to assign.
     * @param j column of the element to assign.
     * @param entry the value to assign to the element.
     * @throws IllegalArgumentException if i is less than 1 or greather than m, or if j is less
     * than 1 or greater than n.
     */
    public void setEntry(int i, int j, double entry) throws IllegalArgumentException {
        if (i < 1 || i > M || j < 1 || j > N) {
            throw new IllegalArgumentException("Element position not found in Matrix.");
        }
        this.A[i - 1][j - 1] = entry;
    }

    /**
     * Sets all Matrix entries from a 2d double array. Argument array must have m * n dimensions.
     * Performs a per-element copy. Argument array will be unaffected by operations on the Matrix.
     * @param a A double[][] array to assign all values to this Matrix.
     * @throws NullPointerException if argument array is null.
     * @throws IllegalArgumentException if argument array dimensions are invalid.
     */
    public void setEntries(double[][] a) throws NullPointerException, IllegalArgumentException {
        if (a == null) {
            throw new NullPointerException("Argument array was null.");
        }
        if (a.length != M || a[0].length != N) {
            throw new IllegalArgumentException("Argument array has invalid dimensions.");
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.A[i][j] = a[i][j];
            }
        }
    }

    /**
     * gets the element at the ith row, jth column.
     * @param i row of element to get
     * @param j column of element to get
     * @return double found at the ith row, jth column
     */
    public double getEntry(int i, int j) {
        return this.A[i - 1][j - 1];
    }

    /**
     * @return a deep copy of the 2d double[][] backing array.
     */
    public double[][] getEntries() {
        double[][] entries = new double[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                entries[i][j] = this.A[i][j];
            }
        }
        return entries;
    }

    /**
     * Intended for retrieving a copy of Matrix A for the augmented Matrix A|B
     * @return a deep copy Matrix of this Matrix less the nth column.
     */
    public Matrix getA() {
        double[][] entries = new double[M][N - 1];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < (N - 1); j++) {
                entries[i][j] = this.A[i][j];
            }
        }
        return new Matrix(entries);
    }

    /**
     * Intended for retrieving a copy of Matrix B for the augmented Matrix A|B
     * @return a deep copy Matrix of the nth column of this Matrix.
     */
    public Matrix getB() {
        double[][] entries = new double[M][1];
        for (int i = 0; i < M; i++) {
            entries[i][0] = this.A[i][N - 1];
        }
        return new Matrix(entries);
    }

    /**
     * Constructs and returns an augmented Matrix from this Matrix and argument Matrix B
     * Note: B may have more than one column.
     * @param b The Matrix with which to augment this Matrix.
     * @return a deep copy augmented Matrix A|B
     * @throws IllegalArgumentException if B has more or fewer rows than A
     */
    public Matrix augment(Matrix b) throws IllegalArgumentException {
        if (this.M != b.M) {
            throw new IllegalArgumentException("Invalid Matrix Dimensions.");
        }
        double[][] entries = new double[this.M][this.N + b.N];
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                entries[i][j] = this.A[i][j];
            }
            for (int j = 0; j < b.N; j++) {
                entries[i][j + this.N] = b.getEntry(i + 1, j + 1);
            }
        }
        return new Matrix(entries);
    }

    /**
     * @return a deep copy of this Matrix.
     */
    public Matrix getCopy() {
        double[][] entries = this.getEntries();
        Matrix x = new Matrix(entries);
        return x;
    }

    //matrix operations
    public Matrix getTranspose() {
        double[][] values = new double[N][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                values[j][i] = this.A[i][j];
            }
        }
        return new Matrix(values);
    }

    public Matrix leftMultiply(Matrix x) {
        double[][] values = new double[x.M][this.N];
        for (int i = 1; i <= x.M; i++) {
            for (int j = 1; j <= this.N; j++) {
                for (int k = 1; k <= this.M; k++) {
                    values[i - 1][j - 1] += x.getEntry(i, k) * this.getEntry(k, j);
                }
            }
        }
        return new Matrix(values);
    }

    public Matrix rightMultiply(Matrix x) {
        double[][] values = new double[this.M][x.N];
        for (int i = 1; i <= this.M; i++) {
            for (int j = 1; j <= x.N; j++) {
                for (int k = 0; k <= x.M; k++) {
                    values[i - 1][j - 1] += this.getEntry(i, k) * x.getEntry(k, j);
                }
            }
        }
        return new Matrix(values);
    }

    public void rowSwap(int r1, int r2) {
        double[] temp= this.A[r1 - 1];
        this.A[r1 - 1] = this.A[r2 - 1];
        this.A[r2 - 1] = temp;
    }

    public void rowScale(int i, double scalar) {
        for (int j = 0; j < this.N; j++) {
            this.A[i - 1][j] = this.A[i - 1][j] * scalar + 0.0;
        }
    }

    public void rowReplace(int r1, int r2, double scalar) {
        for (int j = 0; j < this.N; j++) {
            this.A[r1 - 1][j] = this.A[r1 - 1][j]
                                - (this.A[r2 - 1][j] * scalar) + 0.0;
        }
    }

    public Matrix rowEchelon() {
        Matrix a = this.getCopy();
        int i = 1;
        int j = 1;
        while (j <= a.N && i <= a.M) {
            int k = i;

            for (int p = k + 1; p <= M; p++) {
                if (Math.abs(a.getEntry(p, j)) > Math.abs(a.getEntry(k, j))) {
                    k = p;
                }
            }

             if (a.getEntry(k, j) == 0.0) {
                j++;
            } else {
                if (k != i) {
                    a.rowSwap(k, i);
                }
                for (int p = 1; p <= M; p++) {
                    if (p != i) {
                        double scalar = a.getEntry(p, j) / a.getEntry(i, j);
                        a.rowReplace(p, i, scalar);
                    }
                }
                double rowScalar = 1.0 / a.getEntry(i, j);
                a.rowScale(i, rowScalar);
                i++;
                j++;
            }
        }
        return a;
    }

    // static least-squares methods
    public static Matrix leastSquares(Matrix a, Matrix b)
        throws IllegalArgumentException {
        Matrix aTranspose = a.getTranspose();
        Matrix aTa = a.leftMultiply(aTranspose);
        Matrix aTb = b.leftMultiply(aTranspose);
        return aTa.augment(aTb).rowEchelon().getB();
    }

    public static Matrix leastSquares(Matrix m)
        throws IllegalArgumentException {
        Matrix aTranspose = m.getA().getTranspose();
        Matrix aTa = m.getA().leftMultiply(aTranspose);
        Matrix aTb = m.getB().leftMultiply(aTranspose);
        return aTa.augment(aTb).rowEchelon().getB();
    }

    // custom toString
    public String toString() {
        String s = "";
        s = s.concat(String.format("%d x %d\n", M, N));
        int i = 0;
        for (double[] row : A) {
            for (double value : row) {
                s = s.concat(value + " ");
            }
            if (i++ < M) {
                s = s.concat("\n");
            }
        }
        return s;
    }

    // test method
    public static void main(String[] args) {
        double[][] u = {{1, -2, 12.8},
                        {0, -2, 4.5},
                        {0, -2, 0}};
        Matrix c = new Matrix(u);
        System.out.println(c);
        System.out.println(c.rowEchelon());
        System.out.println(c.getA());
        System.out.println(c.getB());
        try {
            System.out.println(Matrix.leastSquares(c.getA(), c.getB()));
        } catch (IllegalArgumentException e) {
            System.out.println("yeet");
        }
        try {
            System.out.println(Matrix.leastSquares(c));
        } catch (IllegalArgumentException e) {
            System.out.println("yort");
        }
    }
}
