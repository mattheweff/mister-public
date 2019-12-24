import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GraphData {
    private String xLabel;
    private String yLabel;
    private Matrix data;

    public GraphData(File f, String x, String y) throws FileNotFoundException {
        xLabel = x;
        yLabel = y;
        Scanner input = new Scanner(f);
        String header = input.nextLine();
        int columnCount = getColumnCount(header);
        int xIndex = getIndexOf(header, x);
        int yIndex = getIndexOf(header, y);
        data = new Matrix(parseData(input, columnCount, xIndex, yIndex));
    }

    public GraphData() {
        xLabel = "";
        yLabel = "";
        data = new Matrix();
    }

    public String getXLabel() {
        return xLabel;
    }

    public String getYLabel() {
        return yLabel;
    }

    public Matrix getData() {
        return data;
    }

    private static int getColumnCount(String h) {
        Scanner reader = new Scanner(h);
        int count = 0;
        while (reader.hasNext()) {
            count++;
            reader.next();
        }
        reader.close();
        return count;
    }

    private static int getIndexOf(String h, String c) {
        Scanner reader = new Scanner(h);
        int indexOf = 0;
        while (reader.hasNext()) {
            if (reader.next().equals(c)) {
                reader.close();
                return indexOf;
            }
            indexOf++;
        }
        reader.close();
        return -1;
    }

    private static double[][] parseData(Scanner data, int columns,
                                        int xCol, int yCol) {
        ArrayList<double[]> dataList = new ArrayList<>();
        double[] rowData = new double[3];
        rowData[0] = 1.0;
        int count = 0;
        while (data.hasNext()) {
            if (count % columns == xCol) {
                rowData[1] = Double.parseDouble(data.next());
            } else if (count % columns == yCol) {
                rowData[2] = Double.parseDouble(data.next());
            } else {
                data.next();
            }
            if (count != 0 && count % columns == 0) {
                double[] thisRow = new double[rowData.length];
                for (int i = 0; i < rowData.length; i++) {
                    thisRow[i] = rowData[i];
                }
                dataList.add(thisRow);
            }
            if (data.hasNext()) {
                count++;
            }
        }
        double[][] dataArray = new double[dataList.size()][3];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);
        }
        return dataArray;
    }

    public static void main(String[] args) {
        File file = new File("diabetes.tab.txt");
        try {
            GraphData gd = new GraphData(file, "BMI", "Y");
            System.out.println(gd.getData());
            System.out.println(gd.getXLabel());
            System.out.println(gd.getYLabel());
        } catch (FileNotFoundException e) {
            System.out.println("whips");
        }
    }

}
