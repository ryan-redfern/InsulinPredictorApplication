package uk.ac.stir.cs.insulinpredictorapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.List;



public class AddFragment extends Fragment {
    DBHelper DB;
    List<InputModel> data = new ArrayList<>();
    List<SettingsModel> settings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        EditText etBloodSugarBefore = view.findViewById(R.id.etBloodSugarBefore);
        EditText etCarbohydrates = view.findViewById(R.id.etCarbohydrates);
        EditText etInsulin = view.findViewById(R.id.etInsulin);
        EditText etBloodSugarAfter = view.findViewById(R.id.etBloodSugarAfter);
        Button btnAddToDatabase = view.findViewById(R.id.btnAddToDatabase);
        Button btnClear = view.findViewById(R.id.btnClear);
        //When the save button is pressed
        btnAddToDatabase.setOnClickListener(view1 -> {
            data = getData(DB);
            settings = getSettings(DB);
            String bsBeforeText = etBloodSugarBefore.getText().toString();
            //Checks whether data has been entered in each field
            if (bsBeforeText.matches("")) {
                Toast.makeText(getActivity(), "You did not enter a BS before value", Toast.LENGTH_SHORT).show();
                return;
            }
            double bsBefore = Double.parseDouble(bsBeforeText);
            String bsAfterText = etBloodSugarAfter.getText().toString();
            if (bsAfterText.matches("")) {
                Toast.makeText(getActivity(), "You did not enter a BS after value", Toast.LENGTH_SHORT).show();
                return;
            }
            double bsAfter = Double.parseDouble(bsAfterText);
            String carboText  = etCarbohydrates.getText().toString();
            if (carboText.matches("")) {
                Toast.makeText(getActivity(), "You did not enter a carbohydrate value", Toast.LENGTH_SHORT).show();
                return;
            }
            int carbohydrates = Integer.parseInt(carboText);
            String insulinText = etInsulin.getText().toString();
            if (insulinText.matches("")) {
                Toast.makeText(getActivity(), "You did not enter a insulin value", Toast.LENGTH_SHORT).show();
                return;
            }

            int insulin = Integer.parseInt(insulinText);
            if ((settings.get(0).getHighBS() <bsAfter) || (settings.get(0).getLowBS() > bsAfter)) { // Checks whether blood sugar after is within range
                Toast.makeText(getActivity(), "Blood Sugar After Must Be Within Glucose Range", Toast.LENGTH_SHORT).show();
            } else {
                Boolean insertDataBoolean = DB.InsertData(bsBefore, carbohydrates, insulin); // Adds data to the database
                if (insertDataBoolean){
                    etBloodSugarBefore.setText("");
                    etCarbohydrates.setText("");
                    etInsulin.setText("");
                    etBloodSugarAfter.setText("");
                    data = getData(DB);
                    if (data.size() > 50) {
                        requireActivity().setTitle("PIA - Prediction Ready Mode");
                    } else {
                        requireActivity().setTitle("PIA - Data Entry Mode");
                    }
                    Toast.makeText(getActivity(), "New Entry Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "New Entry Did not work", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnClear.setOnClickListener(view12 -> {
            etBloodSugarBefore.setText("");
            etCarbohydrates.setText("");
            etInsulin.setText("");
            etBloodSugarAfter.setText("");


    });
    }
    /**
     * Calls getData from DBhelper class
     * returns the list of data entries
     */
    public List<InputModel> getData(DBHelper DB){
        return DB.getData();
    }
    /**
     * Calls getSettings from DBhelper class
     * returns the list of settings
     */
    public List<SettingsModel> getSettings(DBHelper DB){
        return DB.getSettings();
    }



}
