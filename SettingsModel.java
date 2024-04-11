package uk.ac.stir.cs.insulinpredictorapplication;

import androidx.annotation.NonNull;
/**
 * Class to store the object of the settings
 */
public class SettingsModel {

    public double Hyper;
    public double HighBS;
    public double LowBS;
    public double Hypo;
    public SettingsModel(double Hyper, double HighBS, double LowBS, double Hypo){
        this.Hyper = Hyper;
        this.HighBS = HighBS;
        this.LowBS = LowBS;
        this.Hypo = Hypo;
    }
    public double getHyper(){
        return Hyper;
    }
    public double getHighBS(){
        return HighBS;
    }
    public double getLowBS(){
        return LowBS;
    }
    public double getHypo(){
        return Hypo;
    }


    @NonNull
    @Override
    public String toString() {
        return
                " Hyper = " + Hyper +
                        "\n High = " + HighBS +
                        "\n Low = " + LowBS +
                        "\n Hypo = " + Hypo;
    }
}
