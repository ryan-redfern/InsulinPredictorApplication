package uk.ac.stir.cs.insulinpredictorapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreviousPredictionsFragment extends Fragment {
    DBHelper DB;
    List<PredictionModel> predictions = new ArrayList<>();
    List<PredictionModel> search = new ArrayList<>();
    List<SettingsModel> settings = new ArrayList<>();
    ArrayAdapter<PredictionModel> saveAdapter;
    ImageView ivEmpty;
    TextView tvImage;
    Button btnSearchByDate,btnResetSearch;
    EditText etSearchByDate;
    ListView lvPredictions;
    private AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_previouspredictions, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        settings = getSettings(DB);
        lvPredictions = view.findViewById(R.id.lvPredictions);
        ivEmpty = view.findViewById(R.id.ivEmpty);
        tvImage = view.findViewById(R.id.tvImage);
        btnSearchByDate = view.findViewById(R.id.btnSearchByDate);
        btnResetSearch = view.findViewById(R.id.btnResetSearch);
        etSearchByDate = view.findViewById(R.id.etSearchByDate);


        predictions = getDataForFragment(DB);
        if (predictions.isEmpty()){
            ivEmpty.setVisibility((View.VISIBLE));
            tvImage.setVisibility((View.VISIBLE));
            btnSearchByDate.setVisibility(View.INVISIBLE);
            btnResetSearch.setVisibility(View.INVISIBLE);
            etSearchByDate.setVisibility(View.INVISIBLE);

        }

        saveAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, predictions);
        lvPredictions.setAdapter(saveAdapter);
        registerForContextMenu(lvPredictions);
        etSearchByDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");
                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        String ddmmyyyy = "DDMMYYYY";
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }
                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));
                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etSearchByDate.setText(current);
                    etSearchByDate.setSelection(sel < current.length() ? sel : current.length());

                }
            }


                @Override
                public void afterTextChanged (Editable editable){

                }
            });
        btnSearchByDate.setOnClickListener(view1 -> {




            search = searchPredictions(DB, etSearchByDate.getText().toString());
            saveAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, search);
            lvPredictions.setAdapter(saveAdapter);
            registerForContextMenu(lvPredictions);


        });
        btnResetSearch.setOnClickListener(view12 -> {


            saveAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, predictions);
            lvPredictions.setAdapter(saveAdapter);
            registerForContextMenu(lvPredictions);


        });



    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.previouspredpopupmenu, menu);
    }
    /**
     * When an item is selected, either delete or add the item
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.itemDel:
                saveAdapter.notifyDataSetChanged();
                DB.deletePredictions(predictions.get(info.position).getID());
                predictions.remove(info.position);
                return true;
            case R.id.itemAdd:
                createNewDialogPopup(item);
                return true;
            default:
        }
        return super.onContextItemSelected(item);
    }

    public void createNewDialogPopup(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        final View bloodsugarPopupView = getLayoutInflater().inflate(R.layout.popup_bloodsugarafter, null);
        Button btnCancel = bloodsugarPopupView.findViewById(R.id.btnCancel);
        EditText etPopupvalue = bloodsugarPopupView.findViewById(R.id.etPopupvalue);
        Button btnSubmit = bloodsugarPopupView.findViewById(R.id.btnSubmit);
        dialogBuilder.setView(bloodsugarPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        btnCancel.setOnClickListener(view ->
                dialog.cancel());
        AdapterView.AdapterContextMenuInfo finalInfo = info;
        btnSubmit.setOnClickListener(view -> {
            String newValue = etPopupvalue.getText().toString();
            System.out.println(newValue);
            Double value = Double.parseDouble(newValue);
            System.out.println(value);
            DB = new DBHelper(getContext());
            int id = predictions.get(finalInfo.position).getID();
            String idString = Integer.toString(id);
            if ((settings.get(0).getHighBS() < value) || (settings.get(0).getLowBS() > value)) {
                DB.InsertBSAfter(predictions.get(finalInfo.position).getDate(), predictions.get(finalInfo.position).getBSBefore(), predictions.get(finalInfo.position).getCarbohydrates(), predictions.get(finalInfo.position).getPrediction(),
                        value, idString);
                Toast.makeText(getActivity(), "Blood Sugar Added - Not Added to logbook", Toast.LENGTH_SHORT).show();
            } else {
                DB.InsertData(predictions.get(finalInfo.position).getBSBefore(), predictions.get(finalInfo.position).getCarbohydrates(), predictions.get(finalInfo.position).getPrediction());


                DB.InsertBSAfter(predictions.get(finalInfo.position).getDate(), predictions.get(finalInfo.position).getBSBefore(), predictions.get(finalInfo.position).getCarbohydrates(), predictions.get(finalInfo.position).getPrediction(),
                        value, idString);



                Toast.makeText(getActivity(), "Blood Sugar After Added - Added to logbook", Toast.LENGTH_SHORT).show();
            }
            predictions = getDataForFragment(DB);
            saveAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, predictions);
            lvPredictions.setAdapter(saveAdapter);


            dialog.cancel();

            }
        );
    }
    public List<PredictionModel> getDataForFragment(DBHelper DB){
        return DB.getPredictions();
    }
    public List<PredictionModel> searchPredictions(DBHelper DB, String searchDate){
        return DB.searchPredictionsByDate(searchDate);
    }
    public List<SettingsModel> getSettings(DBHelper DB){
        return DB.getSettings();
    }



}
