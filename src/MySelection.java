import java.util.Arrays;

public class MySelection extends Selection {
	int popsize;
	
	//tourney selections
	
	int k = 3;
	
	public void initialize(int popsize) {
		this.popsize = popsize;
	}

	public Individual[] select(Individual[] population) {
		
		Individual[] newPopulation = new Individual[popsize];

		for (int i = 0; i < popsize; i++){
			//create set, choose winner, add to new pop
			Individual [] tournamentSet = new Individual[k];
			for (int j = 0; j < k; j++) {
				tournamentSet[j] = population[(int) Math.floor(Math.random()*popsize)];
			}
			
			Arrays.sort(tournamentSet);

			//System.out.println(tournamentSet[0].getFitness() + " vs " + tournamentSet[k-1].getFitness());
			newPopulation[i] = tournamentSet[0];
		}
		
		// Code for your selection operator ...
		
		return newPopulation;
	}
	
	
	
}
