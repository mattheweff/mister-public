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
                A[i][j] = a[i][j];
            }
        }
    }

    public double getEntry(int m, int n) {
        return A[m - 1][n - 1];
    }

    public double[][] getEntries() {
        double[][] entries = new double[M][N];
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= N; j++) {
                entries[i - 1][j - 1] = this.getEntry(i, j);
            }
        }
        return entries;
    }

    public Matrix getCopy() {
        double[][] entries = this.getEntries();
        Matrix x = new Matrix(entries);
        return x;
    }

    //matrix operations
    public Matrix getTranspose() {
        double[][] values = new double[N][M];
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= N; j++) {
                values[j - 1][i - 1] = this.getEntry(i, j);
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
        Matrix b = new Matrix(values);
        return b;
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
        Matrix b = new Matrix(values);
        return b;
    }

    public void rowSwap(int r1, int r2) {
        double[] temp= new double[this.N];
        for (int j = 0; j < this.N; j++) {
            temp[j] = this.getEntry(r1, j + 1);
        }
        for (int j = 1; j <= this.N; j++) {
            this.setEntry(r1, j, this.getEntry(r2, j));
        }
        for (int j = 1; j <= this.N; j++) {
            this.setEntry(r2, j, temp[j-1]);
        }
    }

    public void rowScale(int i, double s) {
        for (int j = 1; j <= this.N; j++) {
            this.setEntry(i, j, s * this.getEntry(i, j));
        }
    }

    public void rowReplace(int r1, int r2, double scalar) {
        for (int j = 1; j <= N; j++) {
            double newEntry = this.getEntry(r1, j)
                              + (scalar * this.getEntry(r2, j));
            this.setEntry(r1, j, newEntry);
        }
    }

    //look up partial pivot algorithm
    public Matrix rowEchelon() {
        Matrix x = this.getCopy();
        int lastPivotRow = 0;
        for (int j = 1; j <= x.N; j++) {
            boolean hasPivot = false;
            int currentRow = lastPivotRow + 1;
            while (currentRow <= x.M) {
                if (x.getEntry(currentRow, j) != 0) {
                    hasPivot = true;
                    lastPivotRow++;
                    break;
                }
                currentRow++;
            } // checks for pivot
            if (hasPivot && x.getEntry(currentRow, j) != 1.0) {
                double scalar = 1.0 / x.getEntry(currentRow, j);
                x.rowScale(currentRow, scalar);
            } // scales
            if (hasPivot) {
                x.rowSwap(currentRow, lastPivotRow); //swap current row with lastPivotRow
                currentRow = lastPivotRow;
            } // swaps
            if (hasPivot) {
                for (int i = 1; i <= x.M; i++) {
                    if (i != currentRow) {
                        double scalar = -1.0 * x.getEntry(i, j);
                        x.rowReplace(i, currentRow, scalar);
                    }
                }
            }
        }
        return x;
    }

    public String toString() {
        String s = "";
        s = s.concat(String.format("%d x %d\n", M, N));
        for (double[] row : A) {
            for (double value : row) {
                s = s.concat(value + " ");
            }
            s = s.concat("\n");
        }
        return s;
    }

    // test method
    public static void main(String[] args) {
        double[][] u = {{0, 0, 0},
                        {0, -2, 4.5},
                        {0, -2, 0}};
        Matrix c = new Matrix(u);
        System.out.println(c);
        System.out.println(c.rowEchelon());
    }
}
