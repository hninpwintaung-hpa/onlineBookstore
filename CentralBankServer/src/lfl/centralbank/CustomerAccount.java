/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.io.Serializable;

/**
 *
 * @author Luisito
 */
public class CustomerAccount implements Serializable, CustomerAccountInterface {

    private int bankId;
    private String accountNumber;//00111
    private String customerName;
    private boolean accountStatus;//1=open,0=closed
    private double balance = 0.0;
    
    public CustomerAccount(){
        
    }

    public int getBankId(){
        return bankId;
    }

    public void setBankId(int bankId){
        this.bankId = bankId;
    }

    @Override
    public String toString(){
        return bankId + ":" + accountNumber + ":" + customerName + ":" + accountStatus + ":" + balance;
    }

    public CustomerAccount(int bankId, String customerName, String accountNumber, boolean accountStatus, double balance){
        this.bankId = bankId;
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
        this.balance = balance;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    public void setAccountStatus(boolean accountStatus){
        this.accountStatus = accountStatus;
    }

    public void setAccountBalance(double balance){
        this.balance = balance;
    }

    public String getCustomerName(){
        return customerName;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public boolean getAccountStatus(){
        return accountStatus;
    }

    public double getAccountBalance(){
        return balance;
    }
}
