import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Laplace3DOverRelaxationMethod {
    static double[][][] newArray;
    static double[][][] oldArray;
    
    static double max;
    static double E;
    static double t;
    
        
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//      10 0.5 0.5 0.5 0.5 0.5 1 0.5 0.05 1.5
        PrintWriter pw = new PrintWriter(new File("output.dat"));
        StringBuilder sb = new StringBuilder();
        String[] initialNumbers = br.readLine().split(" "); 
        int n = Integer.parseInt(initialNumbers[0]);                    // 10
        double initialValue = Double.parseDouble(initialNumbers[1]);    // 0.5 
        double b1 = Double.parseDouble(initialNumbers[2]);              // 0.5
        double b2 = Double.parseDouble(initialNumbers[3]);              // 0.5
        double b3 = Double.parseDouble(initialNumbers[4]);              // 0.5
        double b4 = Double.parseDouble(initialNumbers[5]);              // 0.5
        double b5 = Double.parseDouble(initialNumbers[6]);              // 0.5
        double b6 = Double.parseDouble(initialNumbers[7]);              // 1
        E = Double.parseDouble(initialNumbers[8]);                      // 0.0003
        t = Double.parseDouble(initialNumbers[9]);                      // 1.5
        newArray = new double[n][n][n];
        oldArray = new double[n][n][n];
        setBorderValues(b1, b2, b3, b4, b5, b6);
        for (int i = 1; i < n - 1; i++)
            for (int j = 1; j < n - 1; j++)
                for (int k = 1; k < n - 1; k++)
                    oldArray[i][j][k] = initialValue;
        pw.println("TITLE=\"USERData\"");
        pw.println("VARIABLES=i, j, k, U");
        pw.printf("ZONE T=\"ZONE1\", i=%d j=%d k=%d f=Point\n", n, n, n);
        setBorderValues(b1, b2, b3, b4, b5, b6);
        while (true) {
            compute();
            if(compare())
                break;
            copy3DArray();
        }
        copy3DArray();
        print(pw);
        pw.close();
    }
    
    static void print(PrintWriter pw) {
        for (int i = 0; i < newArray.length; i++) {
            for (int j = 0; j < newArray.length; j++) {
                for (int k = 0; k < newArray.length; k++)
                    pw.println(i + " " + j + " " + k + " " + newArray[i][j][k]);
            }
        }
    }
    
    static void setBorderValues (double b1, double b2, double b3, double b4, double b5, double b6) {
        int n = newArray.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newArray[i][0][j] = oldArray[i][0][j] = b2;
                newArray[i][n - 1][j] = oldArray[i][n - 1][j] = b4;
                
                newArray[i][j][0] = oldArray[i][j][0] = b6;
                
                newArray[0][i][j] = oldArray[0][i][j] = b1;
                newArray[n - 1][i][j] = oldArray[n - 1][i][j] = b3;
            }
        }
       for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newArray[i][j][n - 1] = oldArray[i][j][n - 1] = b5;
            }
        }
    }
    
    static void compute () {
        double temp = 6.0 - 6.0 / t;
        double temp2 = t / 6.0;
        for (int i = 1; i < newArray.length - 1; i++) {
            for (int j = 1; j < newArray.length - 1; j++) {
                for (int k = 1; k < newArray.length - 1; k++) {
                    newArray[i][j][k] = newArray[i - 1][j][k] + newArray[i][j - 1][k] + newArray[i][j][k - 1] + oldArray[i + 1][j][k] + oldArray[i][j + 1][k] + oldArray[i][j][k + 1] - temp * oldArray[i][j][k];
                    newArray[i][j][k] *= temp2;
                }
            }
        }
    }
    
    static boolean compare () {
        max = Double.MIN_VALUE;
        double temp;
        for (int i = 1; i < newArray.length - 1; i++) {
            for (int j = 1; j < newArray.length - 1; j++) {
                for (int k = 1; k < newArray.length - 1; k++) {
                    temp = Math.abs(oldArray[i][j][k] - newArray[i][j][k]);
                    if (max < temp)
                        max = temp;
                }
            }
        }
        return max < E;
    }
    
    static void copy3DArray() {
        for (int i = 1; i < newArray.length - 1; i++)
            for (int j = 1; j < newArray.length - 1; j++)
                for (int k = 1; k < newArray.length - 1; k++)
                    oldArray[i][j][k] = newArray[i][j][k];
    }
}
