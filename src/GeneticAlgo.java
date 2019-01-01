import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ahsan on 12/16/2016.
 */

public class GeneticAlgo {
    int target, op;
    int popSize;
    ArrayList<String> p;
    double bestfitness;
    int numgen;

    GeneticAlgo(int target, int op, int popSize){
        this.target = target;
        this.op = op;
        this.popSize = popSize+(popSize%2);
    }

    int randInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    String randomString(){
        String s = ""+randInt(1,9);
        for(int i=0; i < op; i++){
            int randop = randInt(1,3);
            switch (randop){
                case 1: s = s + '+';
                    break;
                case 2: s = s + '-';
                    break;
                case 3: s = s + '*';
            }
            s = s+randInt(1,9);
        }
        return s;
    }

    String geneticAlgorithm(){
        p = new ArrayList<String>();

        for(int i=0; i <popSize; i++){
            p.add(randomString());
        }

        bestfitness = Double.NEGATIVE_INFINITY;
        String best= "";

        int timeLimit = (int) 1e7;

        numgen = 1;
        do{
            numgen++;
            for( String s: p){
//                System.out.println(s);
                double fitness = assessFitness(s);
                if(best.equals("") || fitness > bestfitness){
                    bestfitness = fitness;
                    best = s;
                }
            }
//	          try {
//	          System.in.read();
//	      } catch (IOException e) {
//	          e.printStackTrace();
//	      }
            ArrayList<String> q = new ArrayList<String>();
            for(int i=0; i < popSize/2; i++){
                String pa = selectWithReplacement();
                String pb = selectWithReplacement();

                Children c= crossOver(pa,pb);

                q.add(mutate(c.a));
                q.add(mutate(c.b));
            }
            p=q;
        }while(timeLimit-- > 0 && bestfitness != 0);

        System.out.println(best);
        return best;
    }

    double assessFitness(String s){
        double value = ExpressionEvaluation.eval(s);
        double fitness = -Math.abs(target-value);
        return fitness;
    }


    String selectWithReplacement(){
        //tournament selection
        //p=population
        int t = 5; //tournament size
        String best = pickRandom();

        for(int i=2; i<=t; i++){
            String next = pickRandom();
            if(assessFitness(next) > assessFitness(best)){
                best=next;
            }
        }
        return best;
    }

    String pickRandom(){
        int index = randInt(0,popSize-1);
        String best = p.get(index);

        /* replacement
        int replace = randInt(0,best.length()-1);
        if(replace%2==0){
            best = best.substring(0,replace-1)+randInt(0,9)+best.substring(replace+1);
        }
        else{
            int opval = randInt(1,3);
            char operator='-';
            switch (opval){
                case 1: operator = '+';
                    break;
                case 2: operator = '-';
                    break;
                case 3: operator = '*';
            }
            best=best.substring(0,replace-1)+operator+best.substring(replace+1);
        }
        */
        return best;
    }


    Children crossOver(String pa, String pb){
        double p = Math.random();
        if(p <= 0.5) return onePointCrossOver(pa,pb);
        else return twoPointCrossOver(pa,pb);
    }

    Children onePointCrossOver(String pa, String pb){
        int c = randInt(0,pa.length()-1);
        Children children = new Children();
        children.a = pa.substring(0,c+1)+pb.substring(c+1);
        children.b = pb.substring(0,c+1)+pa.substring(c+1);
//        System.out.println(pa);
//        System.out.println(children.b);

//        try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return children;
    }
    Children twoPointCrossOver(String pa, String pb){
        int c,d;
        do{
            c = randInt(0,pa.length()-1);
            d = randInt(1,pa.length()-1);
        }while(c>=d);
        Children children = new Children();

        children.a = pa.substring(0,c+1)+pb.substring(c+1,d+1)+pa.substring(d+1);
        children.b = pb.substring(0,c+1)+pa.substring(c+1,d+1)+pb.substring(d+1);

        return children;
    }

    String mutate(String s){
        // random walk mutation

//    	System.out.println("before mutation " + s);

        double b = Math.random();
        double p = 1.0/s.length();

        for(int i=0; i < s.length(); i++){
            if(p >= Math.random()){
                if(i%2==0){ //digit
                    do{
                        int n=1-randInt(0,1)*2;
                        n = n*2; // n = -2 or +2
                        if(i+n >=0 && i+n < s.length()){
                            s = s.substring(0,i)+s.charAt(i+n)+s.substring(i+1);
                        }
                        else if(i-n >=0 && i-n < s.length()){
                            s = s.substring(0,i)+s.charAt(i-n)+s.substring(i+1);
                        }
//                        System.out.println("mutation in digit!");
                        if(b > 0.9) s = s.substring(0,i)+randInt(1,9)+s.substring(i+1);
                    }while(b>= Math.random());
                }
                else{ //operator
                    int opval = randInt(1,3);
                    char operator='-';
                    switch (opval){
                        case 1: operator = '+';
                            break;
                        case 2: operator = '-';
                            break;
                        case 3: operator = '*';
                    }
                    s=s.substring(0,i)+operator+s.substring(i+1);
                }
            }
        }
//    	System.out.println("after mutation " + s);
        return s;
    }

}