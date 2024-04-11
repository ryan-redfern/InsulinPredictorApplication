package uk.ac.stir.cs.insulinpredictorapplication;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class LogbookFragment extends Fragment {
    DBHelper DB;
    List<InputModel> data = new ArrayList<>();
    ArrayAdapter<InputModel> saveAdapter;
    ImageView ivEmpty;
    TextView tvImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logbook, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        ListView lvData = view.findViewById(R.id.lvData);
        data = getData(DB);
        ivEmpty = view.findViewById(R.id.ivEmpty);
        tvImage = view.findViewById(R.id.tvImage);
        if (data.isEmpty()){
            ivEmpty.setVisibility((View.VISIBLE));
            tvImage.setVisibility((View.VISIBLE));

        }
        saveAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        lvData.setAdapter(saveAdapter);
        registerForContextMenu(lvData);

}

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.logbookpopupmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.itemDel:
                saveAdapter.notifyDataSetChanged();
                DB.deleteData(data.get(info.position).getID());
                data.remove(info.position);
                if (data.size() > 50) {
                    getActivity().setTitle("PIA - Prediction Ready Mode");
                    Toast.makeText(getActivity(), "Data is sufficient", Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().setTitle("PIA - Data Entry Mode");
                }
                return true;
            case R.id.itemAdd:

            default:
        }
        return super.onContextItemSelected(item);
    }
    public List<InputModel> getData(DBHelper DB){
        return DB.getData();
    }



}
