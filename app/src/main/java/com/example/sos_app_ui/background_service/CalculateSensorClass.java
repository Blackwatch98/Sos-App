package com.example.sos_app_ui.background_service;

import androidx.fragment.app.Fragment;

import com.example.sos_app_ui.ui.configuration.PersonalDataPanel;

import java.security.PermissionCollection;
import java.util.LinkedList;

public class CalculateSensorClass {
    public LinkedList<Float> list;
    private Integer listCapacity;
    private Float max = (float)0;

    CalculateSensorClass(LinkedList list, Integer lengthList){
        this.list=list;
        this.listCapacity=lengthList;
    }

    CalculateSensorClass(LinkedList list){
        this.list=list;
        this.listCapacity = 10;
    }
    CalculateSensorClass(Integer lengthList){
        this.list=new LinkedList<>();
        this.listCapacity = lengthList;
    }

    CalculateSensorClass(){
        this.list=new LinkedList<Float>();
        this.listCapacity = 10;
    }

    // change that names...
    public Float addElement(Float number){
        Float riskValue = calculateRisk(number);
        list.add(number);
        if(list.size() > listCapacity)
            list.remove();

        return riskValue;
    }

    public void removeElement(){
        list.remove();
    }

    public void addAndRemoveElement(Float number){
        list.remove();
        list.add(number);
    }

    private Float calculateRisk(Float number){
        if(list.size()<1)
            return null;
        Float avg = avg();
        if(avg == 0)
            return null;
        return number - avg;
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
