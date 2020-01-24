package com.example.sos_app_ui.ui.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sos_app_ui.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConfigFileChecker extends AppCompatActivity {

       private ListView fileList;
    private List<MyFiles> listOfFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_file_checker);

        fileList = findViewById(R.id.configFilesList);
        listOfFiles =  loadFiles();

        Adapter_for_My_Files adapter = new Adapter_for_My_Files(this,listOfFiles);
        fileList.setAdapter(adapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                Intent resultIntent = new Intent(view.getContext(),
                        ConfigurationFragment.class);

                args.putString("fname",listOfFiles.get(i).filename);

                resultIntent.putExtras(args);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }

    public ListView getFileList() {
        return fileList;
    }

    public void setFileList(ListView fileList) {
        this.fileList = fileList;
    }

    public List<MyFiles> getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(List<MyFiles> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public List<MyFiles> loadFiles()
    {
        List<MyFiles> list = new LinkedList<>();
        String path = this.getExternalFilesDir("Configurations").toString();
        System.out.println(path);
        File directory = new File(path);

        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            String date = "Created: ";

            try
            {
                System.out.println("Current:"+ files[i].getName());
                if(files[i].getName().equals("History.txt"))
                    continue;
                BufferedReader brTest = new BufferedReader(new FileReader(files[i]));
                String test = brTest.readLine();
                //System.out.println(test);

                String[] strArray = test.split(" ");
                date += strArray[3];
                brTest.close();
            }
            catch(IOException e)
            {
                e.getMessage();
            }
            list.add(new MyFiles(files[i].getName(),date));
        }




        return list;
    }


    private class MyFiles
    {
        private String filename;
        private String createDate;

        MyFiles(String name, String date)
        {
            this.filename=name;
            this.createDate=date;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }

    private class Adapter_for_My_Files extends BaseAdapter {
        Context mContext;
        List<MyFiles> mList_MyFiles;

        public Adapter_for_My_Files(Context mContext, List<MyFiles> mContact) {
            this.mContext = mContext;
            this.mList_MyFiles = mContact;
        }

        @Override
        public int getCount() {
            return mList_MyFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return mList_MyFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.textfile_row, null);

            TextView textview_file_name = view.findViewById(R.id.fileName);
            TextView textview_file_date = view.findViewById(R.id.fileDate);

            textview_file_name.setText(mList_MyFiles.get(position).filename);
            textview_file_date.setText(mList_MyFiles.get(position).createDate);


            view.setTag(mList_MyFiles.get(position).filename);
            return view;
        }
    }
}