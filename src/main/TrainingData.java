package main;

public class TrainingData {
	
	private double[] input;
	private double[] output;
	
	public TrainingData(double[] input, double[] output) {
		this.input = input;
		this.output = output;
	}

	public double[] getInput() {
		return input;
	}

	public void setInput(double[] input) {
		this.input = input;
	}

	public double[] getOutput() {
		return output;
	}
	
	public double getOutputNode(int index) {
		return output[index];
	}

	public void setOutput(double[] output) {
		this.output = output;
	}

}
