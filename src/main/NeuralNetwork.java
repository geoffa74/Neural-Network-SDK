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
import main.WeightMatrix.Type;

public class NeuralNetwork implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Layer[] layers;
	private int numLayers;
	private WeightMatrix[] weights;
	private ActivationFunction hiddenLayerFunction;
	private ActivationFunction outputLayerFunction;
	private CostFunction costFunction;
	
	public NeuralNetwork(Layer[] layers, ActivationFunction hiddenLayerFunction, ActivationFunction outputLayerFunction, CostFunction costFunction) {
		this.layers = layers;
		numLayers = layers.length;
		this.hiddenLayerFunction = hiddenLayerFunction;
		this.outputLayerFunction = outputLayerFunction;
		this.costFunction = costFunction;
		weights = new WeightMatrix[layers.length - 1];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = new WeightMatrix(layers[i + 1].length(), layers[i].length(), Type.RANDOM);
		}
	}
	
	public int getResult(double[] values) {
		//TODO
		for(int i = 1; i < layers.length; i++) {
			
		}
		return -1;
	}
	
	public void train(TrainingData[] batch, double learningConstant) {
		WeightMatrix[] weightChangeTotal = new WeightMatrix[numLayers - 1];
		for(int i = 0; i < weights.length; i++) {
			weightChangeTotal[i] = new WeightMatrix(layers[i + 1].length(), layers[i].length(), Type.ZERO);
		}
		for(int batchIndex = 0; batchIndex < batch.length; batchIndex++) {
			input(batch[batchIndex]);
		}

	}

	protected void input(double[] values) {
		layers[0].setNodes(values);
		for(int i = 1; i < layers.length - 1; i++) {
			layers[i].setNodes(weights[i - 1].multiply(layers[i - 1].getNodes()));
			layers[i].addBiases();
			layers[i].applyActivationFunction(hiddenLayerFunction);
		}
		layers[numLayers - 1].setNodes(weights[numLayers - 2].multiply(layers[numLayers - 2].getNodes()));
		layers[numLayers - 1].addBiases();
		layers[numLayers - 1].applyActivationFunction(outputLayerFunction);
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
