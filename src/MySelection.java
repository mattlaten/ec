import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MySelection extends Selection {
	int popsize;
	
	//tourney selections
	
	int k = 11;
	double percentParents = 0.9;

	
	public void initialize(int popsize) {
		this.popsize = popsize;
	}

	public Individual[] select(Individual[] population) {

		int numParents = (int) Math.floor(percentParents*popsize);
		
		Individual[] newPopulation = new Individual[popsize];

		ArrayList<Individual> popList = new ArrayList<Individual>(Arrays.asList(population));
		ArrayList<Individual> newPopList = new ArrayList<Individual>(popsize);
		
		for (int i = 0; i < numParents; i++){
			//create set, choose winner, add to new pop
			if (popList.size() > k) {
				Individual [] tournamentSet = new Individual[k];
				for (int j = 0; j < k; j++) {
					tournamentSet[j] = popList.remove((int) Math.floor(Math.random()*popList.size()));
				}
				
				Arrays.sort(tournamentSet, new WeightedRandom());
	
				//System.out.println(tournamentSet[0].getFitness() + " vs " + tournamentSet[k-1].getFitness());
				//System.out.println("Adding winner from tournamentSet: " + tournamentSet[0]);
				newPopList.add(tournamentSet[0]);
				
				for (int j = 1; j < k; j++) {
					popList.add(tournamentSet[j]);
				}
			}
			else {
				numParents = i+1;
				break;
			}
			
		}
		
		int count = 0;
		for (Individual i : newPopList) {
			//System.out.println(count + " derp:" + i);
			++count;
		}
		
		//System.out.println(numParents + " vs " + popsize);
		while (newPopList.size() < popsize) {
			newPopList.add(newPopList.get((int) Math.floor(Math.random()*numParents)));
			//System.out.println(" herp:" + newPopList.size());
		}
		
		// Code for your selection operator ...
		newPopList.toArray(newPopulation);
		return newPopulation;
	}
	
	private class WeightedRandom implements Comparator<Individual>
	{

		@Override
		public int compare(Individual i, Individual o) {
			// TODO Auto-generated method stub
			double weight1 = Math.random();
			double weight2 = Math.random();
			if (i.getFitness()*weight1 < o.getFitness()*weight2) 
			    return -1;
			else if (i.getFitness()*weight1 > o.getFitness()*weight2)
			    return 1;
			return 0;
		}
	}
	
}
