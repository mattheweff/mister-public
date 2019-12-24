public class Matrix {
    public final int M, N;
    private final double[][] A;

    //constructors
    public Matrix() {
        this(2, 2);
        double[][] a = {{1, 0},
                        {0, 1}};
        this.setEntries(a);
    }

    public Matrix(int M, int N) {
        this(new double[M][N]);
    }

    public Matrix(double[][] A) {
        this.M = A.length;
        this.N = A[0].length;
        this.A = A;
    }

    //getters and setters
    public void setEntry(int i, int j, double entry) {
        this.A[i - 1][j - 1] = entry;
    }

    public void setEntries(double[][] a) {
        if (a.length != M || a[0].length != N) {
            System.out.println("Invalid Dimension");
            return;
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                this.A[i][j] = a[i][j];
            }
        }
    }

    public double getEntry(int m, int n) {
        return this.A[m - 1][n - 1];
    }

    public double[][] getEntries() {
        double[][] entries = new double[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                entries[i][j] = this.A[i][j];
            }
        }
        return entries;
    }

    public Matrix getA() {
        double[][] entries = new double[M][N - 1];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < (N - 1); j++) {
                entries[i][j] = this.A[i][j];
            }
        }
        return new Matrix(entries);
    }

    public Matrix getB() {
        double[][] entries = new double[M][1];
        for (int i = 0; i < M; i++) {
            entries[i][0] = this.A[i][N - 1];
        }
        return new Matrix(entries);
    }

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
