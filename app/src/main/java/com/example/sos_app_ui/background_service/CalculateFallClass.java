package com.example.sos_app_ui.background_service;

import java.util.LinkedList;

public class CalculateFallClass {
    private Boolean impactAlarm = false;
    private Boolean possibleImpact = false;
    private Boolean possibleNotMove = false;
    private Boolean notMoveAlarm = false;

    private Float accXValue;
    private Float accYValue;
    private Float accZValue;

    private LinkedList<Boolean> listOfPossibleImpact;
    private Integer listofPossibleImpactLength;
    private LinkedList<Boolean> listOfPossibleNotMove;
    private Integer listofPossibleNotMoveLength;

    private Integer firstHighImpactValue;
    private Integer secondHighImpactValue;
    private Integer notMoveValue;

    private Long stopAlarmValue;
    private Long counterToStopAlarm;

    private Double timeImpact;
    private Double timeNotMove;

    public CalculateFallClass(int listOfPossibleImpactLength, int listofPossibleNotMoveLength,
                              Integer firstHighImpactValue, Integer secondHighImpactValue,
                              Integer notMoveValue, Long stopAlarmValue, Double timeNotMove) {
        this.firstHighImpactValue = firstHighImpactValue;
        this.secondHighImpactValue = secondHighImpactValue;
        this.notMoveValue = notMoveValue;
        this.stopAlarmValue = stopAlarmValue;
        this.timeNotMove = timeNotMove;
        this.listOfPossibleImpact = new LinkedList<>();
        this.listofPossibleImpactLength = listOfPossibleImpactLength;
        this.listOfPossibleNotMove = new LinkedList<>();
        this.listofPossibleNotMoveLength = listofPossibleNotMoveLength;
    }

    public void setAccValueX(Float accXValue) {
        this.accXValue = accXValue;
    }

    public void setAccValueY(Float accYValue) {
        this.accYValue = accYValue;
    }

    public void setAccValueZ(Float accZValue) {
        this.accZValue = accZValue;
    }

    public Boolean calculate(){
        if(impactAlarm) {
            if(calculateNotMove())
                addElementToNotMoveList(true);
            else {
                addElementToNotMoveList(false);
                counterToStopAlarm++;
                if(getCurrentSeconds() - timeImpact < timeNotMove && counterToStopAlarm > stopAlarmValue)
                    impactAlarm = false;
            }
            notMoveAlarm = checkNotMoveList();
        }
        else {
            counterToStopAlarm = (long)0;
            if(calculateImpact())
                addElementToImpactList(true);
            else
                addElementToImpactList(false);
            if(impactAlarm = checkImpactList())
                timeImpact = getCurrentSeconds();
        }

        if(impactAlarm && notMoveAlarm && getCurrentSeconds() - timeImpact > timeNotMove)
            return true;
        return false;
    }
    
    private boolean calculateNotMove(){
        if(accZValue==null || accYValue==null || accXValue==null)
            return false;

        if(Math.abs(accXValue) < notMoveValue &&
                Math.abs(accYValue) < notMoveValue &&
                Math.abs(accZValue) < notMoveValue)
            return true;
        return false;
    }

    private boolean calculateImpact(){
        if(accZValue==null || accYValue==null || accXValue==null)
            return false;

        if(Math.abs(accXValue) > firstHighImpactValue &&
                Math.abs(accYValue) > secondHighImpactValue &&
                Math.abs(accZValue) > secondHighImpactValue)
            return true;
        if(Math.abs(accYValue) > firstHighImpactValue &&
                Math.abs(accXValue) > secondHighImpactValue &&
                Math.abs(accZValue) > secondHighImpactValue)
            return true;
        if(Math.abs(accZValue) > firstHighImpactValue &&
                Math.abs(accYValue) > secondHighImpactValue &&
                Math.abs(accXValue) > secondHighImpactValue)
            return true;
        return false;
    }

    private void addElementToImpactList(boolean bool){
        listOfPossibleImpact.add(bool);
        if(listOfPossibleImpact.size() > listofPossibleImpactLength)
            listOfPossibleImpact.remove();
    }

    private void addElementToNotMoveList(boolean bool){
        listOfPossibleNotMove.add(bool);
        if(listOfPossibleNotMove.size() > listofPossibleNotMoveLength)
            listOfPossibleNotMove.remove();
        //if(!bool)
            //System.out.println("ruszam sie");
    }

    private boolean checkImpactList(){
        if(listOfPossibleImpact.size() != listofPossibleImpactLength)
            return false;
        //System.out.println(listOfPossibleImpact);
        for (Boolean bool: listOfPossibleImpact) {
            if(!bool)
                return false;
        }
        return true;
    }

    private boolean checkNotMoveList(){
        if(listOfPossibleNotMove.size() != listofPossibleNotMoveLength)
            return false;

        for (Boolean bool: listOfPossibleNotMove) {
            if(!bool)
                return false;
        }
        return true;
    }

    private double getCurrentSeconds(){
        return System.nanoTime() / 1_000_000_000.0;
    }
}
