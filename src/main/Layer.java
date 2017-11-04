package main;

import main.ActivationFunctions.ActivationFunction;

public class Layer {
	
	private double[] nodes;
	private double[] biases;
	private int length;
	
	public Layer(int length) {
		this.length = length;
		nodes = new double[length];
		biases = new double[length];
		for(int i = 0; i < length; i++) {
			nodes[i] = Math.random();
			biases[i] = Math.random();
		}
	}
	
	public int length() {
		return length;
	}

	protected void setNodes(double[] values) {
		nodes = values;
	}
	
	protected double[] getNodes() {
		return nodes;
	}
	
	protected void addBiases() {
		for(int i = 0; i < length; i++) {
			nodes[i] += biases[i];
		}
	}
	
	protected void applyActivationFunction(ActivationFunction function) {
		switch(function) {
		case SIGMOID:
			applySigmoid();
			break;
		}
	}
	
	protected void applySigmoid() {
		for(int i = 0; i < length; i++) {
			nodes[i] = 1.0 / (1.0 + Math.exp(-nodes[i]));
		}
	}

}
