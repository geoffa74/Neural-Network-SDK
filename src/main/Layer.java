package main;

import java.io.Serializable;

import main.ActivationFunctions.ActivationFunction;

public class Layer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private double[] nodes;
	private double[] nodesBeforeActivation;
	private double[] biases;
	private double[] biasChangeTotal;
	private double[] nodeChangeTotal;
	private int length;
	
	public Layer(int length) {
		this.length = length;
		nodes = new double[length];
		biases = new double[length];
		biasChangeTotal = new double[length];
		nodeChangeTotal = new double[length];
		for(int i = 0; i < length; i++) {
			nodes[i] = Math.random();
			biases[i] = Math.random();
			biasChangeTotal[i] = 0.0;
			nodeChangeTotal[i] = 0.0;
		}
	}
	
	public void addNodeChange(int index, double amount) {
		nodeChangeTotal[index] += amount;
	}
	
	public double getNodeBeforeActivation(int index) {
		return nodesBeforeActivation[index];
	}
	
	public double getNode(int index) {
		return nodes[index];
	}
	
	public void setNode(int index, double value) {
		nodes[index] = value;
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
	
	protected void addToBiasChangeTotal(int index, double change) {
		biasChangeTotal[index] += change;
	}
	
	protected void addAverageBiasChanges(int changeCount) {
		for(int i = 0; i < length; i++) {
			biases[i] += biasChangeTotal[i] / changeCount;
			biasChangeTotal[i] = 0.0;
		}
		changeCount = 0;
	}
	
	protected void applyActivationFunction(ActivationFunction function) {
		nodesBeforeActivation = nodes;
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
