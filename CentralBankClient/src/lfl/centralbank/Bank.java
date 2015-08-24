/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luisito
 */
public class Bank implements Serializable{

    private int bankId;
    private String bankName;
    private Map<String,CustomerAccount> customersList;

    /* Constructor */
    public Bank(){
        customersList = new HashMap<String,CustomerAccount>();
    }
    /* Constructor */
    public Bank(String bankName){
        this.bankName = bankName;
        customersList = new HashMap<String,CustomerAccount>();
    }
    /* Constructor */
    public Bank(int bankId, String bankName, Map<String,CustomerAccount> customersList){
        System.out.println("Instantiating a new bank object with values: " + bankId + ", " + bankName);
        this.bankId = bankId;
        this.bankName = bankName;
        this.customersList = new HashMap<String,CustomerAccount>(customersList);
    }

    public void setBankId(int bankId){
        this.bankId = bankId;
    }

    public int getBankId(){
        return bankId;
    }

    public void setBankName(String bankName){
        this.bankName = bankName;
    }

    public String getBankName(){
        return bankName;
    }

    public void setCustomersList(Map<String,CustomerAccount> customersList){
        this.customersList = new HashMap<String,CustomerAccount>(customersList);
    }

    public Map<String,CustomerAccount> getCustomersList(){
        return customersList;
    }

    public boolean openAccount(CustomerAccount customerAccount){
        if (customersList == null){
            this.customersList = new HashMap<String,CustomerAccount>();
        }
        try{
            this.customersList.put(customerAccount.getAccountNumber(),customerAccount);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    public boolean closeAccount(CustomerAccount customerAccount){
       if (customersList.containsKey(customerAccount.getAccountNumber())){
          (customersList.get(customerAccount.getAccountNumber())).setAccountStatus(false);
          System.out.println("Account " + customerAccount.getAccountNumber() + " has been closed!");
          return true;
       }else{
          System.out.println("Account Not Found!");
          return false;
       }
    }

    public double getAccountBalance(String customerName, String accountNumber){
        double balance = 0.0;
        if (customersList.containsKey(accountNumber)){
            balance = ((CustomerAccount)customersList.get(accountNumber)).getAccountBalance();
        }else{
            System.out.println("Account Not Found!");
        }
        return balance;
    }

    public double getAccountBalance(CustomerAccount customerAccount){
        double balance = 0.0;
        if (customersList.containsKey(customerAccount.getAccountNumber())){
            balance = ((CustomerAccount)customersList.get(customerAccount.getAccountNumber())).getAccountBalance();
        }else{
            System.out.println("Account Not Found!");
        }
        return balance;
    }

    public synchronized boolean makeDeposit(CustomerAccount customerAccount, double depositAmount){
           
        double tmpBalance = 0.0;

            if (customersList.containsKey(customerAccount.getAccountNumber())){
              CustomerAccount tmp = (CustomerAccount)customersList.get(customerAccount.getAccountNumber());
              tmpBalance = tmp.getAccountBalance();
              tmp.setAccountBalance(depositAmount + tmpBalance);
              customersList.put(customerAccount.getAccountNumber(), tmp);
              System.out.println("Deposit Successful - Account # " + customerAccount.getAccountNumber());
              System.out.println("Previous balance: " + tmpBalance);
              System.out.println("Deposit amount: " + depositAmount);
              System.out.println("New balance: " + tmpBalance);
              return true;
           }else{
              System.out.println("Account Not Found!");
              return false;
           }
    }

    public synchronized String withdrawFunds(CustomerAccount customerAccount, double withdrawalAmount){

        double tmpBalance = 0.0;

            if (customersList.containsKey(customerAccount.getAccountNumber())){
              CustomerAccount tmp = (CustomerAccount)customersList.get(customerAccount.getAccountNumber());
              tmpBalance = tmp.getAccountBalance();
              if ((tmpBalance - withdrawalAmount) < 0){
                  System.out.println("Insufficient Funds - Account # " + customerAccount.getAccountNumber());
                  System.out.println("Balance: $" + tmpBalance);
                  System.out.println("Withdrawal amount: $" + withdrawalAmount);
                  return "Insufficient Funds";
              }else{
                  tmp.setAccountBalance(tmpBalance - withdrawalAmount);
                  customersList.put(customerAccount.getAccountNumber(), tmp);
                  System.out.println("Withdrawal Successful - Account # " + customerAccount.getAccountNumber());
                  System.out.println("Previous balance: $" + tmpBalance);
                  System.out.println("Withdrawal amount: $" + withdrawalAmount);
                  System.out.println("New balance: $" + tmpBalance);
                  return "Withdrawal Successful";
              }
           }else{
              System.out.println("Account Not Found!");
              return "Account Not Found";
           }
    }
    
}
