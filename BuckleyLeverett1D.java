
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class BuckleyLeverett1D {
    static double[] K1;
    static double[] K2;
    static double[] M;
    static double[] newS;
    static double[] oldS;
    static double[] newP;
    static double[] oldP;
    
    static double Mu1;
    static double Mu2;
    static double K0;
    
    static double ivS;          // initial value of S
    static double ivP;          // initial value of P
    
    static double lP;           // left border of P
    static double rP;           // right border of P
    static double lS;           // right border of S
    
    static double max;
    static double E;
    static double T;
    static double t;
    static double h;
    static double m;
    
    
        
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new File("output1.dat"));
        StringBuilder sb = new StringBuilder();
        String[] initialNumbers = br.readLine().split(" ");
        int n = Integer.parseInt(initialNumbers[0]);
        ivP = Double.parseDouble(initialNumbers[1]);
        lP = Double.parseDouble(initialNumbers[2]);
        rP = Double.parseDouble(initialNumbers[3]);
        ivS = Double.parseDouble(initialNumbers[4]);
        lS = Double.parseDouble(initialNumbers[5]);
        Mu1 = Double.parseDouble(initialNumbers[6]);
        Mu2 = Double.parseDouble(initialNumbers[7]);
        K0 = Double.parseDouble(initialNumbers[8]);
        t = T = Double.parseDouble(initialNumbers[9]);
        m = Double.parseDouble(initialNumbers[10]);
        E = Double.parseDouble(initialNumbers[11]);
        h = Math.pow((1.0 / (n - 1)), 2);
        K1 = new double[n];
        K2 = new double[n];
        M = new double[n];
        oldS = new double[n];
        newS = new double[n];
        oldP = new double[n];
        newP = new double[n];
        newP[0] = oldP[0] = lP;
        newP[oldP.length - 1] = oldP[oldP.length - 1] = rP;
        newS[0] = oldS[0] = lS;
        for (int i = 1; i < n - 1; i++) {
            oldP[i] = ivP;
            oldS[i] = ivS;
        }
        newS[newS.length - 1] = oldS[oldS.length - 1] = ivS;
        //100 0.3 0.5 0.1 0 1 0.09 0.3 0.001 0.01 0.2 0.00003
        int j = 1;
        while (T < 100) {
            pw.println("TITLE=\"USERData\"");
            pw.println("VARIABLES=i, P");
            pw.printf("ZONE T=\"ZONE%d\", i=%d f=Point\n", j++, n, n);
            for (int i = 0; i < K1.length; i++) {
                K1[i] = K0 * oldS[i] * oldS[i] / Mu1;
                K2[i] = K0 * Math.pow((1 - oldS[i]), 2) / Mu2;
                M[i] = K1[i] + K2[i];
            }
            boolean status;
            for (int i = 1; i < n - 1; i++) {
                status = true;
                while (status) {
                    double tempM1;
                    double tempM2;
                    for (int k = 1; k < newP.length - 1; k++) {
                        tempM1 = (M[k] + M[k - 1]) / 2.0;
                        tempM2 = (M[k] + M[k + 1]) / 2.0;
                        newP[k] = tempM1 * oldP[k - 1] + tempM2 * oldP[k + 1];
                        newP[k] /= tempM2 + tempM1;
                    }
                    max = Double.MIN_VALUE;
                    double temp;
                    for (int l = 1; l < newP.length - 1; l++) {
                        temp = Math.abs(oldP[l] - newP[l]);
                        if (max < temp)
                            max = temp;
                    }
                    if(max < E)
                        status = false;
                    copyArray(oldP, newP);
                }
                double temp = t / m;
                double tempK1 = (K1[i] + K1[i + 1]) / 2.0;
                double tempK2 = (K1[i] + K1[i - 1]) / 2.0;
                newS[i] = oldS[i] + temp * (tempK1 * (oldP[i + 1] - oldP[i]) / h - tempK2 * (oldP[i] - oldP[i - 1])  / h);
            }
            newS[n - 1] = newS[n - 2];
            copyArray(oldS, newS);
            for (int i = 0; i < newS.length; i++) {
                pw.println(i + " " + newS[i]);
            }
            T += t;
        }
        pw.close();
    }
    
    static void copyArray(double oldA[], double newA[]) {
        for (int i = 0; i < oldA.length; i++) {
            oldA[i] = newA[i];
        }
    }
}
