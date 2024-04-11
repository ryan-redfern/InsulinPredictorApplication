package uk.ac.stir.cs.insulinpredictorapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GetPredictionFragment extends Fragment {
    DBHelper DB;
    OLSMultipleLinearRegression regression;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_getprediction, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        EditText etCurrentBloodSugar = view.findViewById(R.id.etCurrentBloodSugar);
        EditText etCarbohydrates = view.findViewById(R.id.etCarbohydrates);
        TextView tvInsulin = view.findViewById(R.id.tvPrediction);
        TextView tvPredIns = view.findViewById(R.id.tvPredins);
        TextView tvUnits = view.findViewById(R.id.tvUnits);
        Button btnGetPrediction = view.findViewById(R.id.btnGetPrediction);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        List<InputModel> data = DB.getData();
        List<SettingsModel> settings = DB.getSettings();
        double[] y = new double[data.size()];
        double[][] x = new double[data.size()][];

        for (int i = 0; i < data.size(); i++) { // For the size of the data
            y[i] = data.get(i).getInsulin();  // Sets y equal to each insulin prediction
            x[i] = new double[]{data.get(i).getbsBefore(), data.get(i).getCarbohydrates()}; // Sets x equal to each blood sugar before and carbohydrate prediction
        }
        regression = buildModel(y, x);
        Toast.makeText(getActivity(), "Model Created - Predictions Available", Toast.LENGTH_SHORT).show();

        btnGetPrediction.setOnClickListener(view1 -> {
                String bsBeforeText = etCurrentBloodSugar.getText().toString();
                if (bsBeforeText.matches("")) {
                    Toast.makeText(getActivity(), "You did not enter a blood sugar before value", Toast.LENGTH_SHORT).show();
                    return;
                }
                double bsBefore = Double.parseDouble(bsBeforeText);
                String carboText = etCarbohydrates.getText().toString();
                if (carboText.matches("")) {
                    Toast.makeText(getActivity(), "You did not enter a carbohydrate value", Toast.LENGTH_SHORT).show();
                    return;
                }
                int carbohydrates = Integer.parseInt(carboText);
                if (bsBefore < settings.get(0).getHypo()){ // Used to warn user if their blood sugar is in the hypo range
                    tvInsulin.setText("Blood Sugar is Low - Eat 15g of Carbohydrates");
                    Toast.makeText(getActivity(), "Blood Sugar is Low - Eat 15g of Carbohydrates", Toast.LENGTH_SHORT).show();
                } else{
                    if (bsBefore > settings.get(0).getHyper()){ // Used to warn user if their blood sugar is in the hyper range
                        Toast.makeText(getActivity(), "Blood Sugar is High - Inject Insulin and go for a walk & drink water", Toast.LENGTH_SHORT).show();
                    }

                    double[] predictionData = new double[]{bsBefore, carbohydrates};
                    double prediction = predict(regression, predictionData);

                    if (prediction >= 0){
                        float prediction_rounded = (float)Math.round(prediction); //Rounds prediction to a whole number
                        tvInsulin.setText(Double.toString(prediction_rounded));
                        tvUnits.setVisibility(View.VISIBLE);
                        tvPredIns.setVisibility(View.VISIBLE);


                    }
                    DB.InsertPredictions(currentDate, bsBefore,carbohydrates,prediction);
                }



        });
    }

    /**
     * Method to generate a new regression model
     * Takes in the y and the x variable being insulin and blood sugar before + carbohydrates
     */
    public OLSMultipleLinearRegression buildModel(double[] y, double[][] x) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(y, x); // Performs the regression algorithm on the data from the OLSMultipleLinearRegression library
        return regression;
    }
    /**
     * The predict method takes in the regression model and the double x containing the blood sugar before and carbohydrates
     * outputs a prediction
     */
    public double predict(OLSMultipleLinearRegression regression, double[] x) {
        if (regression == null) {
            throw new IllegalArgumentException("regression must not be null.");
        }
        double[] beta = regression.estimateRegressionParameters();
        // intercept at beta[0]
        double prediction = beta[0];
        for (int i = 1; i < beta.length; i++) {
            prediction += beta[i] * x[i - 1];
        }

        return prediction;
    }
}
