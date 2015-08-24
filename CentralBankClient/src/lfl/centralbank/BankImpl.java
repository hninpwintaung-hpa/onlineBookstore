/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Luisito
 */
public class BankImpl implements Serializable, BankInterface{

    private Service bankService;
    private BankComm comm;

    public BankImpl(Service bankService){
        this.bankService = bankService;
    }
    
    public String closeAccount(CustomerAccount customerAccount) {
        //boolean success = false;
        String message_in = "closeAccount:" + customerAccount.toString();
        String message_out = "";
        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);
        //success = Boolean.valueOf(comm.getResponse().toString());
        message_out = comm.getResponse().toString();
        //System.out.println("success = " + success);
        return message_out;
    }

    public double getAccountBalance(String customerName, String accountNumber) {
        System.out.println("In BankImp.getAccountBalance()");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getBankId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getBankName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, CustomerAccount> getCustomersList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String makeDeposit(CustomerAccount customerAccount, double depositAmount) {
        //boolean success = false;
        String message_in = "makeDeposit:" + customerAccount.toString() + ":" + depositAmount;
        String message_out = "";
        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);
        //success = Boolean.valueOf(comm.getResponse().toString());
        message_out = comm.getResponse().toString();
        //System.out.println("success = " + success);
        return message_out;

    }

    public String openAccount(CustomerAccount customerAccount) {
        
        //boolean success = false;
        String message_in = "openAccount:" + customerAccount.toString();
        String message_out = "";
        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);
        //success = Boolean.valueOf(comm.getResponse().toString());
        message_out = comm.getResponse().toString();
        //System.out.println("success = " + success);
        return message_out;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBankId(int bankId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBankName(String bankName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCustomersList(Map<String, CustomerAccount> customersList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String withdrawFunds(CustomerAccount customerAccount, double withdrawalAmount) {
        //boolean success = false;
        String message_in = "withdrawFunds:" + customerAccount.toString() + ":" + withdrawalAmount;
        String message_out = "";
        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);
        //success = Boolean.valueOf(comm.getResponse().toString());
        message_out = comm.getResponse().toString();
        //System.out.println("success = " + success);
        return message_out;

    }

    public String getAccountBalance(CustomerAccount customerAccount){

        //double balance = 0.0;
        String message_out = "";
        String message_in = "getAccountBalance:" + customerAccount.toString();

        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);
        message_out = comm.getResponse().toString();
        //ystem.out.println("balance = " + balance);
        return message_out;

    }

    public double getAccountBalance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean closeAccount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean makeDeposit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean openAccount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBankId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBankName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCustomersList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String withdrawFunds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
