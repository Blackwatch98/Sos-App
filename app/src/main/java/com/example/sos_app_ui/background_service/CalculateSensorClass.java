package com.example.sos_app_ui.background_service;

import java.util.LinkedList;

/**
 * Class that calculate diffrence of new record compared to last few.
 */
public class CalculateSensorClass {
    public LinkedList<Float> list;
    private Integer listCapacity;
    private Float max = (float)0;
    CalculateSensorClass(Integer lengthList){
        this.list=new LinkedList<>();
        this.listCapacity = lengthList;
    }

    // change that names...
    public Float addElement(Float number){
        Float riskValue = calculateRisk(number);
        list.add(number);
        if(list.size() > listCapacity)
            list.remove();

        return riskValue;
    }

    private Float calculateRisk(Float number){
        if(list.size()<1)
            return (float)0;
        Float avg = avg();
        if(avg == 0)
            return (float)0;
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
}
