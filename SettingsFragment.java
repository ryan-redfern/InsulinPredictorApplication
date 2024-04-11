package uk.ac.stir.cs.insulinpredictorapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.List;

public class SettingsFragment extends Fragment {
    private AlertDialog dialog;
    DBHelper DB;
    TextView tvHyperVal,tvHighBSVal,tvLowBSVal,tvHypoVal;
    List<SettingsModel> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView tvHyper = view.findViewById(R.id.tvHyper);
        TextView tvHighBS = view.findViewById(R.id.tvHighBS);
        TextView tvLowBS = view.findViewById(R.id.tvLowBS);
        TextView tvHypo = view.findViewById(R.id.tvHypo);
        tvHyperVal = view.findViewById(R.id.tvHyperVal);
        tvHighBSVal =  view.findViewById(R.id.tvHighBSVal);
        tvLowBSVal = view.findViewById(R.id.tvLowBSVal);
        tvHypoVal =  view.findViewById(R.id.tvHypoVal);
        Button btnResetSettings = view.findViewById(R.id.btnResetSettings);
        Button btnResetData = view.findViewById(R.id.btnResetData);
        Button btnResetPredictions = view.findViewById(R.id.btnResetPredictions);
        DB = new DBHelper(getContext());
        data = DB.getSettings();
        tvHyperVal.setText(Double.toString(data.get(0).getHyper()));
        tvHighBSVal.setText(Double.toString(data.get(0).getHighBS()));
        tvLowBSVal.setText(Double.toString(data.get(0).getLowBS()));
        tvHypoVal.setText(Double.toString(data.get(0).getHypo()));


        tvHyper.setOnClickListener(view1 -> createNewDialogPopup(tvHyper.getText().toString(), tvHyperVal.getText().toString()));
        tvHighBS.setOnClickListener(view12 -> createNewDialogPopup(tvHighBS.getText().toString(),  tvHighBSVal.getText().toString()));
        tvLowBS.setOnClickListener(view13 -> createNewDialogPopup(tvLowBS.getText().toString(),  tvLowBSVal.getText().toString()));
        tvHypo.setOnClickListener(view14 -> createNewDialogPopup(tvHypo.getText().toString(),  tvHypoVal.getText().toString()));
        tvHyperVal.setOnClickListener(view15 -> createNewDialogPopup(tvHyper.getText().toString(),  tvHyperVal.getText().toString()));
        tvHighBSVal.setOnClickListener(view16 -> createNewDialogPopup(tvHighBS.getText().toString(),  tvHighBSVal.getText().toString()));
        tvLowBSVal.setOnClickListener(view17 -> createNewDialogPopup(tvLowBS.getText().toString(),  tvLowBSVal.getText().toString()));
        tvHypoVal.setOnClickListener(view18 -> createNewDialogPopup(tvHypo.getText().toString(),  tvHypoVal.getText().toString()));
        btnResetSettings.setOnClickListener(view19 -> confirmDialog(1));
        btnResetData.setOnClickListener(view110 -> confirmDialog(2));
        btnResetPredictions.setOnClickListener(view111 -> confirmDialog(3));

    }
    /**
     * Creates a new dialog popup box
     * Contains multiple statements to check whether the input is valid
     */
    public void createNewDialogPopup(String valueText, String valText){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View bloodsugarPopupView = getLayoutInflater().inflate(R.layout.popup_bloodsugarrange, null);
        TextView popup = bloodsugarPopupView.findViewById(R.id.tvPopup);
        popup.setText(valueText);
        EditText etPopupvalue = bloodsugarPopupView.findViewById(R.id.etPopupvalue);
        Button btnCancel = bloodsugarPopupView.findViewById(R.id.btnCancel);
        Button btnSubmit = bloodsugarPopupView.findViewById(R.id.btnSubmit);
        dialogBuilder.setView(bloodsugarPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        etPopupvalue.setHint(valText);
        btnCancel.setOnClickListener(view ->
                dialog.cancel());
        btnSubmit.setOnClickListener(view -> {
            String newValue = etPopupvalue.getText().toString();
            double value = Double.parseDouble(newValue);
            DB = new DBHelper(getContext());
            if (valueText.contains("Hyper")){

                if (value > data.get(0).getHighBS()) {
                    DB.updateSettings(value, data.get(0).getHighBS(), data.get(0).getLowBS(), data.get(0).getHypo());
                    tvHyperVal.setText(Double.toString(value));
                    dialog.cancel();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Hyper Value must be higher than " + data.get(0).getHighBS() , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } else if (valueText.contains("High")){

                if ((value < data.get(0).getHyper() && value > data.get(0).getLowBS())) {
                    DB.updateSettings(data.get(0).getHyper(), value, data.get(0).getLowBS(), data.get(0).getHypo());
                    tvHighBSVal.setText(Double.toString(value));
                    dialog.cancel();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "High Value must be higher than " + data.get(0).getLowBS() + " and lower than " + data.get(0).getHyper() , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
            } else if (valueText.contains("Low")){

                if ((value < data.get(0).getHighBS()) && ( value > data.get(0).getHypo())) {
                    DB.updateSettings(data.get(0).getHyper(), data.get(0).getHighBS(), value, data.get(0).getHypo());
                    tvLowBSVal.setText(Double.toString(value));
                    dialog.cancel();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Low Value must be higher than " + data.get(0).getHypo() + " and lower than " + data.get(0).getHighBS() , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
            } else {

                if (value < data.get(0).getLowBS()) {

                    DB.updateSettings(data.get(0).getHyper(), data.get(0).getHighBS(), data.get(0).getLowBS(), value);
                    tvHypoVal.setText(Double.toString(value));
                    dialog.cancel();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Hypo Value must be lower than " + data.get(0).getLowBS(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
            }
        });

    }
    /**
     * Method to check whether the user actually wants to continue with their choice
     */
    public void confirmDialog(int value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Delete?");
        switch (value) {
            case 1:
                builder.setMessage("Are you sure you want to reset the glucose range?" );
                break;
            case 2:
                builder.setMessage("Are you sure you want to delete all data?" );
                break;
            case 3:
                builder.setMessage("Are you sure you want to delete all predictions?" );
                break;

        }

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (value == 1){
                DB.updateSettings(7.1,7,4,3.9);
                tvHyperVal.setText("7.1");
                tvHighBSVal.setText("7");
                tvLowBSVal.setText("4");
                tvHypoVal.setText("3.9");
                Toast.makeText(getActivity(), "Glucose Range Reset", Toast.LENGTH_SHORT).show();

            } else if (value == 2){
                DB.deleteAllData();
                Toast.makeText(getActivity(), "All Data Deleted", Toast.LENGTH_SHORT).show();
            } else {
                DB.deleteAllPredictions();
                Toast.makeText(getActivity(), "All Predictions Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });
        dialog = builder.create();
        dialog.show();
    }
}

