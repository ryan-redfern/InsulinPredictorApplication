package uk.ac.stir.cs.insulinpredictorapplication;

import androidx.annotation.NonNull;
/**
 * Class to store the objects of the predictions
 */
public class PredictionModel {
    public int id;
    public String date;
    public double bsBefore;
    public int carbohydrates;
    public double prediction;
    public double bsAfter;
    public PredictionModel(int id, String date, double bsBefore, int carbohydrates, double prediction, double bsAfter){
        this.id = id;
        this.date = date;
        this.bsBefore = bsBefore;
        this.carbohydrates = carbohydrates;
        this.prediction = prediction;
        this.bsAfter = bsAfter;



    }
    public PredictionModel(int id, String date, double bsBefore, int carbohydrates, double prediction){
        this.id = id;
        this.date = date;
        this.bsBefore = bsBefore;
        this.carbohydrates = carbohydrates;
        this.prediction = prediction;




    }
    public int getID(){

        return id;
    }
    public String getDate(){
        return date;
    }
    public double getBSBefore(){
        return bsBefore;

    }
    public int getCarbohydrates(){
        return carbohydrates;

    }
    public int getPrediction(){
        return (int)prediction;

    }
    public double getBsAfter(){
        return bsAfter;
    }

    @NonNull
    @Override
    public String toString() {
        return
                " ID = " + id +
                        "\n Date = " + date +
                        "\n Blood Sugar Before = " + bsBefore + " mmol/L"+
                        "\n Carbohydrates Eaten = " + carbohydrates + " grams"+
                        "\n Prediction = " + prediction + " units" +
                        "\n Blood Sugar After = " + bsAfter + " mmol/L";
    }
}
