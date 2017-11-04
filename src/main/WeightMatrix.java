package main;

import java.io.Serializable;

public class WeightMatrix implements Serializable {

	private static final long serialVersionUID = 1L;
	//i x j where i is the index of the head node and j is the index of the tail node that connect the weight.
	private double[][] weights;
	private int headLayerLength;
	private int tailLayerLength;
	
	protected enum Type {
		RANDOM,
		ZERO
	}
	
	public WeightMatrix(int headLayerLength, int tailLayerLength, Type type) {
		this.headLayerLength = headLayerLength;
		this.tailLayerLength = tailLayerLength;
		weights = new double[headLayerLength][tailLayerLength];
		for(int i = 0; i < headLayerLength; i++) {
			for(int j = 0; j < tailLayerLength; j++) {
				switch(type) {
				case RANDOM:
					weights[i][j] = Math.random();
					break;
				case ZERO:
					weights[i][j] = 0.0;
					break;
				}
			}
		}
	}
	
	public void zero() {
		for(int i = 0; i < headLayerLength; i++) {
			for(int j = 0; j < tailLayerLength; j++) {
				weights[i][j] = 0.0;
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
