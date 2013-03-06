public class EC2013 extends Function 
{
    public EC2013() 
    {
     super(-5000, 5000, 100, "EC2013");
    }
    
    public double fitness(double[] x) throws FunctionException 
    {
     if (x.length != 100) 
       {
        throw new FunctionException("Argument vector length != 100");
       }
	
     checkConstraints(x, lowerBound, upperBound, "Exception");

     double v1 = 0;   
     double v2 = 1;

     for (int i = 0; i < x.length; i++) 
	{
         v1 += Math.pow(x[i],2)/4000.0; 
         v2 *= Math.cos(x[i]/Math.sqrt(i+1)); 
	}
	
     return v1-v2+1;
    }
}
