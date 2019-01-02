import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.sqrt;

class ScoringFunction{
    String INPUT_PATH = "/media/ahsan/8fe5b69a-2108-4aca-9442-606f63604637/Projects/Travelling Santa - Kaggle/data/"; //input path here
    ArrayList<Integer> plist;
    static boolean alreadyExists = false;
    double [] all_x;
    double [] all_y;
    protected ScoringFunction() throws IOException {
        plist = get_primes();

        FileReader fileReader = new FileReader(INPUT_PATH+"cities.csv");
        CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .withSkipLines(1)
                .build();

        List <String[]>cities = csvReader.readAll();

        all_x = new double[cities.size()];
        all_y = new double[cities.size()];

        for(int i=0; i < cities.size(); i+=1000){
            all_x[i] = Double.parseDouble(cities.get(i)[1]);
            all_y[i] = Double.parseDouble(cities.get(i)[2]);
        }
//        System.out.println(all_x[0]);
//        System.out.println(all_y[0]);
    }

    public static ScoringFunction getScoringFunction(){
        if(alreadyExists){
            return null;
        }
        else{
            alreadyExists = true;
            try {
                return new ScoringFunction();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }



    private boolean[] SieveOfEratosthenes(int n) {
        // Create a boolean array "prime[0..n]" and initialize
        // all entries it as true. A value in prime[i] will
        // finally be false if i is Not a prime, else true.
        boolean prime[] = new boolean[n + 1];
        prime[0] = false;
        prime[1] = false;
        int p = 2;
        while (p * p <= n) {
            //If prime[ p]is not changed, then it is a prime
            if (prime[p] == true) {
                //Update all multiples of p
                for (int i = p * 2; i <= n; i += p) {
                    prime[i] = false;
                }
            }
            p += 1;
        }
        return prime;
    }

    private ArrayList<Integer> get_primes() {
        int n = 200000;
        boolean [] prime = SieveOfEratosthenes(n);
        ArrayList <Integer> plist = new ArrayList<Integer>();
        for (int p=2; p < n; p++) {
            if (prime[p]) {
                plist.add(p);
            }
        }
        return plist;
    }


    public double get_score(int[] s) {
        double score = 0.0;
        for (int i=0; i < s.length - 1; i++) {
            double p1x = all_x[i];
            double p1y = all_y[i];
            double p2x = all_x[s[i + 1]];
            double p2y = all_y[s[i+1]];

            double stepSize = sqrt((p1x - p2x) * (p1x - p2x) + (p1y - p2y) * (p1x - p2y));
            if ((i + 1) % 10 == 0 && (!plist.contains(s[i]))) {
                stepSize *= 1.1;
                score += stepSize;
            }
        }
        return score;
    }

    public static void main(String[] args){
        System.out.println("hello");
        ScoringFunction scoringFunction = ScoringFunction.getScoringFunction();
        int order[] = {0,1,2};
        double s =scoringFunction.get_score(order);
        System.out.println(s);

    }

}