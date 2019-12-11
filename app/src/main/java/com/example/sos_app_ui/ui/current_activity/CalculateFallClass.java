package com.example.sos_app_ui.ui.current_activity;

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
    private Integer firstLowImpactValue;
    private Integer secondLowImpactValue;

    private Integer notMoveHighValue;
    private Integer notMoveLowValue;

    private Long delayBeforeNotMove;     // amount of detections after impact
    private Long counterOfDelay;

    private boolean needCheckmpact = true;

    public CalculateFallClass(int listOfPossibleImpactLength, int listofPossibleNotMoveLength,
                              Integer firstHighImpactValue, Integer secondHighImpactValue,
                              Integer firstLowImpactValue, Integer secondLowImpactValue,
                              Integer notMoveHighValue, Integer notMoveLowValue, Long delayBeforeNotMove) {
        this.firstHighImpactValue = firstHighImpactValue;
        this.secondHighImpactValue = secondHighImpactValue;
        this.firstLowImpactValue = firstLowImpactValue;
        this.secondLowImpactValue = secondLowImpactValue;
        this.notMoveHighValue = notMoveHighValue;
        this.notMoveLowValue = notMoveLowValue;
        this.delayBeforeNotMove = delayBeforeNotMove;
        this.listOfPossibleImpact = new LinkedList<>();
        this.listofPossibleImpactLength = listOfPossibleImpactLength;
        this.listOfPossibleNotMove = new LinkedList<>();
        this.listofPossibleNotMoveLength = listofPossibleNotMoveLength;
    }

    public void setAccXValue(Float accXValue) {
        this.accXValue = accXValue;
    }

    public void setAccYValue(Float accYValue) {
        this.accYValue = accYValue;
    }

    public void setAccZValue(Float accZValue) {
        this.accZValue = accZValue;
    }

    public Boolean calculate(){
        if(impactAlarm) {
            //possibleNotMove = calculateNotMove();
            //  if(possibleNotMove)
            if(calculateNotMove())
                addElementToNotMoveList(true);
            else {
                addElementToNotMoveList(false);
                counterOfDelay++;
                if(counterOfDelay >= delayBeforeNotMove){
                    impactAlarm = false;
                    counterOfDelay = (long)0;
                }
            }
            notMoveAlarm = checkNotMoveList();
        }
        else {
            //possibleImpact = calculateImpact();
            //if (possibleImpact)
            counterOfDelay = (long)0;  // zrob cos z tym
            if(calculateImpact())
                addElementToImpactList(true);
            else
                addElementToImpactList(false);
            impactAlarm = checkImpactList();
        }

        //if(needCheckmpact)
          //  impactAlarm = checkImpactList();

//        if(impactAlarm) {
//            needCheckmpact = false;
//            notMoveAlarm = checkNotMoveList();
//        }
        // jesli przez jakis czas nie, to zresetuj impactAlarm
        if(impactAlarm && notMoveAlarm)
            return true;
        return false;
    }
    
    private boolean calculateNotMove(){
        if(accZValue==null || accYValue==null || accXValue==null)
            return false;
//        if(accXValue < notMoveHighValue && accXValue > notMoveLowValue &
//                accYValue < notMoveHighValue && accYValue > notMoveLowValue &&
//                accZValue < notMoveHighValue && accZValue > notMoveLowValue){
//            return true;
//        }
        if(Math.abs(accXValue) < notMoveLowValue &&
                Math.abs(accYValue) < notMoveLowValue &&
                Math.abs(accZValue) < notMoveLowValue)
            return true;
        return false;
    }

    private boolean calculateImpact(){
        if(accZValue==null || accYValue==null || accXValue==null)
            return false;
        //System.out.println("X: "+accXValue+", Y: "+accYValue+", Z: "+accZValue);
//        if(accXValue > firstHighImpactValue || accXValue < firstLowImpactValue &
//                accYValue > secondHighImpactValue || accYValue < secondLowImpactValue &&
//                accZValue > secondHighImpactValue || accZValue < secondLowImpactValue){
//            return true;
//        }
//        else if(accYValue > firstHighImpactValue || accYValue < firstLowImpactValue &
//          3      accXValue > secondHighImpactValue || accXValue < secondLowImpactValue &&
//                accZValue > secondHighImpactValue || accZValue < secondLowImpactValue){
//            return true;
//        }
//        else if(accZValue > firstHighImpactValue || accZValue < firstLowImpactValue &
//                accXValue > secondHighImpactValue || accXValue < secondLowImpactValue &&
//                accYValue > secondHighImpactValue || accYValue < secondLowImpactValue){
//            return true;
//        }
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
}
