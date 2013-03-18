import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MySelection extends Selection {
	int popsize;

	// arguments for tournament selection
	int k = 11;
	double percentParents = 0.6;
	double p = 0.6;
	boolean skewed = false;

	public void initialize(int popsize) {
		this.popsize = popsize;
	}

	// Implemented Tournament Selection
	public Individual[] select(Individual[] population) {

		int numParents = (int) Math.floor(percentParents * popsize);

		Individual[] newPopulation = new Individual[popsize];

		ArrayList<Individual> popList = new ArrayList<Individual>(
				Arrays.asList(population));
		ArrayList<Individual> newPopList = new ArrayList<Individual>(popsize);

		for (int i = 0; i < numParents; i++) {
			// create set, choose winner, add to new pop
			if (popList.size() > k) {
				Individual[] tournamentSet = new Individual[k];
				for (int j = 0; j < k; j++) {
					tournamentSet[j] = popList.remove((int) Math.floor(Math
							.random() * popList.size()));
				}	

				// Probabilistic
				if (!skewed) {
					//sort and pick top with probability p
					Arrays.sort(tournamentSet);
					newPopList.add(chooseWinner(tournamentSet));
				} else {
					//sort with skewness
					Arrays.sort(tournamentSet, new RandomlySkewedComparator());
					newPopList.add(tournamentSet[0]);
				}

				for (int j = 1; j < k; j++) {
					popList.add(tournamentSet[j]);
				}
			} else {
				numParents = i + 1;
				break;
			}

		}
		
		while (newPopList.size() < popsize) {
			newPopList.add(newPopList.get((int) Math.floor(Math.random()
					* numParents)));
		}

		newPopList.toArray(newPopulation);
		return newPopulation;
	}

	// probabilistic selection
	private Individual chooseWinner(Individual[] tournamentSet) {
		for (int i = 0; i < tournamentSet.length; i++) {
			if (Math.random() < p) {
				// System.out.println("Adding winner from tournamentSet: " + i);
				return tournamentSet[i];
			}
		}
		return tournamentSet[tournamentSet.length - 1];
	}

	private class RandomlySkewedComparator implements Comparator<Individual> {
		@Override
		public int compare(Individual i, Individual o) {
			// TODO Auto-generated method stub
			double weight1 = Math.random();
			double weight2 = Math.random();
			if (i.getFitness() * weight1 < o.getFitness() * weight2)
				return -1;
			else if (i.getFitness() * weight1 > o.getFitness() * weight2)
				return 1;
			return 0;
		}
	}

}
