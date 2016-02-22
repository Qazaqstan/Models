
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class LaplaceDirichlet2D {
        
    static double[][] newArray;
    static double[][] oldArray;
    
    static double max;
    static double E;
    static double h;
    
        
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new File("output.txt"));
        StringBuilder sb = new StringBuilder();
        String[] initialNumbers = br.readLine().split(" "); 
        int n = Integer.parseInt(initialNumbers[0]);
        double initialValue = Double.parseDouble(initialNumbers[1]);
        double border1 = Double.parseDouble(initialNumbers[2]);
        double border2 = Double.parseDouble(initialNumbers[3]);
        double border3 = Double.parseDouble(initialNumbers[4]);
        double border4 = Double.parseDouble(initialNumbers[5]);
        E = Double.parseDouble(initialNumbers[6]);
        h = 1.0 / (n - 1);
        newArray = new double[n][n];
        oldArray = new double[n][n];
        setBorderValues(border1, border2, border3, border4);
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++)
                oldArray[i][j] = initialValue;
        }
        int i = 0;
        while (true) {
            setBorderValues(border1, border2, border3, border4);
            compute();
            pw.println(++i);
            print(pw);
            if(compare())
                break;
            copy2DArray();
        }
        pw.close();
    }
    
    static void print(PrintWriter pw) {
        for (int i = 0; i < newArray.length; i++) {
            for (int j = 0; j < newArray.length; j++) {
                pw.print(newArray[i][j] + " ");
            }
            pw.println();
        }
        pw.println("---------------------------------------------");
    }
    
    static void setBorderValues (double b1, double b2, double b3, double b4) {
        int n = newArray.length;
        for (int i = 0; i < n; i++) {
            newArray[0][i] = oldArray[0][i] = b1;
            newArray[i][n - 1] = oldArray[i][n - 1] = b2;
            newArray[n - 1][i] = oldArray[n - 1][i] = b3;
            newArray[i][0] = oldArray[i][0] = b4;
        }
    }
    
    static void compute () {
        for (int i = 1; i < newArray.length - 1; i++) {
            for (int j = 1; j < newArray.length - 1; j++) {
                newArray[i][j] = oldArray[i + 1][j] + oldArray[i - 1][j] + oldArray[i][j + 1] + oldArray[i][j - 1];
                newArray[i][j] /= 4.0;
            }
        }
    }
    
    static boolean compare () {
        max = Double.MIN_VALUE;
        double temp;
        for (int i = 1; i < newArray.length - 1; i++) {
            for (int j = 1; j < newArray.length - 1; j++) {
                temp = Math.abs(oldArray[i][j] - newArray[i][j]);
                if (max < temp)
                    max = temp;
            }
        }
        return max < E;
    }
    
    static void copy2DArray() {
        for (int i = 1; i < newArray.length - 1; i++)
            for (int j = 1; j < newArray.length - 1; j++)
                oldArray[i][j] = newArray[i][j];
    }
}