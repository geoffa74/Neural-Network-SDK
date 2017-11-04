package test;

public class SoftmaxTest {
	
	public static void main(String[] args) {
		double[] input = {Math.E, Math.PI, 1.242334, 3.2345, 4, 8};
		double[] result = softmax(input);
		double sum = 0;
		for(int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
			sum += result[i];
		}
	}
	
	public static double[] softmax(double[] z) {
		double sum = 0;
		for(int i = 0; i < z.length; i++) {
			sum += Math.exp(z[i]);
		}
		double[] result = new double[z.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = Math.exp(z[i])/sum;
		}
		return result;
	}

}
