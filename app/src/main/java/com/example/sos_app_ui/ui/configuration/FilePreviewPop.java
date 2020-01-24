package com.example.sos_app_ui.ui.configuration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.sos_app_ui.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FilePreviewPop extends Activity {

    private TextView fileContent;
    private TextView fctFileName;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview_pop);

        fileContent=findViewById(R.id.fileContent);
        fctFileName=findViewById(R.id.fctFileName);

        fileContent.setMovementMethod(new ScrollingMovementMethod());
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        //this.setOutsideTouchable(true);
        showFileContent();
    }

    public void showFileContent()
    {
        Intent i = this.getIntent();
        Bundle args = i.getExtras();
        String path = args.get("fpath").toString();
        file = new File(path);

        fctFileName.setText(file.getName());

        try
        {
            BufferedReader brTest = new BufferedReader(new FileReader(file));
            String test = brTest.readLine();
            String content = "";

            while(test != null)
            {
                content += test;
                content += "\n\n";
                test = brTest.readLine();
            }

            fileContent.setText(content);
            brTest.close();
        }
        catch(IOException e)
        {
            e.getMessage();
        }

    }
}