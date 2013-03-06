public abstract class Selection {
    public abstract void initialize(int popsize);

    public abstract Individual[] select(Individual[] population);
}
