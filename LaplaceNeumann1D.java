
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class LaplaceNeumann1D {
    
    static double[] newArray;
    static double[] oldArray;
    static double[] exactArray;
    
    static double max;
    static double E;
    static double h;
    
        
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        StringBuilder sb = new StringBuilder();
        String[] initialNumbers = br.readLine().split(" "); 
        int n = Integer.parseInt(initialNumbers[0]);
        double initialValue = Double.parseDouble(initialNumbers[1]);
        double border1 = Double.parseDouble(initialNumbers[2]);
        E = Double.parseDouble(initialNumbers[3]);
        h = 1.0 / (n - 1);
        newArray = new double[n];
        oldArray = new double[n]; 
        exactArray = new double[n]; 
        exactArray[0] = oldArray[0] = border1;
        for (int i = 1; i < n; i++) {
            exactArray[i] = oldArray[i] = initialValue;
        }
        int i = 0;
        while (true) {
            newArray[0] = border1;
            compute();
            pw.println((++i) + " " + Arrays.toString(newArray));
            if(compare())
                break;
            System.arraycopy(newArray, 0, oldArray, 0, newArray.length);
        }
        computeExactValue();
        pw.println(Arrays.toString(newArray));
        pw.println(Arrays.toString(exactArray));
        pw.close();
    }
    
    static void compute () {
        for (int i = 1; i < newArray.length - 1; i++) {
            newArray[i] = oldArray[i + 1] + oldArray[i - 1];
            newArray[i] /= 2.0;
        }
        newArray[newArray.length - 1] = newArray[newArray.length - 2]; 
    }
    
    static boolean compare () {
        max = Double.MIN_VALUE;
        double temp;
        for (int i = 0; i < newArray.length; i++) {
            temp = Math.abs(oldArray[i] - newArray[i]);
            if (max < temp)
                max = temp;
        }
        return max < E;
    }
    
    static void computeExactValue () {
        for (int i = 1; i < exactArray.length; i++)
            exactArray[i] = -0.5 * i * h + 1;
    }
}
