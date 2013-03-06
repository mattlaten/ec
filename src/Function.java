import java.lang.reflect.Constructor;

public abstract class Function {
    protected double lowerBound;
    protected double upperBound;
    protected int dimension;
    protected String name;
    
    public Function(double lowerBound, 
		    double upperBound, 
		    int dimension, 
		    String name) {
	this.lowerBound = lowerBound;
	this.upperBound = upperBound;
	this.dimension = dimension;
	this.name = name;
    }

    public abstract double fitness(double[] x) throws FunctionException;

    public String getName() {
	return name;
    }
    
    public int getDimension() {
	return dimension;
    }
    
    protected static void checkConstraints(double[] x, double lowerBound, double upperBound, String exception) throws FunctionException {
	for (int i = 0; i < x.length; i++) {
	    if ((x[i]<lowerBound)||(x[i]>upperBound)) {
		FunctionException excep = null;
		try {
		    Class c = Class.forName(exception);
		    Constructor con = c.getConstructor(Class.forName("java.lang.String"));
		    excep = (FunctionException) con.newInstance("Constraint violated by x["+i+"] = "+x[i]);
		    
		    //Removes the reflection stackTraceElements from the stackTrace.
		    StackTraceElement[] stackTraceElements = excep.getStackTrace();
		    StackTraceElement[] newStackTrace = new StackTraceElement[stackTraceElements.length - 4];
		    for (int j = 0; j < newStackTrace.length; j++) {
			newStackTrace[j] = stackTraceElements[4+j];			
		    }
		    excep.setStackTrace(newStackTrace);
		} catch(Exception e) {
		    e.printStackTrace();
		}
		throw excep;
	    }

	}
    }
}
