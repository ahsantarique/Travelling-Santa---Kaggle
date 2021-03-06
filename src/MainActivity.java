import java.util.Scanner;

public class MainActivity {



    public static void main(String[] args) {
        int target, op;
        Scanner scanner;

        scanner = new Scanner(System.in);

        System.out.print("Enter target number:");
        target = scanner.nextInt();
        System.out.println("Enter number of operators:");
        op = scanner.nextInt();

        int popSize = 5;
        GeneticAlgo geneticAlgo = new GeneticAlgo(target,op,popSize);
        showOutput(geneticAlgo);
    }

    private static void showOutput(GeneticAlgo geneticAlgo){
        String expression = geneticAlgo.geneticAlgorithm();
        double bestfitness = geneticAlgo.bestfitness;
        int numgen = geneticAlgo.numgen;

        System.out.println(expression);
        System.out.println(""+ bestfitness);
        System.out.println("" + numgen);
    }
}