import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class LinearRegression {
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        String header = null;
        File data = null;
        boolean fileFound = false;
        do {
            try {
                String fileName = promptInput(console, "Enter File Name: ");
                data = new File(fileName);
                Scanner h = new Scanner(data);
                header = h.nextLine();
                h.close();
                fileFound = true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (!fileFound);

        String xAxis = null;
        String yAxis = null;
        boolean validColumns = false;
        do {
            try {
                xAxis = promptInput(console, "Enter X Axis: ");
                yAxis = promptInput(console, "Enter Y Axis: ");
                validColumns = checkValidColumns(header, xAxis, yAxis);
            } catch (ColumnNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (!validColumns);

        console.close();

        GraphData graphData = null;
        try {
            graphData = new GraphData(data, xAxis, yAxis);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        Matrix xHat = Matrix.leastSquares(graphData.getData());
        System.out.printf("b = %.3f\n", xHat.getEntry(1, 1));
        System.out.printf("m = %.3f\n", xHat.getEntry(2, 1));
    }

    private static String promptInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }

    private static boolean checkValidColumns(String header, String col1,
        String col2) throws ColumnNotFoundException {
        ArrayList<String> columns = new ArrayList<>();
        Scanner headReader = new Scanner(header);
        while (headReader.hasNext()) {
            columns.add(headReader.next());
        }
        headReader.close();
        if (!columns.contains(col1)) {
            throw new ColumnNotFoundException("Column 1 Not Found.");
        } else if (!columns.contains(col2)) {
            throw new ColumnNotFoundException("Column 2 Not Found.");
        } else {
            return true;
        }
    }

}
