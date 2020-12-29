
public class NeuralNetwork {
	public int inputNodes;	
	public int hiddenNodes;
	public int outputNodes;
	
	public Matrix weightsIH;
	public Matrix weightsHO;
	
	public Matrix biasH;
	public Matrix biasO;
	
	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes) {
		this.inputNodes = inputNodes;
		this.hiddenNodes = hiddenNodes;
		this.outputNodes = outputNodes;
		
		weightsIH = new Matrix(this.hiddenNodes, this.inputNodes);
		weightsHO = new Matrix(this.outputNodes, this.hiddenNodes);
		weightsIH.randomize();
		weightsHO.randomize();
		
		biasH = new Matrix(this.hiddenNodes, 1);
		biasO = new Matrix(this.outputNodes, 1);
		biasH.randomize();
		biasO.randomize();
	}
	
	public NeuralNetwork(NeuralNetwork a) {
		inputNodes = a.inputNodes;
		hiddenNodes = a.hiddenNodes;
		outputNodes = a.outputNodes;
		
		weightsIH = a.weightsIH.copy();
		weightsHO = a.weightsHO.copy();
		
		biasH = a.biasH.copy();
		biasO = a.biasO.copy();
	}
	
	public double[] feedForward(double[] input) {
		//generating hidden outputs
		Matrix inputs = Matrix.fromArray(input);
		Matrix hiddens = Matrix.multiply(weightsIH, inputs);
		hiddens.add(biasH);
		//activation function
		hiddens.function();
		
		Matrix outputs = Matrix.multiply(weightsHO, hiddens);
		outputs.add(biasO);
		//activation function
		outputs.function();
		
		return outputs.toArray();
	}
	
	public NeuralNetwork copy() {
		return new NeuralNetwork(this);
	}
	
	public void mutate(double mutationRate) {
		weightsIH.mutate(mutationRate);
		weightsHO.mutate(mutationRate);
		
		biasH.mutate(mutationRate);
		biasO.mutate(mutationRate);
	}
}
