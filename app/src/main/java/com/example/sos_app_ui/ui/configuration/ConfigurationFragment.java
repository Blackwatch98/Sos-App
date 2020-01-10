package com.example.sos_app_ui.ui.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.sos_app_ui.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigurationFragment extends Fragment {

    private ConfigurationViewModel homeViewModel;
    private ListView list;
    private TextView path;
    private Button previewBtn;
    private String name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ConfigurationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuration, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        name = null;
        list = (ListView) root.findViewById(R.id.listView1);
        path = root.findViewById(R.id.finalFilePath);

        previewBtn = root.findViewById(R.id.previewBtn);
       // previewBtn.setEnabled(false);

        previewBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(name != null)
                  {
                      Intent appInfo = new Intent(getContext(), FilePreviewPop.class);
                      appInfo.putExtra("fpath", name);
                      startActivity(appInfo);
                  }
                  else
                  {
                      Toast.makeText(v.getContext(), "You did not choose any file!",
                              Toast.LENGTH_SHORT).show();
                  }
              }
        });

        String functions[] = {"Create New","Load Created"};

        ArrayList<String> funList = new ArrayList<String>();
        funList.addAll( Arrays.asList(functions) );

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.row, funList);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                if(position == 0)
                {
                    Intent appInfo = new Intent(view.getContext(),CreateNewConfiguration.class);
                    startActivity(appInfo);
                }
                if(position == 1)
                {
                    Intent appInfo = new Intent(view.getContext(),ConfigFileChecker.class);
                    startActivityForResult(appInfo, 2);
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2)
        {
            Bundle extras = data.getExtras();

            name = getContext().getExternalFilesDir("Configurations").toString();
            name = name +'/'+ extras.get("fname").toString();
            path.setText(name);
        }
    }

}