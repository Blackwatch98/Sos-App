package com.example.sos_app_ui.ui.current_activity;

import java.util.LinkedList;

public class CalculateFallClass {
    private LinkedList<Double> list;
    private Integer listCapacity;

    CalculateFallClass(LinkedList list, Integer lengthList){
        this.list=list;
        this.listCapacity=lengthList;
    }

    CalculateFallClass(LinkedList list){
        this.list=list;
        this.listCapacity = 10;
    }

    CalculateFallClass(){
        this.list=new LinkedList<Double>();
        this.listCapacity = 10;
    }

    public void addElement(Double number){
        list.add(number);
        if(list.size() > listCapacity)
            list.remove();
    }

    public void removeElement(){
        list.remove();
    }

    public void addAndRemoveElement(Double number){
        list.remove();
        list.add(number);
    }

    public Double calculateRiskPercent(Double number){
        Double result = avg() / number;
        return result; // finish it!
    }

    private Double avg(){
        Double sum = (double) 0;
        for (Double val: list) {
            sum += val;
        }
        return sum / listCapacity;
    }
}
