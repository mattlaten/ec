public class Individual implements Comparable<Individual>{
    private int dimension;

    private double[] candidate;
    private double fitness;

    private Function f;

    public Individual(Function f) throws FunctionException {
	this.f = f;
	dimension = f.dimension;
	candidate = new double[dimension];
	
	for (int i = 0; i < dimension; i++) {
	    double r = Math.random();	
	    double x = r*(f.upperBound - f.lowerBound) + f.lowerBound;
	    candidate[i] = x;
	}

	fitness = f.fitness(candidate);
    }
    
    public Individual(Function f, double[] candidate) throws FunctionException {
	this.f = f;
	checkConstraints(candidate);
	this.candidate = candidate;
	dimension = candidate.length;
	fitness = f.fitness(candidate);
    }

    public double[] getCandidate() {
	return candidate;
    }

    public double getFitness() {
	return fitness;
    }        

    public void mutate(double sigma) throws FunctionException {
	double[] m = new double[dimension];
	for (int i = 0; i < m.length - 1; i++) {
	    m[i] = EAMath.gaussian(0, sigma);
	}	
	for (int i = 0; i < m.length - 1; i++)
	    candidate[i] += m[i];
	checkConstraints(candidate);
	fitness = f.fitness(candidate);
    }
    
    
    private void checkConstraints(double[] m) {
	for (int i = 0; i < m.length - 1; i++) {
	    if (m[i] < f.lowerBound)
		m[i] = f.lowerBound;
	    else if (m[i] > f.upperBound)
		m[i] = f.upperBound;
	}
    }

    public Individual copy() throws FunctionException {
	double[] c = new double[dimension];
	for (int i = 0; i < dimension; i++)
	    c[i] = candidate[i];
	return new Individual(f, c);
    }

    public String toString() {
	String individual = "(";	
	for (int i = 0; i < candidate.length - 1; i++) {
	    individual += candidate[i] + ",";
	}
	individual += candidate[candidate.length - 1] + ") = " + fitness;
	return individual;
    }
    
    //Forces a sort in descending order
    public int compareTo(Individual o) {
	if (fitness < o.getFitness()) 
	    return -1;
	else if (fitness > o.getFitness())
	    return 1;
	return 0;
    }    
}
