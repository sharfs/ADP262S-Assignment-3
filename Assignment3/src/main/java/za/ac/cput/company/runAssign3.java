/**
 * runAssign3.java
 * @author Sharfaa Sedick Anthony 220041571
 * Date: 07 June 2021
 */

package za.ac.cput.company;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public class runAssign3{
    private ObjectInputStream input;
    
    ArrayList<Customer> cust = new ArrayList<Customer>();
    ArrayList<Supplier> supp = new ArrayList<Supplier>();
    
    public void openFile(){
        try{
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("** ser file opned for reading **");
        }
        catch(IOException ioe){
            System.out.println("error opening ser file: " + ioe.getMessage());
        }
    }
    
    public void closeFile(){
        try{
        input.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("error closing ser file: " + ioe.getMessage());
        }        
    }  
    
    public void readFromFile(){
        try{
            Object obj = null;
            while(!(obj = input.readObject()).equals(null)){
                if(obj instanceof Customer){
                    cust.add((Customer) obj);
                    System.out.println("Adding To Customer: " + ((Customer) obj).getFirstName());
                }
                if(obj instanceof Supplier){
                    supp.add((Supplier) obj);
                    System.out.println("Adding To Supplier: " +((Supplier) obj).getName());
                }
            }
            System.out.println("File Read Complete");
        }
        catch (ClassNotFoundException ioe){
            System.out.println("class error reading ser file: " + ioe);
        }
        catch (IOException ioe){
            System.out.println("End of File");
        }
        finally{
            closeFile(); 
        }
    }
    
    public void sortCustomerByStakeholder() {
        Collections.sort(cust, (c1, c2) -> {
           return c1.getStHolderId().compareTo(c2.getStHolderId()); 
        });
    }
    
    private int getAge(String date) {
        LocalDate d1 = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.now();
        return Period.between(d1, d2).getYears();
    }
    
    public String reformatBirthDate(Customer c) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(c.getDateOfBirth());
            return dateFormat.format(dob);
        } catch (Exception e) {
            System.out.println("Error Converting Date");
        }
        return null;
    }
    
    public void writeCustomersToFile(){
        try{
            FileWriter writer = new FileWriter("customerOutFile.txt"); 
            writer.write("============================ CUSTOMERS ============================\n");
            writer.write(String.format("%-10s\t%-10s\t%-10s\t%-15s\t%-10s\n", "ID", "Name", "Surname", "Date of birth", "Age"));
            writer.write("===================================================================\n");
            for(Customer cust: cust) {
                String output = String.format("%-10s\t%-10s\t%-10s\t%-15s\t%-10s",cust.getStHolderId(), cust.getFirstName(), cust.getSurName(), reformatBirthDate(cust), getAge(cust.getDateOfBirth()));
                writer.write(output + "\n");
            }
            writer.write("\nNumber of customers who can rent: " + cust.stream().filter(Customer::getCanRent).collect(Collectors.toList()).size() + "\n");
            writer.write("\nNumber of customers who cannot rent: " + cust.stream().filter(c -> !c.getCanRent()).collect(Collectors.toList()).size());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Writing to file");
        }
    }
    
    public void sortSuppliers(){
        Collections.sort(supp, (c1, c2) -> {
           return c1.getName().compareTo(c2.getName()); 
        });
    }
    
    public void writeSuppliersToFile(){
                try{
            FileWriter writer = new FileWriter("supplierOutFile.txt"); 
            writer.write("========================== SUPPLIERS  ============================\n");
            writer.write(String.format("%-10s\t%-20s\t%-15s\t%-15s\n", "ID", "Name", "Prod Type", "Description"));
            writer.write("==================================================================\n");
            for(Supplier supp: supp) {
                String output = String.format("%-10s\t%-20s\t%-15s\t%-15s",supp.getStHolderId(), supp.getName(), supp.getProductType(), supp.getProductDescription());
                writer.write(output + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error writing to file");
        }
    }
    
    public static void main(String[] args) {
        runAssign3 assign = new runAssign3();
        assign.openFile();
        assign.readFromFile();
        assign.sortCustomerByStakeholder();
        assign.writeCustomersToFile();
        assign.sortSuppliers();
        assign.writeSuppliersToFile();
    }
}
