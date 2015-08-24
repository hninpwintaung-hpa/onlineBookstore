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
        //System.out.println("Instantiating a new bank object with values: " + bankId + ", " + bankName);
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

    public String openAccount(CustomerAccount customerAccount){
        String message = "";
        if (customersList == null){
            this.customersList = new HashMap<String,CustomerAccount>();
        }
        try{
            this.customersList.put(customerAccount.getAccountNumber(),customerAccount);
        }catch(Exception e){
            message = "Failed to create new account!";
        }
        message = "Customer " + customerAccount.getCustomerName() + " with acct# " + customerAccount.getAccountNumber() + " added successfully!";
        return message;
    }
    
    public String closeAccount(CustomerAccount customerAccount){
       String message = "";
       if (customersList.containsKey(customerAccount.getAccountNumber())){
          (customersList.get(customerAccount.getAccountNumber())).setAccountStatus(false);
          System.out.println("Account " + customerAccount.getAccountNumber() + " has been closed!");
          message = "Account " + customerAccount.getAccountNumber() + " has been closed!\n";
          message = message + "If you have an available balance you may withdraw up to that amount.\n";
          message = message + "We are sorry to see you leave - come back soon!";
       }else{
          System.out.println("Account Not Found!");
          message = "Account Not Found!";
       }

       return message;
    }

    public double getAccountBalance(String customerName, String accountNumber) throws RuntimeException{
        double balance = 0.0;
        if (customersList.containsKey(accountNumber) && ((CustomerAccount)customersList.get(accountNumber)).getCustomerName().equalsIgnoreCase(customerName)){
            balance = ((CustomerAccount)customersList.get(accountNumber)).getAccountBalance();
        }else{
            System.out.println("Account Not Found!");
            throw new RuntimeException("Account Not Found!");
        }
        return balance;
    }

    public String getAccountBalance(CustomerAccount customerAccount){
        String message = "";
        double balance = 0.0;
        if (customersList.containsKey(customerAccount.getAccountNumber()) && ((CustomerAccount)customersList.get(customerAccount.getAccountNumber())).getCustomerName().equalsIgnoreCase(customerAccount.getCustomerName())){
                balance = ((CustomerAccount)customersList.get(customerAccount.getAccountNumber())).getAccountBalance();
                message = String.valueOf(balance);
        }else{
            System.out.println("Account Not Found!");
            message = "Account Not Found!";
        }
        return message;
    }

    public synchronized String makeDeposit(CustomerAccount customerAccount, double depositAmount){
           
        double tmpBalance = 0.0;
        String message = "";

            if (customersList.containsKey(customerAccount.getAccountNumber())){
              CustomerAccount tmp = (CustomerAccount)customersList.get(customerAccount.getAccountNumber());

              if (tmp.getAccountStatus()){
                  tmpBalance = tmp.getAccountBalance();
                  tmp.setAccountBalance(depositAmount + tmpBalance);
                  customersList.put(customerAccount.getAccountNumber(), tmp);
                  System.out.println("Deposit Successful - Account # " + customerAccount.getAccountNumber());
                  System.out.println("Previous balance: " + tmpBalance);
                  System.out.println("Deposit amount: " + depositAmount);
                  System.out.println("New balance: " + tmp.getAccountBalance());
                  message = "Deposit Successful - Account # " + customerAccount.getAccountNumber() + "\n";
                  message = message + "Previous balance: " + tmpBalance + "\n";
                  message = message + "Deposit amount: " + depositAmount + "\n";
                  message = message + "New balance: " + tmp.getAccountBalance() + "\n";
            }else{
                message = "- Unable to make a deposit -\n";
                message = message + "Your account is closed\n";
                message = message + "You may only withdraw funds up to your available balance of $" + tmp.getAccountBalance();
            }
           }else{
              System.out.println("Account Not Found!");
              message = "Account Not Found!";
           }

        return message;
    }

    public synchronized String withdrawFunds(CustomerAccount customerAccount, double withdrawalAmount){

        double tmpBalance = 0.0;
        String message = "";
            if (customersList.containsKey(customerAccount.getAccountNumber())){
              CustomerAccount tmp = (CustomerAccount)customersList.get(customerAccount.getAccountNumber());
              tmpBalance = tmp.getAccountBalance();
              if ((tmpBalance - withdrawalAmount) < 0){
                  System.out.println("Insufficient Funds - Account # " + customerAccount.getAccountNumber());
                  System.out.println("Balance: $" + tmpBalance);
                  System.out.println("Withdrawal amount: $" + withdrawalAmount);

                  message = "Insufficient Funds - Account # " + customerAccount.getAccountNumber() + "\n";
                  message = message + "Balance: $" + tmpBalance + "\n";
                  message = message + "Withdrawal amount: $" + withdrawalAmount + "\n";

              }else{
                  tmp.setAccountBalance(tmpBalance - withdrawalAmount);
                  customersList.put(customerAccount.getAccountNumber(), tmp);
                  System.out.println("Withdrawal Successful - Account # " + customerAccount.getAccountNumber());
                  System.out.println("Previous balance: $" + tmpBalance);
                  System.out.println("Withdrawal amount: $" + withdrawalAmount);
                  System.out.println("New balance: $" + tmp.getAccountBalance());
                  message = "Withdrawal Successful - Account # " + customerAccount.getAccountNumber() + "\n";
                  message = message + "Previous balance: $" + tmpBalance + "\n";
                  message = message + "Withdrawal amount: $" + withdrawalAmount + "\n";
                  message = message + "New balance: $" + tmp.getAccountBalance() + "\n";
              }
           }else{
              System.out.println("Account Not Found!");
              message = "Account Not Found";
           }

        return message;
    }
    
}
