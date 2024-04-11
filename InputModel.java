package uk.ac.stir.cs.insulinpredictorapplication;

import androidx.annotation.NonNull;
/**
 * Class to store the objects of data inputted by the user
 */
public class InputModel {
    public int id;
    public int insulin;
    public double bsBefore;
    public int carbohydrates;
    public InputModel(int id,double bsBefore , int carbohydrates, int insulin){
        this.id = id;
        this.insulin = insulin;
        this.bsBefore = bsBefore;
        this.carbohydrates = carbohydrates;
    }
    public int getID(){
        return id;
    }
    public int getInsulin(){
        return insulin;
    }
    public int getCarbohydrates(){
        return carbohydrates;
    }
    public double getbsBefore(){
        return bsBefore;
    }

    @NonNull
    @Override
    public String toString() {
        return
                " ID = " + id +
                "\n Insulin = " + insulin+ " units"+
                "\n Blood Sugar Before = " + bsBefore + " mmol/L"+
                "\n Carbohydrates Eaten = " + carbohydrates+" grams";
    }
}
