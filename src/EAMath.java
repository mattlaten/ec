public class EAMath {
    // random number with standard Gaussian distribution
    public static double gaussian() {
        double U = Math.random();
        double V = Math.random();
        return Math.sin(2 * Math.PI * V) * Math.sqrt((-2 * Math.log(U)));
    }

    // random number with Gaussian distribution of mean mu and stddev sigma
    public static double gaussian(double mu, double sigma) {
        return mu + sigma * gaussian();
    }

    public static double uniform(double sigma) {
	double U = Math.random();
	return (U-0.5)*2*sigma;
    }

}
