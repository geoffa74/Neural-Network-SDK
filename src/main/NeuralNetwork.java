package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import main.ActivationFunctions.ActivationFunction;
import main.CostFunctions.CostFunction;

public class NeuralNetwork implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private double[][] nodes;
	private double[][] nodesBeforeActivation;
	private int numLayers;
	private double[][][] weights;
	private double[][] biases;
	private ActivationFunction hiddenLayerFunction;
	private ActivationFunction outputLayerFunction;
	private CostFunction costFunction;
	
	public NeuralNetwork(double[][] nodes, ActivationFunction hiddenLayerFunction, ActivationFunction outputLayerFunction, CostFunction costFunction) {
		this.nodes = nodes;
		numLayers = nodes.length;
		this.hiddenLayerFunction = hiddenLayerFunction;
		this.outputLayerFunction = outputLayerFunction;
		this.costFunction = costFunction;
		biases = new double[nodes.length][];
		nodesBeforeActivation = new double[nodes.length][];
		for(int i = 0; i < nodes.length; i++) {
			biases[i] = new double[nodes[i].length];
			nodesBeforeActivation[i] = new double[nodes[i].length];
			for(int j = 0; j < nodes[i].length; j++) {
				this.nodes[i][j] = Math.random();
				biases[i][j] = Math.random();
			}
		}
		weights = new double[nodes.length - 1][][];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = new double[nodes[i + 1].length][];
			for(int j = 0; j < nodes[i + 1].length; j++) {
				weights[i][j] = new double[nodes[i].length];
				for(int k = 0; k < nodes[i].length; k++) {
					weights[i][j][k] = Math.random();
				}
			}
		}
	}
	
	public double[] getResults(double[] values) {
		input(values);
		return nodes[numLayers - 1];
	}
	
	public void train(TrainingData[] batch, double learningConstant) {

		double[][] nodeChanges = new double[nodes.length][nodes[0].length];
		double[][] biasChanges = new double[nodes.length][nodes[0].length];
		for(int i = 0; i < nodes.length; i++) {
			for(int j = 0; j < nodes[i].length; j++) {
				biasChanges[i][j] = 0.0;
				nodeChanges[i][j] = 0.0;
			}
		}
		
		double[][][] weightChanges = new double[numLayers - 1][][];
		for(int i = 0; i < weights.length; i++) {
			weightChanges[i] = new double[nodes[i + 1].length][nodes[i].length];
			for(int j = 0; i < nodes[i + 1].length; i++) {
				for(int k = 0; j < nodes[i].length; j++) {
					weights[i][j][k] = 0.0;
				}
			}
		}
		
		//Update weights and biases
		for(int batchIndex = 0; batchIndex < batch.length; batchIndex++) {
			input(batch[batchIndex].getInput());
			//update weights and biases of output layer
			for(int i = 0; i < nodes[numLayers - 1].length; i++) {
				double activationChange = getActivationDerivitive(nodesBeforeActivation[numLayers - 2][i], outputLayerFunction);
				nodeChanges[numLayers - 1][i] = getCostDerivitive(nodes[numLayers - 1][i], batch[batchIndex].getOutputNode(i));
				biasChanges[numLayers - 1][i] = activationChange * nodeChanges[numLayers - 1][i];
				for(int j = 0; j < nodes[numLayers - 2].length; j++) {
					weightChanges[weights.length - 1][j][i] = nodes[j][i] * activationChange * nodeChanges[numLayers - 1][i];
					nodeChanges[numLayers - 2][j] += weights[weights.length - 1][j][i] * activationChange * nodeChanges[numLayers - 1][i];
				}
				biasChanges[numLayers - 2][i] += activationChange * nodeChanges[numLayers - 1][i];
			}
			//update weights and biases of hidden layers;
			for(int i = numLayers - 2; i >= 0; i--) {
				for(int j = 0; j < nodes[i].length; j++) {
					double activationChange = getActivationDerivitive(nodesBeforeActivation[i - 1][j], hiddenLayerFunction);
					for(int k = 0; k < nodes[i - 1].length; k++) {
						weightChanges[weights.length - 1][k][j] = nodes[k][j] * activationChange * nodeChanges[i][j];
						nodeChanges[i - 1][k] += weights[weights.length - 1][k][j] * activationChange * nodeChanges[i][j];
					}
					biasChanges[i - 1][j] += activationChange * nodeChanges[i][j];
				}
			}
		}
		//Add to weights and biases the gradient of the training data (Average of total changes).
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].length; j++) {
				for(int k = 0; k < weights[j].length; k++) {
					weights[i][j][k] += weightChanges[i][j][k] / batch.length;
				}
				biases[i][j] += biasChanges[i][j] / batch.length;
			}
		}

	}

	private double getActivationDerivitive(double value, ActivationFunction function) {
		switch(function) {
		case SIGMOID:
			return getSigmoidDerivitive(value);
		default:
			return -1;
		}
	}
	
	protected double getSigmoidDerivitive(double value) {
		double sigmoid =  1.0 / (1.0 + Math.exp(-value));
		return (1 - sigmoid) * sigmoid;
	}

	private double getCostDerivitive(double actual, double target) {
		switch(costFunction) {
		case QUADRATIC:
			return actual - target;
		}
		return 0;
	}

	protected void input(double[] values) {
		nodes[0] = values;
		//Calculate nodes in inner layers
		for(int i = 1; i < nodes.length - 1; i++) {
			for(int j = 0; j < nodes[i].length; j++) {
				double value = 0.0;
				for(int k = 0; k < nodes[i - 1].length; k++) {
					value += nodes[i - 1][k] * weights[i - 1][j][k];
				}
				nodes[i][j] = value;
			}
			//add biases
			for(int j = 0; j < nodes[i].length; j++) {
				nodes[i][j] += biases[i][j];
			}
			nodesBeforeActivation[i] = nodes[i];
			for(int j = 0; j < nodes[i].length; j++) {
				nodes[i][j] = getActivationFunction(nodes[i][j], hiddenLayerFunction);
			}
		}
		//Calculate nodes in outer layer
		for(int i = 0; i < nodes[nodes.length - 1].length; i++) {
			double value = 0.0;
			for(int j = 0; j < nodes[nodes.length - 2].length; j++) {
				value += nodes[nodes.length - 2][j] * weights[nodes.length - 2][i][j];
			}
			nodes[nodes.length - 1][i] = value;
		}
		for(int i = 0; i < nodes[nodes.length - 1].length; i++) {
			nodes[nodes.length - 1][i] += biases[nodes.length - 1][i];
		}
		nodesBeforeActivation[nodes.length - 1] = nodes[nodes.length - 1];
		for(int i = 0; i < nodes[nodes.length - 1].length; i++) {
			nodes[nodes.length - 1][i] = getActivationFunction(nodes[nodes.length - 1][i], hiddenLayerFunction);
		}	
	}
	
	protected double getActivationFunction(double value, ActivationFunction function) {
		nodesBeforeActivation = nodes;
		switch(function) {
		case SIGMOID:
			return getSigmoid(value);
		default:
			return -1;
		}
	}
	
	protected double getSigmoid(double value) {
		return 1.0 / (1.0 + Math.exp(-value));		
	}
	
	public void save(String location, String fileName) throws IOException {
		File file = new File(location + "\\" + fileName + ".jnn");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
	}
	
	public static void save(NeuralNetwork nn, String location) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(new File(location));
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(nn);
		objectOutputStream.close();
	}
	
	public static NeuralNetwork load(String location) throws ClassNotFoundException, IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(location));
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		NeuralNetwork nn = (NeuralNetwork) objectInputStream.readObject();
		objectInputStream.close();
		return nn;
	}

}
