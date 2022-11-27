package com.nighthawk.spring_portfolio.mvc.steptracker;

import java.util.List;
import java.util.Map;

import com.nighthawk.spring_portfolio.mvc.person.Person;

public class StepTracker {
    private int stepGoal;
    private int activeDays;
    private int totalSteps;
    private int daysRecorded;

    public StepTracker(int stepGoal) {
        this.stepGoal = stepGoal;
        this.totalSteps = 0;
        this.daysRecorded = 0;
    }

    public double caloriesBurnt(Person person) {
        return (totalSteps * 0.04 + person.bmrCalculator() * daysRecorded);
    }

    public double caloriesConsumed(Person person) {
        Map<String, Map<String, Object>> stats = person.getStats();
        double caloriesConsumed = 0.0;

        for (String key : stats.keySet()) {
            caloriesConsumed += (int) stats.get(key).get("calories");
        }

        return caloriesConsumed;
    }

    public double netCalories(Person person) {
        return caloriesConsumed(person) - caloriesBurnt(person);
    }

    public String netWeightReport(Person person) {
        if (netCalories(person) > 200) {
            return "\"Gaining weight\"";
        } else if (netCalories(person) < -200) {
            return "\"Losing weight\"";
        } else {
            return "\"Maintaining current weight\"";
        }
    }

    public void calculateSteps(Person person) {
        Map<String, Map<String, Object>> stats = person.getStats();
        setDaysRecorded(stats.size());

        for (String key : stats.keySet()) {
            addDailySteps((int) stats.get(key).get("steps"));
        }
    }

    public String exerciseReport(Person person) {
        this.calculateSteps(person);

        return ("{ \"personID\": " + person.getId() + ", " + "\"stepGoal\": " + this.stepGoal + ", "
                + "\"totalSteps\": "
                + this.totalSteps + ", " + "\"daysRecorded\": " + this.daysRecorded + ", " + "\"activeDays\": "
                + activeDays() + ", " + "\"caloriesConsumed\": " + caloriesConsumed(person) + ", "
                + "\"caloriesBurnt\": "
                + caloriesBurnt(person) + ", " + "\"netCalories\": " + this.netCalories(person) + ", "
                + "\"netWeightReport\": " + netWeightReport(person) + ", " + "\"stats\": " + person.getStats() + " }");
    }

    // FRQ Methods
    public void addDailySteps(int steps) {
        setTotalSteps(getTotalSteps() + steps); // adds steps

        // checks if activeDay & adds to count
        if (steps >= getStepGoal()) {
            setActiveDays(getActiveDays() + 1);
        }
    }

    public int activeDays() {
        return getActiveDays();
    }

    public double averageSteps() {
        if (getDaysRecorded() == 0) {
            return 0.0;
        }

        double averageSteps = (double) getTotalSteps() / getDaysRecorded();
        return averageSteps;
    }

    // main method (tester/unit test)
    // public static void main(String[] args) {
    // StepTracker tr = new StepTracker(10000);

    // System.out.println("---------------------");
    // System.out.println("Active Days: " + tr.activeDays()); // should return 0
    // System.out.println("Average Steps: " + tr.averageSteps()); // should return
    // 0.0
    // System.out.println("---------------------");

    // tr.addDailySteps(9000);
    // System.out.println("Added 9000 Steps");

    // tr.addDailySteps(5000);
    // System.out.println("Added 5000 Steps");

    // System.out.println("Active Days: " + tr.activeDays()); // should return 0
    // System.out.println("Average Steps: " + tr.averageSteps()); // should return
    // 7000.0
    // System.out.println("---------------------");

    // tr.addDailySteps(13000);
    // System.out.println("Added 13000 Steps");

    // System.out.println("Active Days: " + tr.activeDays()); // should return 1
    // System.out.println("Average Steps: " + tr.averageSteps()); // should return
    // 9000.0
    // System.out.println("---------------------");

    // tr.addDailySteps(23000);
    // System.out.println("Added 23000 Steps");

    // tr.addDailySteps(1111);
    // System.out.println("Added 1111 Steps");

    // System.out.println("Active Days: " + tr.activeDays()); // should return 2
    // System.out.println("Average Steps: " + tr.averageSteps()); // should return
    // 10222.2
    // System.out.println("---------------------");
    // }

    // Setters & Getters
    public int getStepGoal() {
        return this.stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public int getActiveDays() {
        return this.activeDays;
    }

    public void setActiveDays(int activeDays) {
        this.activeDays = activeDays;
    }

    public int getTotalSteps() {
        return this.totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getDaysRecorded() {
        return daysRecorded;
    }

    public void setDaysRecorded(int daysRecorded) {
        this.daysRecorded = daysRecorded;
    }
}
