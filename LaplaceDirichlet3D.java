
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class LaplaceDirichlet3D {
    static double[][][] newArray;
    static double[][][] oldArray;
    
    static double max;
    static double E;
    
        
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new File("output.dat"));
        StringBuilder sb = new StringBuilder();
        String[] initialNumbers = br.readLine().split(" "); 
        int n = Integer.parseInt(initialNumbers[0]);
        double initialValue = Double.parseDouble(initialNumbers[1]);
        double border1 = Double.parseDouble(initialNumbers[2]);
        double border2 = Double.parseDouble(initialNumbers[3]);
        double border3 = Double.parseDouble(initialNumbers[4]);
        double border4 = Double.parseDouble(initialNumbers[5]);
        double border5 = Double.parseDouble(initialNumbers[6]);
        double border6 = Double.parseDouble(initialNumbers[7]);
        E = Double.parseDouble(initialNumbers[8]);
        newArray = new double[n][n][n];
        oldArray = new double[n][n][n];
        pw.println("TITLE=\"USERData\"");
        pw.println("VARIABLES=i, j, k, U");
        pw.printf("ZONE T=\"ZONE1\", i=%d j=%d k=%d f=Point\n", n / 2, n, n);
        setBorderValues(border1, border2, border3, border4, border5, border6);
        for (int i = 1; i < n - 1; i++)
            for (int j = 1; j < n - 1; j++)
                for (int k = 1; k < n - 1; k++)
                    oldArray[i][j][k] = initialValue;
        int i = 0;
        while (true) {
            setBorderValues(border1, border2, border3, border4, border5, border6);
            compute();
            if(compare())
                break;
            copy3DArray();
        }
        print(pw);
        pw.close();
    }
    
    static void print(PrintWriter pw) {
        for (int i = 0; i < newArray.length; i++) {
            for (int j = 0; j < newArray.length; j++) {
                for (int k = newArray.length / 2; k < newArray.length; k++)
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
                newArray[i][j][n - 1] = oldArray[i][j][n - 1] = b5;

                newArray[0][i][j] = oldArray[0][i][j] = b1;
                newArray[n - 1][i][j] = oldArray[n - 1][i][j] = b3;
            }
        }
    }
    
    static void compute () {
        for (int i = 1; i < newArray.length - 1; i++) {
            for (int j = 1; j < newArray.length - 1; j++) {
                for (int k = 1; k < newArray.length - 1; k++) {
                    newArray[i][j][k] = oldArray[i + 1][j][k] + oldArray[i - 1][j][k] + oldArray[i][j + 1][k] + oldArray[i][j - 1][k] + oldArray[i][j][k + 1] + oldArray[i][j][k - 1];
                    newArray[i][j][k] /= 6.0;
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
