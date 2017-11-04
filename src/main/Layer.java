package main;

import java.io.Serializable;

import main.ActivationFunctions.ActivationFunction;

public class Layer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private double[] nodes;
	private double[] biases;
	private double[] biasChangeTotal;
	private int changeCount;
	private int length;
	
	public Layer(int length) {
		this.length = length;
		nodes = new double[length];
		biases = new double[length];
		biasChangeTotal = new double[length];
		changeCount = 0;
		for(int i = 0; i < length; i++) {
			nodes[i] = Math.random();
			biases[i] = Math.random();
			biasChangeTotal[i] = 0.0;
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
	
	protected void addToBiasChangeTotal(double[] change) {
		for(int i = 0; i < length; i++) {
			biasChangeTotal[i] += change[i];
		}
		changeCount++;
	}
	
	protected void addAverageBiasChanges() {
		for(int i = 0; i < length; i++) {
			biases[i] += biasChangeTotal[i] / changeCount;
			biasChangeTotal[i] = 0.0;
		}
		changeCount = 0;
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
