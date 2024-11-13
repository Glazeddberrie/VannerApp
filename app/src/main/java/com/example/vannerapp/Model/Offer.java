package com.example.vannerapp.Model;

public class Offer {
    private String name;
    private String company;
    private String salary;

    public Offer(String name, String company, String salary) {
        this.name = name;
        this.company = company;
        this.salary = salary;
    }

    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getSalary() { return salary; }
}

