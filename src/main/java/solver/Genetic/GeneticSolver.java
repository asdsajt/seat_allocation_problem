package solver.Genetic;

import java.util.*;

//Main class
public class GeneticSolver {

    Population population;
    Individual fittest;
    Individual secondFittest;
    int generationCount = 0;

    public int[] run(int[] room, int rowNumber, int colNumber, Map orders, int orderSum){
        Random rn = new Random();

        population = new Population(room, rowNumber, colNumber, orders);

        //Initialize population
        population.initializePopulation(10);

        //Calculate fitness of each individual
        population.calculateFitness();

        //While population gets an individual with maximum fitness
        while (((population.fittest < orderSum) && (generationCount < 5000000))) {
            ++generationCount;

            selection();
            crossover();

            if (rn.nextInt()%7 < 5) mutation();

            addFittestOffspring();

            //Calculate new fitness value
            population.calculateFitness();
        }

        System.out.println("\nSolution found in generation " + generationCount);
        System.out.println("Fitness: "+population.getFittest().fitness);
        System.out.print("Genes: ");
        for (int i = 0; i < colNumber * rowNumber; i++) {
            System.out.print(population.getFittest().genes[i]);
        }

        if(population.getFittest().fitness < orderSum){
            return null;
        }
        else {
            return population.getFittest().genes;
        }
    }

    //Selection
    void selection() {

        //Select the most fittest individual
        fittest = population.getFittest();

        //Select the second most fittest individual
        secondFittest = population.getSecondFittest();
    }

    //Crossover
    void crossover() {
        Random rn = new Random();

        //Select a random crossover point
        int crossOverPoint = rn.nextInt(population.individuals[0].geneLength);

        //Swap values among parents
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = fittest.genes[i];
            fittest.genes[i] = secondFittest.genes[i];
            secondFittest.genes[i] = temp;

        }

    }

    //Mutation
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        //Flip values at the mutation point
        if (fittest.genes[mutationPoint] == 0) {
            fittest.genes[mutationPoint] = 1;
        } else {
            fittest.genes[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        if (secondFittest.genes[mutationPoint] == 0) {
            secondFittest.genes[mutationPoint] = 1;
        } else {
            secondFittest.genes[mutationPoint] = 0;
        }
    }

    //Get fittest offspring
    Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }

    //Replace least fittest individual from most fittest offspring
    void addFittestOffspring() {

        //Update fitness values of offspring
        fittest.calcFitness();
        secondFittest.calcFitness();

        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();

        //Replace least fittest individual from most fittest offspring
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }
}

//Individual class
class Individual {

    int fitness = 0;
    int[] genes;
    int[] room;
    Map orders=new HashMap();
    Map currentOrders=new HashMap();
    int orderNumber = 0;
    int rowNum = 0;
    int geneLength = 0;

    public Individual(int[] theaterRoom, int rowNum, int colNum, Map orders) {
        Random rn = new Random();
        room = theaterRoom;
        orderNumber = colNum;
        this.rowNum = rowNum;
        geneLength = colNum * rowNum;
        genes = new int[geneLength];

        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2);
        }

        this.orders = orders;

        fitness = 0;
    }

    public static<K> void increment(Map<K, Integer> map, K key)
    {
        map.putIfAbsent(key, 0);
        map.put(key, map.get(key) + 1);
    }

    //Calculate fitness
    public void calcFitness() {

        fitness = 0;
        int oneCount = 0;
        ArrayList<Integer> startI = new ArrayList<Integer>();
        ArrayList<Integer> startJ = new ArrayList<Integer>();
        ArrayList<Integer> orderLength = new ArrayList<Integer>();

        for (int i = 0; i < geneLength; i++) {
            if (genes[i] == 1 && room[i] == 1) {
                genes[i] = 0;
            }
        }

        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

        for (int i = 0; i < rowNum; i++){
            matrix.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < rowNum; i++){
            for (int j = 0; j < orderNumber; j++){
                matrix.get(i).add(genes[i * orderNumber + j]);
            }
        }

        for (int i = 0; i < rowNum; i++){
            boolean first = true;
            for (int j = 0; j < orderNumber; j++){
                if (matrix.get(i).get(j) == 1){
                    if (first){
                        startI.add(i);
                        startJ.add(j);
                        first = false;
                    }
                    oneCount++;
                }
                else if(matrix.get(i).get(j) == 0 && oneCount > 0){
                    orderLength.add(oneCount);
                    increment(currentOrders, oneCount);
                    oneCount = 0;
                    first = true;
                }
            }
            if(oneCount > 0){
                orderLength.add(oneCount);
                increment(currentOrders, oneCount);
                oneCount = 0;
                first = true;
            }
        }

        Set currentKeys = currentOrders.keySet();
        Iterator it = orders.keySet().iterator();

        while(it.hasNext())
        {
            int key=(int)it.next();
            if(currentKeys.contains(key)){
                if ((int)currentOrders.get(key) == (int)orders.get(key) || (int)currentOrders.get(key) > (int)orders.get(key)){
                    fitness += (int)orders.get(key);
                }
                else {
                    fitness += (int)currentOrders.get(key);
                }
            }
        }

        currentOrders.clear();
    }
}

//Population class
class Population {

    int popSize = 10;
    Individual[] individuals = new Individual[10];
    int fittest = 0;
    int[] room;
    int rowNumber;
    int colNumber;
    Map orders;

    public Population(int[] theaterRoom, int rowNum, int colNum, Map orders){
        room = theaterRoom;
        rowNumber = rowNum;
        colNumber = colNum;
        this.orders = orders;
    }

    //Initialize population
    public void initializePopulation(int size) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual(room, rowNumber, colNumber, orders);
        }
    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].fitness;
        return individuals[maxFitIndex];
    }

    //Get the second most fittest individual
    public Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].fitness > individuals[maxFit1].fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (individuals[i].fitness > individuals[maxFit2].fitness) {
                maxFit2 = i;
            }
        }
        return individuals[maxFit2];
    }

    //Get index of least fittest individual
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].fitness) {
                minFitVal = individuals[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate fitness of each individual
    public void calculateFitness() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
        }
        getFittest();
    }

}