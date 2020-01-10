package com.example.sos_app_ui.ui.configuration;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CurrentConfiguration
{
    private String fName;
    private String sName;
    private int age;

    private String messageText;

    private List<Android_Contact> targets;

    private File file;

    CurrentConfiguration()
    {
        this.fName="None";
        this.sName="None";
        this.age= -1;
        this.messageText="None";
        targets = null;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public List<Android_Contact> getTargets() {
        return targets;
    }

    public void setTargets(List<Android_Contact> targets) {
        this.targets = targets;
    }

    public boolean writeConfigToFile(String fileName, CurrentConfiguration data, Context context)
    {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            System.out.println("ext not available");
        }
        else {
            context.getExternalFilesDir("Configurations");
            file = new File(context.getExternalFilesDir("Configurations"), fileName);
        }


        try{
            file.createNewFile();
            if(file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(data.toString());
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

    public void display()
    {
        String classContent = "";
        classContent = classContent + "First Name: " + this.fName + '\n';
        classContent = classContent + "Second Name: " + this.sName + '\n';
        classContent = classContent + "Age: " + this.age + '\n';
        classContent = classContent + "MessageContent:\n\"" + this.messageText + "\"\n\n";
        classContent = classContent + "Targets: \n" + listToString(this.targets);

        System.out.println(classContent);
    }

    @Override
    public String toString() {
        String classContent = "";
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        classContent = classContent + "Date of creating: " + formatter.format(date) + "\n\n";
        classContent = classContent + "First Name: " + this.fName + '\n';
        classContent = classContent + "Second Name: " + this.sName + '\n';
        classContent = classContent + "Age: " + this.age + '\n';
        classContent = classContent + "MessageContent:\n\"" + this.messageText + "\"\n";
        classContent = classContent + "Targets: \n" + listToString(this.targets);

        return classContent;
    }

    public static String listToString(List<Android_Contact> list) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += "Name: " + list.get(i).android_contact_Name +
                    " Phone number: " + list.get(i).android_contact_TelefonNr + '\n';
        }
        return result;
    }

    public boolean loadConfigFromFile(String fileName, CurrentConfiguration data, Context context)
    {


        return false;
    }
}
