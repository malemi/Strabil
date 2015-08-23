/**
 * 
 */
package org.strabil.math;

import org.strabil.utils.DoTest;

/**
 * Generates according to a given array, which is the probability density function. 
 * If min and max are not given, the output is between 0 and 1, otherwise between min and max.
 * 
 * @author Mario Alemi
 *
 */
public class InverseCumulativeArray implements InverseCumulative {

	
	private double[] pdf;
	private double min;
	private double max;
	
	public InverseCumulativeArray(double ... probabilityArray){
		pdf = probabilityArray;
		min = 0.0;
		max = 1.0;
	}
	
	public InverseCumulativeArray(double minimum, double maximum, double ... probabilityArray){
		pdf = probabilityArray;
		min = minimum;
		max = maximum;
	}

	
	/**
	 * 
	 * 
	 */
	@Override
	public double op(double x) {
		
		int l = pdf.length;
		double bin = (max - min)/l;
		double[] intercept = new double[l];
		double output = -1;
		//normalized pdf
		double sum = 0;
		double[] npdf = new double[l]; 

		DoTest.require(x>=0 && x <=1, "InversCumulativeArray: argument must be in [0,1].");
		if(x==0) return min;
		if(x==1) return max;
		
		for(int i=0;i<l ;i++){
			sum = sum + pdf[i];
		}
		for(int i=0;i<l ;i++){
			npdf[i] = pdf[i]/sum;
		}
		
		
		for(int i=0;i<l ;i++){
			if(i==0) intercept[i] = 0;
			else intercept[i] = intercept[i] + bin*npdf[i-1]; 
		
			//Integrated pdf (ie x) in the i-th bin is: x = npdf[i]*output + intercept[i]
			if(x>=intercept[i] && x<= intercept[i] + npdf[i]*bin )
				output = (x - intercept[i])/npdf[i];	
		}
		
		if(output == -1) DoTest.error("ERROR in InverseCumulativeArray op(x)");
		return output;
		
	}

}












