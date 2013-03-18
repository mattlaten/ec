import java.util.Arrays;
import java.io.PrintWriter;
import java.io.File;
import java.lang.reflect.Constructor;

public class Evolve {
    public final static int UNIFORM_VARIANCE = 0;
    public final static int NONUNIFORM_VARIANCE_LINEAR = 1;
    public final static int NONUNIFORM_VARIANCE_NONLINEAR = 2;
    public final static int NONUNIFORM_VARIANCE_NONLINEAR_SPECIAL = 3;

    private int generation;
    private int maxGen;
    private int popsize = 100;
    private Function f; 
    private Selection s;
    private Individual[] population;
    private Individual[] elite = null;
    private int variance;
    private double sigma = 5;

    private double pm = 0.8; // probability of mutation ;
    private double pc = 0.5; // probability of crossover ;
    private double percentElite = 0.05;
    private int indexElite;
    
    private int experiments;
    private double[] average;
    private double[] bestFitness;
    private double[] averageOfAverage;

    private PrintWriter out1;
    private PrintWriter out2;	

    public void evolve(Function f, 
		       int popsize,
		       double pm,
		       double pc,
		       int maxGen, 
		       Selection s,
		       int experiments, 
		       String file) {
	this.f = f;
	this.popsize = popsize;
	this.pm = pm;
	this.pc = pc;
	this.maxGen = maxGen;
	this.s = s;
	this.experiments = experiments;
	
	average = new double[maxGen];
	bestFitness = new double[experiments];
	averageOfAverage = new double[maxGen];
	for (int i = 0; i < maxGen; i++) {
	    average[i] = 0;
	    averageOfAverage[i] = 0;
	}
	
	for (int i = 0; i < experiments; i++) {
	    System.out.println("Executing run: " + i);
	    generation = 0;
	    initialize();
	    s.initialize(popsize);
	    indexElite = (int) (percentElite * popsize) - 1;
	    
	    while (generation < maxGen) {
		Arrays.sort(population);
		getElite();
		
		population = s.select(population);
		
		mutate();
		
		crossover();
		
		Arrays.sort(population);
		putElite();

		average[generation] += population[0].getFitness();
		double tmp = 0;
		for (int j = 0; j < popsize; j++) {
		    tmp += population[j].getFitness();
		}	       
		averageOfAverage[generation] += tmp/popsize;
		
		generation++;
	    }
	    bestFitness[i] = population[0].getFitness();
	}	
	
	for (int i = 0; i < maxGen; i++) {
	    average[i] /= experiments;
	    averageOfAverage[i] /= experiments;
	}

	try {
	    out1 = new PrintWriter(new File(file + ".averageBest"));
	    if (experiments != 1) 
		out2 = new PrintWriter(new File(file + ".averageBestFinal"));
	} catch(Exception e) {
	    e.printStackTrace();
	}
	
	out1.println("# Average best fitness over " + experiments + " runs.");
	for (int i = 1; i <= maxGen; i++)
	    out1.println(i + " " + average[i-1]);
	out1.flush();
	
	if (experiments != 1) {	    	
	    double sd = 0;
	    for (int i = 0; i < experiments; i++) {
		sd += Math.pow(average[maxGen - 1] - bestFitness[i], 2)*(1.0/(experiments-1));
	    }
	    sd = Math.sqrt(sd);
	    out2.println("#Average best fitness in last generation: " + average[maxGen - 1]);
	    out2.println("#Standard deviation: +-" + sd);
	    out2.flush();
	}
    }
    
    private void initialize() {
	try {
	    population = new Individual[popsize];	    
	    for (int i = 0; i < popsize; i++) {
		population[i] = new Individual(f);
	    }	
	} catch (FunctionException e) {
	    e.printStackTrace();
	}
    }
    
    //Assumes the population is already sorted in ascending order
    public void getElite() {
	try {
	    if (indexElite != -1) {
		elite = new Individual[indexElite + 1];
		for (int i = 0; i < elite.length; i++)
		    elite[i] = population[i].copy();
	    } else {
		elite = null;
	    }
	} catch (FunctionException e) {
	    e.printStackTrace();
	}
    }

    //Assumes the population is already sorted in ascending order
    public void putElite() {
	try {
	    if (elite != null) {
		int index = 0;
		for (int i = 0; i < elite.length; i++) {				
		    if (population[i].compareTo(elite[index]) == 1) {
			population[i] = elite[index].copy();
			index++;
		    }		    
		}		
	    }
	    elite = null;
	} catch (FunctionException e) {
	    e.printStackTrace();
	}	
    }

    public void mutate() {
	try {
	    for (int i = 0; i < popsize; i++) {
		double p = Math.random();
		if (p <= pm)
		    population[i].mutate(sigma);
	    }
	} catch(Exception e) {
	    e.printStackTrace();
	}	
    }

    public void crossover() {
	try {
	    Individual[] newPopulation = new Individual[popsize];
	    double r,w;
	    
	    for (int i = 0; i < popsize; i++) {
		r = Math.random();		
		if (r > pc) {
		    newPopulation[i] = population[i].copy();
		} else {
		    int iParent1 = (int) (Math.random()*popsize);
		    int iParent2 = (int) (Math.random()*popsize);
		    double[] cParent1 = population[iParent1].getCandidate();
		    double[] cParent2 = population[iParent2].getCandidate();

		    w = Math.random();
		    double[] offspring = new double[f.getDimension()];
		    for (int j = 0; j<offspring.length; j++) {
			offspring[j] = w*cParent1[j] + (1 - w)*cParent2[j];			
		    }
		    newPopulation[i] = new Individual(f, offspring);
		}				
	    }
	    population = newPopulation;
	} catch(Exception e) {
	    e.printStackTrace();
	}
	    
    }
    
    public String toString() {
	String result = "Fitness function: " + f.getName() + ", Popsize: " + popsize + ", Generation: " + generation + "\n\n";
	for (int i = 0; i < popsize; i++) {
	    result += population[i].toString() + "\n";
	}
	return result;
    }

    public static void main(String args[]) {
	if (args.length != 7) {
	    System.out.println("Usage: java Evolve <Fitness Landscape> <Population size>");
	    System.out.println("                   <Mutation Probability> <Cross-Over Probability>");
	    System.out.println("                   <#Generations> <Selection> <#Experiments>");
	    System.exit(0);
	}
	Function f = null;
	Selection s = null;
	
	try {
	    Class c = Class.forName(args[0]);
	    Constructor con = c.getConstructor();
	    f = (Function) con.newInstance();
	    c = Class.forName(args[5]);
	    con = c.getConstructor();
	    s = (Selection) con.newInstance();	   
	} catch(Exception e) {
	    e.printStackTrace();
	    System.exit(0);
	}
	
	int popsize = Integer.parseInt(args[1]);
	double pm = Double.parseDouble(args[2]);
	double pc = Double.parseDouble(args[3]);	
	int generations = Integer.parseInt(args[4]);
	int experiments = Integer.parseInt(args[6]);

	Evolve e = new Evolve();
	String filePrefix = args[0] + "_" + args[5]  + "_" + popsize + "_" + pm + "_" + pc;
	e.evolve(f, popsize, pm, pc, generations, s, experiments, filePrefix);
    }
}
