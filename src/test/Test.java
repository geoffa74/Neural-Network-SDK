package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.ActivationFunctions.ActivationFunction;
import main.CostFunctions.CostFunction;
import main.NeuralNetwork;
import main.TrainingData;

public class Test {
	
	public static void main(String[] args) {
		create();
		trainNeuralNetwork(100, 1);
		test();
	}
	
	private static void test() {
		try {
			int correct = 0;
			NeuralNetwork nn = NeuralNetwork.load("C:\\Users\\appelbaumgl\\Desktop\\MNIST.jnn");
			FileInputStream imageInput = new FileInputStream(new File("t10k-images.idx3-ubyte"));
			FileInputStream labelInput = new FileInputStream(new File("t10k-labels.idx1-ubyte"));
			imageInput.skip(16);
			labelInput.skip(8);
			for(int i = 0; i < 10000; i++) {
				double[] input = new double[784];
				byte[] pixel = new byte[1];
				byte[] label = new byte[1];
				for(int j = 0; j < 784; j++) {
					imageInput.read(pixel);
					input[j] = (double)(pixel[0] & 0xFF) / 255.0;						
				}
				double[] output = nn.getResults(input);
				labelInput.read(label);
				int n = (int)label[0];
				double biggest = 0;
				int answer = 0;
				for(int j = 0; j < 10; j++) {
					if(output[j] > biggest) {
						biggest = output[j];
						answer = j;
					}
				}
				if(n == answer) correct++;
			}
			System.out.print(correct / 10000.0);
			imageInput.close();
			labelInput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void create() {
		double[][] nodes = new double[4][];
		nodes[0] = new double[784];
		nodes[1] = new double[16];
		nodes[2] = new double[16];
		nodes[3] = new double[10];
		NeuralNetwork nn = new NeuralNetwork(nodes, ActivationFunction.RELU, ActivationFunction.SIGMOID, CostFunction.QUADRATIC);
		try {
			nn.save("C:\\Users\\appelbaumgl\\Desktop", "MNIST");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void trainNeuralNetwork(int batchSize, double learningConstant) {
		try {
			NeuralNetwork nn = NeuralNetwork.load("C:\\Users\\appelbaumgl\\Desktop\\MNIST.jnn");
			FileInputStream imageInput = new FileInputStream(new File("train-images.idx3-ubyte"));
			FileInputStream labelInput = new FileInputStream(new File("train-labels.idx1-ubyte"));
			imageInput.skip(16);
			labelInput.skip(8);
			TrainingData[] batch;
			for(int i = 0; i < 60000 / batchSize; i++) {
				batch = new TrainingData[batchSize];
				for(int j = 0; j < batchSize; j++) {
					double[] input = new double[784];
					double[] output = new double[10];
					byte[] pixel = new byte[1];
					byte[] label = new byte[1];
					for(int k = 0; k < 784; k++) {
						imageInput.read(pixel);
						input[k] = (double)(pixel[0] & 0xFF) / 255.0;						
					}
					labelInput.read(label);
					int n = (int)label[0];
					for(int k = 0; k < 10; k++) {
						if(k != n) {
							output[k] = 0.0;
						} else {
							output[k] = 1.0;
						}
					}
					batch[j] = new TrainingData(input, output);
				}
				nn.train(batch, learningConstant);
			}
			imageInput.close();
			labelInput.close();
			nn.save("C:\\Users\\appelbaumgl\\Desktop", "MNIST");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
