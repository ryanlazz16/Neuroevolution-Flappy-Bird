import java.util.*;

public class Matrix {
	public int rows;
	public int cols;
	public double[][] data;
	public static Random random = new Random();
	
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		data = new double[rows][cols];
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				data[i][j] = 0;
			}
		}
	}
	
	public Matrix copy() {
		Matrix answer = new Matrix(rows, cols);
		for(int i = 0; i<answer.rows; i++) {
			for(int j = 0; j<answer.cols; j++) {
				answer.data[i][j] = data[i][j];
			}
		}
		return answer;
	}
	
	public void mutate(double mutationRate) {
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				double randomNum = Math.random();
				if(randomNum<mutationRate/2) {
					//data[i][j]+=random.nextDouble()*0.2-0.1;
					data[i][j]+=random.nextGaussian()*0.05;
				}
				else if(randomNum<mutationRate) {
					data[i][j] = Math.random()*2-1;
				}
			}
		}
	}
	
	//applies activation function to whole matrix (sigmoid function)
	public void function() {
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				data[i][j] = 1/(1+Math.pow(Math.E, -data[i][j]));
			}
		}
	}
	
	//transforms a one dimensional array into a matrix with arr.length rows and one column
	public static Matrix fromArray(double[] arr) {
		Matrix result = new Matrix(arr.length, 1);
		for(int i = 0; i<arr.length; i++) {
			result.data[i][0]=arr[i];
		}
		return result;
	}
	
	//subtracts matrix a-b
	public static Matrix subtract(Matrix a, Matrix b) {
		Matrix result = new Matrix(a.rows, a.cols);
		for(int i = 0; i<a.rows; i++) {
			for(int j = 0; j<a.cols; j++) {
				result.data[i][j] = a.data[i][j]-b.data[i][j];
			}
		}
		return result;
	}
	
	//returns a one-dimensional array of all the elements in a Matrix
	public double[] toArray() {
		double[] result = new double[rows*cols];
		int count = 0;
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				result[count] = data[i][j];
				count++;
			}
		}
		return result;
	}
	
	//randomizes the data in the matrix to values between -1 and 1
	public void randomize() {
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				data[i][j] = Math.random()*2-1;
			}
		}
	}
	
	//adds a double to all elements of a matrix
	public void add(double n) {
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				data[i][j]+=n;
			}
		}
	}
	
	//adds an array to this array
	public void add(Matrix a) {
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				data[i][j]+=a.data[i][j];
			}
		}
	}
	
	//returns a matrix that switches all [rows][cols] to [cols][rows]
	public static Matrix transpose(Matrix a) {
		Matrix result = new Matrix(a.cols, a.rows);
		for(int i = 0; i<a.rows; i++) {
			for(int j = 0; j<a.cols; j++) {
				result.data[j][i] = a.data[i][j];
			}
		}
		return result;
	}
	
	//Matrix multiplication
	public static Matrix multiply(Matrix a, Matrix b) {
		if(a.cols!=b.rows) {
			return null;
		}
		Matrix result = new Matrix(a.rows, b.cols);
		for(int i = 0; i<result.rows; i++) {
			for(int j = 0; j<result.cols; j++) {
				double sum = 0;
				for(int k = 0; k<a.cols; k++) {
					sum+=a.data[i][k]*b.data[k][j];
				}
				result.data[i][j] = sum;
			}
		}
		return result;
	}
	
	//Matrix scalar multiplication
	public void multiply(double n) {
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				data[i][j]*=n;
			}
		}
	}
}
