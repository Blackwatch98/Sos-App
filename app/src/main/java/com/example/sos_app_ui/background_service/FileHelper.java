package com.example.sos_app_ui.background_service;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileHelper {
    private File file;

    FileHelper(String fileName, Context context){
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            System.out.println("ext not available");
        }
        else {
            context.getExternalFilesDir("sosAppTest");
            file = new File(context.getExternalFilesDir("sosAppTest"), fileName);
        }
    }

    public boolean writeToFile(String text){
        try{
            file.createNewFile();
            if(file.exists()) {
                //FileOutputStream fos = new FileOutputStream(file);
                //fos.write(text.getBytes());
                //fos.close();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(text);
                fileWriter.close();
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
                fileWriter.close();
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

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
