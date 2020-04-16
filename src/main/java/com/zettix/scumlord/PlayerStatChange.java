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

    public PlayerStatChange addChange(PlayerStatChange change) {
        fundsChange += change.fundsChange;
        incomeChange += change.incomeChange;
        reputationChange += change.reputationChange;
        populationChange += change.populationChange;
        return this;
    }

    public PlayerStatChange setPopulationChange(int populationChange) {
        this.populationChange = populationChange;
        return this;
    }

    public PlayerStatChange setReputationChange(int reputationChange) {
        this.reputationChange = reputationChange;
        return this;
    }

    public PlayerStatChange setIncomeChange(int incomeChange) {
        this.incomeChange = incomeChange;
        return this;
    }

    public PlayerStatChange setFundsChange(int fundsChange) {
        this.fundsChange = fundsChange;
        return this;
    }
}
