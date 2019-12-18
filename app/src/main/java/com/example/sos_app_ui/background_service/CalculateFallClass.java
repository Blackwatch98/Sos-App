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
    private Integer listofPossibleImpactLength;
    private LinkedList<Boolean> listOfPossibleNotMove;
    private Integer listofPossibleNotMoveLength;
    //private Integer notMoveCounter;

    // risk values
    private Integer highImpactValue;
    private Integer lowImpactValue;
    private Double notMoveValue;

    // move after impact
    private Long stopAlarmValue;
    private Long counterToStopAlarm;

    // time
    private Double timeImpact;
    private Double timeNotMove;
    private Double startedNotMovingTime;

    public CalculateFallClass(int listOfPossibleImpactLength, int listofPossibleNotMoveLength,
                              Integer highImpactValue, Integer lowImpactValue,
                              Double notMoveValue, long stopAlarmValue, double timeNotMove) {
        this.highImpactValue = highImpactValue;
        this.lowImpactValue = lowImpactValue;
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
            if(calculateNotMove()) {
                addElementToNotMoveList(true);
                if (checkNotMoveList()) {
                    counterToStopAlarm = (long) 0;
                    if (startedNotMovingTime == 0)
                        startedNotMovingTime = getCurrentSeconds();
                }

            }
            else {
                addElementToNotMoveList(false);
                counterToStopAlarm++;
                if(getCurrentSeconds() - timeImpact > timeNotMove)
                    if(counterToStopAlarm > stopAlarmValue)
                        impactAlarm = false;
            }
            //notMoveAlarm = checkNotMoveList();
            if(startedNotMovingTime != 0 && getCurrentSeconds() - startedNotMovingTime > 10)
                notMoveAlarm = true;
        }
        else {
            startedNotMovingTime = (double)0;
            counterToStopAlarm = (long)0;
            if(calculateImpact())
                addElementToImpactList(true);
            else
                addElementToImpactList(false);
            if(impactAlarm = checkImpactList())
                timeImpact = getCurrentSeconds();
        }

        if(impactAlarm && notMoveAlarm && (getCurrentSeconds() - timeImpact) > timeNotMove)
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

    private boolean checkNotMoveList() {
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
