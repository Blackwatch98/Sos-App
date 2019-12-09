package com.example.sos_app_ui.ui.current_activity;

import androidx.fragment.app.Fragment;

import com.example.sos_app_ui.ui.configuration.PersonalDataPanel;

import java.security.PermissionCollection;
import java.util.LinkedList;

public class CalculateSensorClass {
    public LinkedList<Float> list;
    private Integer listCapacity;
    private Float max = (float)0;
    private String line;

    CalculateSensorClass(LinkedList list, Integer lengthList, String line){
        this.list=list;
        this.listCapacity=lengthList;
        this.line = line;
    }

    CalculateSensorClass(LinkedList list){
        this.list=list;
        this.listCapacity = 10;
    }
    CalculateSensorClass(Integer lengthList, String line){
        this.list=new LinkedList<>();
        this.listCapacity = lengthList;
        this.line = line;
    }

    CalculateSensorClass(){
        this.list=new LinkedList<Float>();
        this.listCapacity = 10;
    }

    public Float addElement(Float number){
        Float percent = calculateRiskPercent(number);
        list.add(number);
        if(list.size() > listCapacity)
            list.remove();

        return percent;
    }

    public void removeElement(){
        list.remove();
    }

    public void addAndRemoveElement(Float number){
        list.remove();
        list.add(number);
    }

    // popraw to => liczenie procentow z malutkich wartosci nie ma sensu
    private Float calculateRiskPercent(Float number){
        if(list.size()<1)
            return null;
        Float avg = avg();
        if(avg == 0)
            return null;
        Float percent = (number*100) / avg();
        percent = (float) 100 - percent;
        if(percent> max)
            max=percent;
        return percent;
    }

    private Float avg(){
        if(list.size() < 1)
            return null;
        Float sum = (float) 0;
        for (Float val: list) {
            sum += val;
        }
        return sum / listCapacity;
    }

    public String display(){
        return String.valueOf(max);
    }
}
