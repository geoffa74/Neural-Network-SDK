package main;

public class WeightMatrix {
	
	//i x j where i is the index of the head node and j is the index of the tail node that connect the weight.
	private double[][] weights;
	
	public WeightMatrix(int headLayerLength, int tailLayerLength) {
		weights = new double[headLayerLength][tailLayerLength];
		for(int i = 0; i < headLayerLength; i++) {
			for(int j = 0; j < tailLayerLength; j++) {
				weights[i][j] = Math.random();
			}
		}
	}
	
	public WeightMatrix(double[][] weights) {
		this.weights = weights;
	}
	
	public double[] multiply(double[] values) {
		double[] result = new double[values.length];
		double temp;
		for(int i = 0; i < weights.length; i++) {
			temp = 0;
			for(int j = 0; j < weights[0].length; j++) {
				temp += weights[i][j] * values[j];
			}
			result[i] = temp;
		}
		return result;
	}

}
