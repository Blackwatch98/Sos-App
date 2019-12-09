package com.example.sos_app_ui.ui.current_activity;

import java.util.LinkedList;

public class CalculateFallClass {
    private Boolean impactAlarm = false;
    private Boolean possibleImpact = false;
    private Boolean possibleNotMove = false;
    private Boolean notMoveAlarm = false;

    private Float accXPercent;
    private Float accYPercent;
    private Float accZPercent;

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

    public CalculateFallClass(int listOfPossibleImpactLength, int listofPossibleNotMoveLength,
                              Integer firstHighImpactValue, Integer secondHighImpactValue,
                              Integer firstLowImpactValue, Integer secondLowImpactValue,
                              Integer notMoveHighValue, Integer notMoveLowValue) {
        this.firstHighImpactValue = firstHighImpactValue;
        this.secondHighImpactValue = secondHighImpactValue;
        this.firstLowImpactValue = firstLowImpactValue;
        this.secondLowImpactValue = secondLowImpactValue;
        this.notMoveHighValue = notMoveHighValue;
        this.notMoveLowValue = notMoveLowValue;
        this.listOfPossibleImpact = new LinkedList<>();
        this.listofPossibleImpactLength = listOfPossibleImpactLength;
        this.listOfPossibleNotMove = new LinkedList<>();
        this.listofPossibleNotMoveLength = listofPossibleNotMoveLength;
    }

    public void setAccXPercent(Float accXPercent) {
        this.accXPercent = accXPercent;
    }

    public void setAccYPercent(Float accYPercent) {
        this.accYPercent = accYPercent;
    }

    public void setAccZPercent(Float accZPercent) {
        this.accZPercent = accZPercent;
    }

    public Boolean calculate(){
         possibleImpact = calculateImpact();
         possibleNotMove = calculateNotMove();

         if(possibleNotMove)
             addElementToNotMoveList(true);
         else
             addElementToNotMoveList(false);

        if(possibleImpact)
            addElementToImpactList(true);
        else
            addElementToImpactList(false);
        
        impactAlarm = checkImpactList();
        if(impactAlarm)
            notMoveAlarm = checkNotMoveList();
        // jesli przez jakis czas nie, to zresetuj impactAlarm
        if(impactAlarm && notMoveAlarm)
            return true;
        return false;
    }
    
    private boolean calculateNotMove(){
        if(accZPercent==null || accYPercent==null || accXPercent==null)
            return false;
        if(accXPercent < notMoveHighValue && accXPercent > notMoveLowValue &
                accYPercent < notMoveHighValue && accYPercent > notMoveLowValue &&
                accZPercent < notMoveHighValue && accZPercent > notMoveLowValue){
            return true;
        }
        return false;
    }

    private boolean calculateImpact(){
        if(accZPercent==null || accYPercent==null || accXPercent==null)
            return false;
        if(accXPercent > firstHighImpactValue || accXPercent < firstLowImpactValue &
                accYPercent > secondHighImpactValue || accYPercent < secondLowImpactValue &&
                accZPercent > secondHighImpactValue || accZPercent < secondLowImpactValue){
            return true;
        }
        else if(accYPercent > firstHighImpactValue || accYPercent < firstLowImpactValue &
                accXPercent > secondHighImpactValue || accXPercent < secondLowImpactValue &&
                accZPercent > secondHighImpactValue || accZPercent < secondLowImpactValue){
            return true;
        }
        else if(accZPercent > firstHighImpactValue || accZPercent < firstLowImpactValue &
                accXPercent > secondHighImpactValue || accXPercent < secondLowImpactValue &&
                accYPercent > secondHighImpactValue || accYPercent < secondLowImpactValue){
            return true;
        }
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
        if(!bool)
            System.out.println(false);
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
