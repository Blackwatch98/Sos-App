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

import com.example.sos_app_ui.MainActivity;
import com.example.sos_app_ui.R;
import com.example.sos_app_ui.ui.current_activity.CurrentActivityFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ConfigurationFragment extends Fragment {

    public CurrentConfiguration getWorkingConf() {
        return workingConf;
    }

    private CurrentConfiguration workingConf;
    private ConfigurationViewModel homeViewModel;
    private ListView list;
    private TextView pathView;
    private Button previewBtn;
    private Button confirmBtn;

    private String fileName;
    private String pathName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(ConfigurationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuration, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        list = (ListView) root.findViewById(R.id.listView1);
        pathView = root.findViewById(R.id.finalFilePath);


        previewBtn = root.findViewById(R.id.previewBtn);
        confirmBtn = root.findViewById(R.id.finalConfirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateHistoryFile();
                    Toast.makeText(v.getContext(), "You confirmed you choice successfully :)",
                            Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity().getBaseContext(),MainActivity.class);
                i.putExtra("FinalConfig", workingConf);
                getActivity().startActivity(i);
                CurrentActivityFragment.buttonActiveFlag = true;
            }
        });

        this.workingConf = new CurrentConfiguration();
        if(checkIfAnyFile())
        {
            String lastConfigFilePath = this.getContext().getExternalFilesDir("Configurations").toString();
            lastConfigFilePath += "/" + getLastFile();
            workingConf.getDataFromConfigFile(lastConfigFilePath,this.getContext());


            pathView.setText(lastConfigFilePath);
        }

        previewBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(pathName != null)
                  {
                      Intent appInfo = new Intent(getContext(), FilePreviewPop.class);
                      appInfo.putExtra("fpath", pathName);
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
                    startActivityForResult(appInfo,1);
                }
                if(position == 1)
                {
                    if(checkIfAnyFile())
                    {
                        Intent appInfo = new Intent(view.getContext(),ConfigFileChecker.class);
                        startActivityForResult(appInfo, 2);
                    }
                    else
                    {
                        Toast.makeText(view.getContext(), "You have no files to load!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

    public boolean checkIfAnyFile()
    {
        String path = this.getContext().getExternalFilesDir("Configurations").toString();
        File directory = new File(path);
        int size = 0;

        File[] files = directory.listFiles();

        for(File f : files)
            if(f.getName().contains("Config"))
                size++;

        if(size != 0)
            return true;
        else
            return false;
    }

    public void updateHistoryFile()
    {
        boolean isHisFile = false;

        File file = new File(this.getContext().getExternalFilesDir("Configurations"), "History.txt");

        try
        {
            if(file.exists())
            {
                FileWriter fileWriter = new FileWriter(file);
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                fileWriter.write(fileName + " ");
                fileWriter.write(formatter.format(date) + '\n');
                fileWriter.close();
            }
            else
            {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                fileWriter.write(fileName + " ");
                fileWriter.write(formatter.format(date) + '\n');
                fileWriter.close();
            }
        }
        catch(IOException e)
        {
            e.getMessage();
        }
    }

    public String getLastFile()
    {
        File file = new File(this.getContext().getExternalFilesDir("Configurations"), "History.txt");
        String str = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            str = br.readLine();

        }
        catch(IOException e)
        {
            e.getMessage();
        }
        String[] words = str.split(" ");
        return words[0];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(data != null)
            {
                Bundle extras = data.getExtras();
                this.workingConf = (CurrentConfiguration) extras.get("fullyConfig");

                fileName = (String)extras.get("fileName");
                pathName = getContext().getExternalFilesDir("Configurations").toString();
                pathName = pathName +'/'+ fileName;
                pathView.setText(pathName);

                //System.out.println("-------------------CURRENT WORKING CONFIG-------------------");
                //this.workingConf.display();
            }

        }

        if(requestCode == 2)
        {
            if(data != null)
            {
                Bundle extras = data.getExtras();

                pathName = getContext().getExternalFilesDir("Configurations").toString();
                fileName = extras.get("fname").toString();

                pathName = pathName +'/'+ fileName;
                pathView.setText(pathName);
                this.workingConf.getDataFromConfigFile(pathName,this.getContext());
            }

        }
    }

}