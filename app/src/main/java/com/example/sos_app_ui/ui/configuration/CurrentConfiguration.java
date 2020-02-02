package com.example.sos_app_ui.ui.configuration;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel Duczymiński
 *
 * That is main configuration data class
 * It contains all data filled by user like:
 * Personal data: first name, second name, age
 * Message Content
 * Chosen contact targets
 */

public class CurrentConfiguration implements Serializable
{
    private String fName;
    private String sName;
    private int age;

    private static String messageText;

    public static String getMessageText() {
        return messageText;
    }

    public List<AndroidContact> getTargets() {
        return targets;
    }

    private static List<AndroidContact> targets;

    private File file;

    /**
     * This is constructor to initialize configuration object with empty data
     */
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

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setTargets(List<AndroidContact> targets) {
        this.targets = targets;
    }

    /**
     * This method creates new configuration file and fill it with data
     * @param fileName name of file (normally ,,Config" with its number in directory)
     * @param data data that file should be fulfilled with
     * @param context context of activity
     * @return true if operation was successful, false if not
     */

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

    /**
     * This method load data from chosen file to CurrentConfiguration attributes
     * @param path path to chosen file
     * @param context activity context
     * @return true if operation was successful, false if not
     */

    public boolean getDataFromConfigFile(String path, Context context)
    {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            System.out.println("ext not available");
        }
        else {
            context.getExternalFilesDir("Configurations");
            file = new File(path);
        }

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            br.readLine();
            br.readLine();


            st = br.readLine();
            String[] words = st.split(" ");
            this.fName = words[2];

            st = br.readLine();
            words = st.split(" ");
            this.sName = words[2];

            st = br.readLine();
            words = st.split(" ");
            this.age = Integer.parseInt(words[1]);

            br.readLine();
            this.messageText="";
            while(true)
            {
                st = br.readLine();
                if(!st.equals("Targets: "))
                    this.messageText += st;
                else
                    break;
            }

            this.targets = new ArrayList<>();
            while((st = br.readLine()) != null)
            {
                words = st.split(" ");
                AndroidContact contact = new AndroidContact();
                String contactName = "";
                for(String str : words)
                {
                    if(str.equals("Name:"))
                        continue;
                    else if(str.equals("Phone"))
                        break;
                    else
                        contactName += str;
                        contactName += " ";
                }

                contact.setAndroid_contact_Name(contactName);
                contact.setAndroid_contact_TelefonNr(words[words.length-1]);
                this.targets.add(contact);
            }
            br.close();
        }
        catch(FileNotFoundException e)
        {
            e.getMessage();
        }
        catch (IOException e)
        {
            e.getMessage();
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

    /**
     * Method to display whole class content
     */
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


    /**
     * Method that converts class attributes into a string
     * @return class content as string
     */

    @Override
    public String toString() {
        String classContent = "";
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        classContent = classContent + "Date of creating: " + formatter.format(date) + "\n\n";
        classContent = classContent + "First Name: " + this.fName + '\n';
        classContent = classContent + "Second Name: " + this.sName + '\n';
        classContent = classContent + "Age: " + this.age + '\n';
        classContent = classContent + "MessageContent:\n" + this.messageText + "\n";
        classContent = classContent + "Targets: \n" + listToString(this.targets);

        return classContent;
    }

    /**
     * Method that converts list of android contacts into a string
     * @param list list of contacts
     * @return  list of contscts in a string
     */

    public static String listToString(List<AndroidContact> list) {
        if(list != null)
        {
            String result = "";
            for (int i = 0; i < list.size(); i++) {
                result += "Name: " + list.get(i).android_contact_Name +
                        " Phone number: " + list.get(i).android_contact_TelefonNr + '\n';
            }
            return result;
        }
        return null;
    }

    /**
     * That method checks if all config attributes are correctly changed
     * @param tested class that is being tested
     * @return true if everything is correct, false if not
     */

    public boolean validateChanges(CurrentConfiguration tested)
    {
        boolean isChanged = false;

        if(!tested.getfName().equals(this.getfName()) && !tested.getsName().equals(this.getsName()) &&
                tested.getAge() != this.age &&
                tested.targets != null)
            isChanged = true;

        return isChanged;
    }
}


