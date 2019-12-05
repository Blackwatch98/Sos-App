package com.example.sos_app_ui.ui.current_activity;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileHelper {
    private String fileName;
    private File file;

    FileHelper(String fileName){
        file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator +"/sosAppTest/"+ fileName);
    }

    public boolean writeToFile(String text){
        try{
            file.createNewFile();
            if(file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(text);
            }else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean writeToFile(List list){
        try{
            file.createNewFile();
            if(file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(String.valueOf(list));
            }else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getPath(){
        return file.getPath();
    }
}
