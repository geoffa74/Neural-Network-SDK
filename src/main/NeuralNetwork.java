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
	
	//TODO Turn layers into double[][] and add all garbage inside to the NeuralNetwork;
	
	private static final long serialVersionUID = 1L;
	private Layer[] layers;
	private int numLayers;
	private double[][][] weights;
	private ActivationFunction hiddenLayerFunction;
	private ActivationFunction outputLayerFunction;
	private CostFunction costFunction;
	
	public NeuralNetwork(Layer[] layers, ActivationFunction hiddenLayerFunction, ActivationFunction outputLayerFunction, CostFunction costFunction) {
		this.layers = layers;
		numLayers = layers.length;
		this.hiddenLayerFunction = hiddenLayerFunction;
		this.outputLayerFunction = outputLayerFunction;
		this.costFunction = costFunction;
		weights = new double[layers.length - 1][][];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = new double[layers[i + 1].length()][layers[i].length()];
			for(int j = 0; i < layers[i + 1].length(); i++) {
				for(int k = 0; j < layers[i].length(); j++) {
					weights[i][j][k] = Math.random();
				}
			}
		}
	}
	
	public int getResult(double[] values) {
		//TODO
		for(int i = 1; i < layers.length; i++) {
			
		}
		return -1;
	}
	
	public void train(TrainingData[] batch, double learningConstant) {
		//Initialize weightChangeTotal
		double[][][] weightChangeTotal = new double[numLayers - 1][][];
		for(int i = 0; i < weights.length; i++) {
			weightChangeTotal[i] = new double[layers[i + 1].length()][layers[i].length()];
			for(int j = 0; i < layers[i + 1].length(); i++) {
				for(int k = 0; j < layers[i].length(); j++) {
					weights[i][j][k] = 0.0;
				}
			}
		}
		//Update weights and biases
		for(int batchIndex = 0; batchIndex < batch.length; batchIndex++) {
			input(batch[batchIndex].getInput());
			//update weights and biases of output layer
			for(int i = 0; i < layers[numLayers - 1].length(); i++) {
				double activationChange = getActivationDerivitive(layers[numLayers - 2].getNodeBeforeActivation(i), outputLayerFunction);
				double nodeChange = getCostDerivitive(layers[numLayers - 1].getNode(i), batch[batchIndex].getOutputNode(i));
				for(int j = 0; j < layers[numLayers - 2].length(); j++) {
					weightChangeTotal[weights.length - 1][j][i] = layers[j].getNode(i) * activationChange * nodeChange;
					layers[numLayers - 2].addNodeChange(j, weights[weights.length - 1][j][i] * activationChange * nodeChange);
				}
				layers[numLayers - 1].addToBiasChangeTotal(i, activationChange * nodeChange);
			}
			//update weights and biases of hidden layers;
			for(int i = numLayers - 2; i >= 0; i--) {
				for(int j = 0; j < layers[i].length(); j++) {
					double activationChange = getActivationDerivitive(layers[i - 1].getNodeBeforeActivation(j), hiddenLayerFunction);
					double nodeChange = getCostDerivitive(layers[i].getNode(j), batch[batchIndex].getOutputNode(j));
					for(int k = 0; k < layers[i - 1].length(); k++) {
						weightChangeTotal[weights.length - 1][k][j] = layers[k].getNode(j) * activationChange * nodeChange;
						layers[i - 1].addNodeChange(j, weights[weights.length - 1][k][j] * activationChange * nodeChange);
					}
					layers[i].addToBiasChangeTotal(j, activationChange * nodeChange);
				}
			}
		}
		//Add to weights and biases the gradient of the training data (Average of total changes).

	}

	private double getActivationDerivitive(double nodeBeforeActivation, ActivationFunction function) {
		// TODO Auto-generated method stub
		return 0;
	}

	private double getCostDerivitive(double actual, double target) {
		switch(costFunction) {
		case QUADRATIC:
			return actual - target;
		}
		return 0;
	}

	protected void input(double[] values) {
		layers[0].setNodes(values);
		//Calculate nodes in inner layers
		for(int i = 1; i < layers.length - 1; i++) {
			for(int j = 0; j < layers[i].length(); j++) {
				double value = 0.0;
				for(int k = 0; k < layers[i - 1].length(); k++) {
					value += layers[i - 1].getNode(k) * weights[i - 1][j][k];
				}
				layers[i].setNode(j, value);
			}
			layers[i].addBiases();
			layers[i].applyActivationFunction(hiddenLayerFunction);
		}
		//Calculate nodes in outer layer
		for(int i = 0; i < layers[layers.length - 1].length(); i++) {
			double value = 0.0;
			for(int j = 0; j < layers[layers.length - 2].length(); j++) {
				value += layers[layers.length - 2].getNode(j) * weights[layers.length - 2][i][j];
			}
			layers[layers.length - 1].setNode(i, value);
		}
		layers[layers.length - 1].addBiases();
		layers[layers.length - 1].applyActivationFunction(outputLayerFunction);
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
