package com.example.sos_app_ui.background_service;

import java.util.LinkedList;

public class CalculateFallClass {

    // alarms
    private Boolean impactAlarm = false;
    private Boolean possibleImpact = false;
    private Boolean possibleNotMove = false;
    private Boolean notMoveAlarm = false;

    // xyz values
    private Float accXValue;
    private Float accYValue;
    private Float accZValue;

    // lists
    private LinkedList<Boolean> listOfPossibleImpact;
    private Integer listOfImpactLength;
    private LinkedList<Boolean> listOfPossibleNotMove;
    private Integer listofNotMoveLength;
    //private Integer notMoveCounter;

    // risk values
    private Integer highImpactValue;
    private Integer lowImpactValue;
    private Double notMoveValue;

    // move after impact
    private Integer stopAlarmValue;
    private Integer counterToStopAlarm = 0;

    // time
    private Double timeImpact;
    private Double timeAfterImpact;
    private Double startedNotMovingTime = (double)0;
    private Double notMoveTime;

    public CalculateFallClass(int listOfImpactLength, int listofNotMoveLength,
                              Integer highImpactValue, Integer lowImpactValue,
                              Double notMoveValue, int stopAlarmValue, double timeAfterImpact,
                              Double notMoveTime) {
        this.highImpactValue = highImpactValue;
        this.lowImpactValue = lowImpactValue;
        this.notMoveValue = notMoveValue;
        this.stopAlarmValue = stopAlarmValue;
        this.timeAfterImpact = timeAfterImpact;
        this.notMoveTime = notMoveTime;
        this.listOfPossibleImpact = new LinkedList<>();
        this.listOfImpactLength = listOfImpactLength;
        this.listOfPossibleNotMove = new LinkedList<>();
        this.listofNotMoveLength = listofNotMoveLength;
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
            if(calculateImpact())
                addElementToImpactList(true);
            else
                addElementToImpactList(false);


            if(calculateNotMove()) {
                addElementToNotMoveList(true);
                if (checkNotMoveList()) {
                    counterToStopAlarm = 0;
                    if (startedNotMovingTime == 0)
                        startedNotMovingTime = getCurrentSeconds();
                }

            }
            else {
                addElementToNotMoveList(false);
                if(getCurrentSeconds() - timeImpact > timeAfterImpact) {
                    counterToStopAlarm++;
                    if(checkImpactList()) {
                        counterToStopAlarm = 0;
                        timeImpact = getCurrentSeconds();
                        startedNotMovingTime = (double)0;
                    }
                    if (counterToStopAlarm > stopAlarmValue) {
                        impactAlarm = false;
                        startedNotMovingTime = (double) 0;
                        counterToStopAlarm = 0;
                    }
                }
            }
            //notMoveAlarm = checkNotMoveList();
            if(startedNotMovingTime != 0 && getCurrentSeconds() - startedNotMovingTime > notMoveTime && impactAlarm && !notMoveAlarm)
                notMoveAlarm = true;
        }
        else {
            if(calculateImpact())
                addElementToImpactList(true);
            else
                addElementToImpactList(false);
            if(impactAlarm = checkImpactList())
                timeImpact = getCurrentSeconds();
        }
        System.out.println(accZValue);
        if(impactAlarm && notMoveAlarm && (getCurrentSeconds() - timeImpact) > timeAfterImpact) {
            impactAlarm = false;
            notMoveAlarm = false;
            timeImpact=(double)0;
            timeAfterImpact = (double)0;
            return true;
        }
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

        if(Math.abs(accXValue) > highImpactValue &&
                Math.abs(accYValue) > lowImpactValue &&
                Math.abs(accZValue) > lowImpactValue)
            return true;
        if(Math.abs(accYValue) > highImpactValue &&
                Math.abs(accXValue) > lowImpactValue &&
                Math.abs(accZValue) > lowImpactValue)
            return true;
        if(Math.abs(accZValue) > highImpactValue &&
                Math.abs(accYValue) > lowImpactValue &&
                Math.abs(accXValue) > lowImpactValue)
            return true;
        return false;
    }

    private void addElementToImpactList(boolean bool){
        listOfPossibleImpact.add(bool);
        if(listOfPossibleImpact.size() > listOfImpactLength)
            listOfPossibleImpact.remove();
    }

    private void addElementToNotMoveList(boolean bool){
        listOfPossibleNotMove.add(bool);
        if(listOfPossibleNotMove.size() > listofNotMoveLength)
            listOfPossibleNotMove.remove();
        //if(!bool)
            //System.out.println("ruszam sie");
    }

    private boolean checkImpactList(){
        if(listOfPossibleImpact.size() != listOfImpactLength)
            return false;
        //System.out.println(listOfPossibleImpact);
        for (Boolean bool: listOfPossibleImpact) {
            if(!bool)
                return false;
        }
        return true;
    }

    private boolean checkNotMoveList() {
        if(listOfPossibleNotMove.size() != listofNotMoveLength)
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
