package com.pennapps.insulin;

public class Meal {
    final boolean insulinType;
    final int TDD;
    final int targetBG;
    private int actualBG;
    private int mealCHO;

    public Meal()
    {
        this(false,50,120);
    }

    public Meal(boolean type, int dailydose, int target)
    {
        insulinType = type;
        TDD = dailydose;
        targetBG = target;
    }

    public void setCurrentBG(int currentBG)
    {
        actualBG = currentBG;
    }

    public void setMealCHO(int mealCarbs)
    {
        mealCHO = mealCarbs;
    }

    public int calcCHO()
    {
        if(insulinType) //rapid acting
        {
            return 500/TDD;
        }
        else //regular acting
        {
            return 450/TDD;
        }
    }

    public int calcCF() // correction factor aka insulin sensitiveity factor
    {
        return 1500/TDD;
    }

    public int calcCarbCoverage()
    {
        return mealCHO / calcCHO();
    }

    public int calcBloodSugarCorrection()
    {
        return (int) Math.ceil((actualBG - targetBG)/calcCF());
    }

    public int insulin() {
        return Math.max(calcCarbCoverage() + calcBloodSugarCorrection(),0);
    }

}

