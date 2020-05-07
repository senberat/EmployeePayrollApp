/*
    Employee.java
    Author: Berat U. Sen
    Date: 7 Apr 2019

    Description
    Defines an instance of an "employee" and calculates a few tax related info
 */
package finalapplication;

/**
 *
 * @author Berat
 */
public class Employee {

    private String name, position;
    private double salary, tax, netpay;
    // tax is set to 25% flat
    private final double TAX_RATE = 0.25;

    public Employee() {
    }

    public Employee(String name, String position, double salary,
        double tax, double netpay) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.tax = tax;
        this.netpay = netpay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * calculates the tax to be paid from the monthly salary
     * @return salary multiplied by the tax rate
     */
    public double getTax() {
        return salary * TAX_RATE;
    }

    /**
     * calculates how much the person will earn after the taxes are deducted
     * @return deduction of getTax from salary
     */
    public double getNetpay() {
        return salary - getTax();
    }


}
