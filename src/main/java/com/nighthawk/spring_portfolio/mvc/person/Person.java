package com.nighthawk.spring_portfolio.mvc.person;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import com.nighthawk.spring_portfolio.mvc.steptracker.StepLog;
import com.nighthawk.spring_portfolio.mvc.steptracker.StepTracker;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is a POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class Person {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min = 5)
    @Column(unique = true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max =
    // 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    // health related info (other than age)
    @Positive
    private double height; // in inches

    @Positive
    private double weight; // in lb

    @NotEmpty
    private String gender;

    // one Person has many StepLogs (relationship)
    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<StepLog> stepLogs = new ArrayList<>();

    /*
     * HashMap is used to store JSON for daily "stats"
     * "stats": {
     * "2022-11-13": {
     * "calories": 2200,
     * "steps": 8000
     * }
     * }
     */
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Map<String, Map<String, Object>> stats = new HashMap<>();

    // Constructor used when building object from an API
    public Person(String email, String password, String name, Date dob, double height, double weight, String gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender.toLowerCase();
    }

    // public int getStepGoal() { // TODO: change to actual calculation
    // return 10000;
    // }

    // A custom getter to return age from dob attribute
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears();
        }
        return -1;
    }

    public double bmrCalculator() {
        if (getGender().equals("male")) {
            return 66.5 + (13.75 * getWeight() / 2.205) + (5.003 * getHeight() * 2.54) - (6.75 * getAge());
        } else if (getGender().equals("female")) {
            return 655.1 + (9.563 * getWeight() / 2.205) + (1.850 * getHeight() * 2.54) - (4.676 * getAge());
        } else {
            return -1;
        }
    }

    public double amrCalculator() {
        return bmrCalculator() * 1.55;
    }

    // Calories required to burn per day to maintain current weight (Uses Active &
    // Basal Metabolic Rate)
    public int getStepGoal() {
        if (getDob() != null && getWeight() != 0 && getHeight() != 0) {
            // assumes moderate exercise each day, indicates calories needed to burn
            return (int) (Math.round(amrCalculator() - bmrCalculator() - 500) / 0.04);
        }

        return -1;
    }

    // toString Method
    public String toString() {
        return ("{ \"email\": " + this.email + ", " + "\"password\": " + this.password + ", " + "\"name\": " + this.name
                + ", " + "\"dob\": " + this.dob + ", " + "\"height\": " + this.height + ", " + "\"weight\": "
                + this.weight + ", " + "\"gender\": " + this.gender + " }");
    }

    // Unit Test
    public static void main(String[] args) {
        // instantiates no args & all args
        Person personNoArgs = new Person();
        Person personAllArgs = new Person("someone@email.com", "12345", "Joe Jack",
                new java.util.GregorianCalendar(2005, 11, 8).getTime(), 68, 140, "Male");

        // toString prints
        System.out.println(personNoArgs.toString());
        System.out.println(personAllArgs.toString());

        // getCaloriesRequiredPerDay print tests
        System.out.println(personNoArgs.getStepGoal());
        System.out.println(personAllArgs.getStepGoal());
    }

}