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
    
    public boolean closeAccount(CustomerAccount customerAccount) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public boolean makeDeposit(CustomerAccount customerAccount, double depositAmount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean openAccount(CustomerAccount customerAccount) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getAccountBalance(CustomerAccount customerAccount){
        double balance = 0.0;
        
        /*
         * The message is sent to the server as a string to be tokenized
         */
        /*SerializableObject message_in = new SerializableObject();
        message_in.setObject(new String("getAccountBalance:" + customerAccount.toString()));*/

        String message_in = "getAccountBalance:" + customerAccount.toString();

        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);

        //SerializableObject message_out = new SerializableObject();
        
        /*/message_out = comm
        if (message_out.getObject() == null){
            System.out.println("message_out is NULL!: ");
        }else{
            System.out.println("message_out is NOT NULL!: " + message_out.getObject().toString());
        }*/
        //balance = ((CustomerAccount)message_out.getObject()).getAccountBalance();
        //balance = ((CustomerAccount)comm.getServerResponse()).getAccountBalance();
        return balance;

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
