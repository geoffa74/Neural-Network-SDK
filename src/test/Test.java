package test;

import main.ActivationFunctions.ActivationFunction;
import main.CostFunctions.CostFunction;
import main.NeuralNetwork;

public class Test {
	
	public static void main(String[] args) {
		double[][] nodes = new double[3][];
		nodes[0] = new double[2];
		nodes[1] = new double[3];
		nodes[2] = new double[2];
		NeuralNetwork nn = new NeuralNetwork(nodes, ActivationFunction.SIGMOID, ActivationFunction.SIGMOID, CostFunction.QUADRATIC);
		double[] input = {1.1, 2.2};
		double[] results = nn.getResults(input);
		for(double n : results) {
			System.out.println(n);
		}
	}

}
