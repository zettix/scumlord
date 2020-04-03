package com.zettix.scumlord;

public class PlayerStatChange {
    private int populationChange;
    private int reputationChange;
    private int incomeChange;
    private int fundsChange;


    @Override
    public String toString() {
        String s = "<";
        if (populationChange != 0) {
            s += "[p:" + populationChange + "]";
        }

        if (incomeChange != 0) {
            s += "[i:" + incomeChange + "]";
        }

        if (reputationChange != 0) {
            s += "[r:" + reputationChange + "]";
        }

        if (fundsChange != 0) {
            s += "[$:" + fundsChange + "]";
        }
        s += ">";
        return s;
    }

    public int getPopulationChange() {
        return populationChange;
    }

    public int getReputationChange() {
        return reputationChange;
    }

    public int getIncomeChange() {
        return incomeChange;
    }

    public int getFundsChange() {
        return fundsChange;
    }

    public void setPopulationChange(int populationChange) {
        this.populationChange = populationChange;
    }

    public void setReputationChange(int reputationChange) {
        this.reputationChange = reputationChange;
    }

    public void setIncomeChange(int incomeChange) {
        this.incomeChange = incomeChange;
    }

    public void setFundsChange(int fundsChange) {
        this.fundsChange = fundsChange;
    }
}
