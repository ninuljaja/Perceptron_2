/**
 * Perceptron Class
 */
package com.example.ai_assignment3b;

public class Perceptron {
    private double[] weights;
    private double bias;
    private double learningRate;
    private boolean trainingCompleted;

    /**
     * Constructor that creates Perceptron object
     * @param inputSize an integer that holds number of input values
     */
    public Perceptron(int inputSize) {
        this.weights = new double[inputSize];
        initializeWeights();
    }

    /**
     * Method that initializes weights with random values between -0.5 and 0.5
     */
    private void initializeWeights(){
        this.bias = Math.random()-0.5;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random()-0.5;
        }
    }

    /**
     * Method that starts training
     * @param trainingData 2D array og integers that holds training input data
     * @param targets array of integers that holds expected output for each sample of training input data
     * @param learningRate an integer that holds learning rate
     */
    public void training(double[][] trainingData, int[] targets, double learningRate){

        this.learningRate = learningRate;
        do {
            trainingCompleted = true;
            for (int i = 0; i < trainingData.length; i++) {
                trainHelper(trainingData[i], targets[i]);
            }
        }while (!trainingCompleted);
    }
    /**
     * Method that updates weights if the predicted output is not equals to expected one
     * @param input array of integers that holds one sample of training input data
     * @param target an integer that holds expected output fot the sample
     */
    public void trainHelper(double[] input, int target) {
        // find predicted output
        int prediction = predict(input);
        // calculate an error
        int error = target - prediction;
        if(error != 0){
            // if predicted output is not equals to expected one, update weights and continue training
            for (int i = 0; i < weights.length; i++) {
                weights[i] += learningRate * error * input[i];
            }
            bias += learningRate*error;
            trainingCompleted = false;
        }
    }
    /**
     * Method that calculates and returns the predicted output for the sample data
     * @param input array of integers that holds weights of each input data
     * @return an integer that represents the predicted output
     */
    public int predict(double[] input) {
        // Step function
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * input[i];
        }
        return (sum > 0) ? 1 : -1;
    }
}