import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class LinearRegression {
    public static void main(String[] args) throws FileNotFoundException {

        //read file and create Matrix objects
        File data = new File(args[0]);
        Scanner input = new Scanner(data);
        ArrayList<double[]> dataList = new ArrayList<>();
        String header = input.nextLine();
        int numColumns = countColumns(header);
        int xAxis = getArgIndex(header, args[1]);
        int yAxis = getArgIndex(header, args[2]);
        while (input.hasNextLine()) {
            String thisLine = input.nextLine();
            double[] lineData = getRowData(thisLine, xAxis, yAxis, numColumns);
            dataList.add(lineData);
        }
        double[][] dataArray = new double[dataList.size()][2];
        double[][] bArray = new double[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i][0] = 1.0;
            dataArray[i][1] = dataList.get(i)[0];
            bArray[i][0] = dataList.get(i)[1];
        }
        Matrix a = new Matrix(dataArray);
        Matrix b = new Matrix(bArray);

        //Matrix Operations
        Matrix aTranspose = a.getTranspose();
        Matrix aTa = a.leftMultiply(aTranspose);
        Matrix aTb = b.leftMultiply(aTranspose);
        System.out.println(aTa);
        System.out.println(aTb);

        //create augmented matrix A(t)A = A(t)b
        double[][] u = {{aTa.getEntry(1,1), aTa.getEntry(1,2),
                         aTb.getEntry(1,1)},
                        {aTa.getEntry(2,1), aTa.getEntry(2,2),
                         aTb.getEntry(2,1)}};
        Matrix aTab = new Matrix(u);
        System.out.println(aTab);

        //solve system
        aTab = aTab.rowEchelon();
        System.out.println(aTab);
        System.out.printf("b = %.3f\nm = %.3f", aTab.getEntry(1, 3),
                          aTab.getEntry(2, 3));
    }

    private static int countColumns(String header) {
        Scanner line = new Scanner(header);
        int count = 0;
        while (line.hasNext()) {
            count++;
            line.next();
        }
        return count;
    }

    private static int getArgIndex(String header, String arg) {
        Scanner line = new Scanner(header);
        int argIndex = 1;
        String colName;
        while (line.hasNext()) {
            colName = line.next();
            if (colName.equals(arg)) {
                return argIndex;
            }
            argIndex++;
        }
        return argIndex;
    }

    private static double[] getRowData(String row, int col1, int col2,
                                       int numColumns) {
        Scanner line = new Scanner(row);
        double[] lineData = new double[2];
        double entry;
        for (int i = 1; i <= numColumns; i++) {
            entry = Double.parseDouble(line.next());
            if (i == col1) {
                lineData[0] = entry;
            }
            if (i == col2) {
                lineData[1] = entry;
            }
        }
        return lineData;
    }
}
