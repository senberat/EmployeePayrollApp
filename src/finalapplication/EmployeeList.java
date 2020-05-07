/*
    EmployeeList.java
    Author: Berat U. Sen
    Date: 7 Apr 2019

    Description
    Creates an arraylist to store informtion related to employees and 
    sends them to the table when called
 */
package finalapplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Berat
 */
public class EmployeeList extends ArrayList<Employee> {

    private File file;
    private Scanner input;
    private Employee employee;
    int FIELD_SIZE = 35;
    long EMPLOYEE_SIZE = 164;
    ObservableList<Employee> list = FXCollections.observableArrayList();

    //ArrayList<Employee> list;
    public EmployeeList() {
        //  this.list = new ObservableList();
    }

    /**
     * writes the employees one by one to a random access file
     *
     * @param em an instance of an employee object
     * @throws IOException
     */
    public void writeEmployees(Employee em) throws IOException {
        file = new File("employees.dat");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(raf.length());

        String name = prepString(em.getName(), FIELD_SIZE);
        String pos = prepString(em.getPosition(), FIELD_SIZE);
        double salary = em.getSalary();
        double tax = em.getTax();
        double netpay = em.getNetpay();

        raf.writeChars(name);
        raf.writeChars(pos);
        raf.writeDouble(salary);
        raf.writeDouble(tax);
        raf.writeDouble(netpay);
        raf.close();
    }

    /**
     * deletes the content in the file
     *
     * @throws IOException
     */
    public void resetFile() throws IOException {
        file = new File("employees.dat");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(0);
    }

    /**
     * deletes everything in the file and rewrites the new info
     *
     * @param em instance of an employee object
     * @throws IOException
     */
    public void deleteFromList(Employee em) throws IOException {
        file = new File("employees.dat");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        raf.seek(raf.length());

        String name = prepString(em.getName(), FIELD_SIZE);
        String pos = prepString(em.getPosition(), FIELD_SIZE);
        double salary = em.getSalary();
        double tax = em.getTax();
        double netpay = em.getNetpay();

        raf.writeChars(name);
        raf.writeChars(pos);
        raf.writeDouble(salary);
        raf.writeDouble(tax);
        raf.writeDouble(netpay);
        raf.close();
    }

    /**
     * reads each character of a string and returns it as a whole
     *
     * @param raf random access file
     * @param size size of each record's item predetermined by us
     * @return one a string from the characters read
     * @throws IOException
     */
    public String readString(RandomAccessFile raf, int size) throws IOException {
        String n = "";
        for (int i = 0; i < size; i++) {
            n += String.valueOf(raf.readChar());
        }
        return n;
    }

    /**
     * reads information in the file and creates them as a new employee instance
     * then adds it into an observable list and passes it
     * @return the employee list read from the file
     * @throws IOException 
     */
    public ObservableList<Employee> readFile() throws IOException {
        ObservableList<Employee> emList = FXCollections.observableArrayList();
        file = new File("employees.dat");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        long empNum = raf.length() / EMPLOYEE_SIZE;
        for (int i = 0; i < empNum; i++) {
            emList.add(new Employee(readString(raf, FIELD_SIZE),
                readString(raf, FIELD_SIZE), raf.readDouble(), raf.readDouble(),
                raf.readDouble()));

        }
        return emList;
    }

    /**
     * adds extra spaces or removes extra characters if it exceeds the preset
     * value of each string to be passed to keep the sizes consistent
     * @param str the string entered
     * @param size the predetermined size
     * @return 
     */
    public static String prepString(String str, int size) {
        if (str.length() < size) {
            int numOfSpaces = size - str.length();
            for (int i = 1; i <= numOfSpaces; i++) {
                str += " ";
            }
        } else {
            str = str.substring(0, size);
        }
        return str;
    }

}
