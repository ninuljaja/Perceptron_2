/**
 * PartIIController Class
 */
package com.example.ai_assignment3b;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;

public class PartIIController {
    @FXML
    private Button startTestingBtn;
    @FXML
    private TextFlow finalResult;
    @FXML
    private Label resultLbl;
    @FXML
    private TextField temperatureTextField, windSpeedTextField, outlookTextField, precipitationTextField;
    @FXML
    private Group usersData;

    Perceptron perceptron;
    // Training data
    double[][] trainingData = {
            // temperature, wind speed, outlook (-1 - Sunny, 0 - Mostly Cloudy 1 - Overcast),
            // precipitation ( -1 - No, 1 - Yes)
            {90, 14, -1, -1}, // No delay
            {70, 25, 0, 1} ,  // No delay
            {65, 35, 1, 1},   // Delay
            {50, 15, 0, -1} ,  // No delay
            {40, 30, 1, 1} ,  // Delay
            {35, 16, 1, 1},   // No delay
            {20, 10, -1, -1},   // No delay
            {70, 20, 1, 1},   // Delay
            {30, 15, 0, 1},   // No Delay
            {65, 40, 0, -1},    // Delay
            {62, 17, 1, -1},    // No Delay
            {60, 40, 1, -1}    // Delay
    };


    // Target labels for training data
    int[] targets = {-1, -1, 1, -1, 1, -1, -1, 1, -1, 1, -1, 1}; // -1 for No delay, 1 for delay

    // testing data
    double[][] testingData = {
            {64, 37, 1, 1},   // Delay
            {24, 12, 0, -1},   // No delay
            {10, 20, 1, 1} ,  // Delay
            {40, 15, 1, 1},   // No delay
            {60, 13, 0, -1},   // No delay
            {70, 5, -1, -1},   // No delay
            {75, 10, 0, -1}    // No delay
    };
    // Expected output for testing data
    int[] expected = {1,-1,1,-1,-1,-1,-1};
    int sampleNumber;
    float accuracy;
    int correct;

    /**
     * Method that resets GUI
     */
    public void initialize(){
        // reset GUI
        usersData.setDisable(true);
        perceptron = null;
        sampleNumber = 0;
        startTestingBtn.setText("Start testing");
        startTestingBtn.setDisable(true);
        resultLbl.setText("");
        finalResult.setVisible(true);
        correct = 0;
        // Display training data
        finalResult.getChildren().setAll(new Text("Training data\n"));
        for (int i = 0; i < trainingData.length; i++) {
            Text text1=new Text("\nSample " + (i + 1) + ": " + Arrays.toString(trainingData[i]));
            finalResult.getChildren().addAll(text1);
        }
    }

    /**
     * Event handler for the "Run training data" button. Starts training
     */
    @FXML
    protected void onTrainingBtn() {
        initialize();
        int inputSize = trainingData[0].length;
        // Create s perceptron object with learning rate 0.1
        perceptron = new Perceptron(inputSize);
        double [][] normalizedData = new double[trainingData.length][trainingData[0].length];
        for (int i = 0; i < trainingData.length; i++) {
            normalizedData[i] = normalizeData(trainingData[i]);
        }


        // Training the perceptron
        perceptron.training(normalizedData, targets, 0.1);
        resultLbl.setText("Training completed");
        startTestingBtn.setDisable(false);

        // Display testing data
        finalResult.getChildren().setAll(new Text("Testing data\n"));
        for (int i = 0; i < testingData.length; i++) {
            Text text1=new Text("\nSample " + (i + 1) + ": " + Arrays.toString(testingData[i]));
            finalResult.getChildren().addAll(text1);
        }
        usersData.setDisable(false);
    }

    /**
     * Method that normalized data
     * @param input array of integers that holds one sample of input data
     * @return array of doubles with normalized data
     */
    private double[] normalizeData(double[] input){
        double[] normData = new double[input.length];
        normData[0] = input[0]/100; //normalize temperature
        normData[1] = input[1]/20;  //normalize wind speed
        normData[2] = input[2];     //outlook
        normData[3] = input[3];     //precipitation
        return normData;
    }

    /**
     * Event handler for the "Start testing" button.
     */
    @FXML
    protected void onTestingBtn() {
        // check if all testing data samples processed
        if(sampleNumber < testingData.length) {
            usersData.setDisable(true);
            // reset TextFlow
            if(sampleNumber == 0){
                finalResult.getChildren().setAll(new Text("Result:"));
                finalResult.setVisible(false);
            }
            // find predicted output
            double[] normTestingData = normalizeData(testingData[sampleNumber]);
            int prediction = perceptron.predict(normTestingData);
            if(prediction == expected[sampleNumber]){
                // increment the number of correct predictions
                correct++;
            }

            // display the result of testing
            resultLbl.setText("Sample " + (sampleNumber + 1) + ": " + Arrays.toString(testingData[sampleNumber]) + "\n" +
                    "Prediction:\t" + (prediction == 1 ? "Delayed" : "Not delayed"));

            // add the result of the sample testing to the finalResult TextFlow
            String formattedText = String.format("\n\nSample %d: %s \tPrediction:\t", (sampleNumber + 1), Arrays.toString(testingData[sampleNumber]));
            Text text1=new Text(formattedText);
            Text text2=new Text(prediction == 1 ? "Delayed" : "Not delayed");
            text2.setStyle("-fx-font-weight: bold");
            if(prediction != expected[sampleNumber]){
                text2.setFill(Color.RED); // setting color of the text to red
            }
            finalResult.getChildren().addAll(text1, text2);
            sampleNumber++; // move to the next sample data
        }else{
            // if testing completes, calculate accuracy of predictions, and display the results
            accuracy = 100.0f*correct/expected.length;
            Text text=new Text("\n\nAccuracy is: " + accuracy + "%.");
            text.setStyle("-fx-font-weight: bold");
            finalResult.getChildren().addAll(text);
            finalResult.setVisible(true);
            resultLbl.setText("Test completed");
            startTestingBtn.setDisable(true);
            usersData.setDisable(false);
        }
        // change the text on the button if testing is already started
        if(startTestingBtn.getText().equalsIgnoreCase("Start testing")){
            startTestingBtn.setText("Next testing data");
        }
    }


    /**
     * Event handler for the "Exit" button. Terminates the program after confirmation
     */
    @FXML
    protected void onExitBtn() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            System.exit(0);
        }
    }

    /**
     * Event handler for the "Predict" button.
     */
    @FXML
    protected void onPredictBtn() {
        try{
            finalResult.setVisible(false);
            double[] inputData = new double[4];
            inputData[0] = Double.parseDouble(temperatureTextField.getText());
            inputData[1] = Double.parseDouble(windSpeedTextField.getText());
            inputData[2] = Double.parseDouble(outlookTextField.getText());
            inputData[3] = Double.parseDouble(precipitationTextField.getText());
            if((inputData[2] != -1 && inputData[2] != 0 && inputData[2] != 1) ||
                    (inputData[3] != -1 && inputData[3] != 1) || inputData[1] < 0 ){
                showAlert();
                temperatureTextField.setText("");
                windSpeedTextField.setText("");
                outlookTextField.setText("");
                precipitationTextField.setText("");
            }
            double[] normData = normalizeData(inputData);
            int prediction = perceptron.predict(normData);
            // display the result of testing
            resultLbl.setText("Sample : " + Arrays.toString(inputData) + "\n" +
                    "Prediction:\t" + (prediction == 1 ? "Delayed" : "Not delayed"));
            resultLbl.setStyle("-fx-font-weight: bold");

        }catch (Exception e){
            showAlert();
        }
        finally {
            temperatureTextField.setText("");
            windSpeedTextField.setText("");
            outlookTextField.setText("");
            precipitationTextField.setText("");
        }
    }

    /**
     * Method that informs user about invalid input
     */
    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Input invalid");
        alert.showAndWait();
    }
}