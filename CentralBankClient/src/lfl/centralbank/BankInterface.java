/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.util.Map;

/**
 *
 * @author Luisito
 */
public interface BankInterface {

    public static int CLOSE_ACCOUNT = 0;
    public static int OPEN_ACCOUNT = 1;
    public static int CHECK_BALANCE = 2;
    public static int MAKE_DEPOSIT = 3;
    public static int WITHDRAW_FUNDS = 4;
    public static int LIST_ACCOUNTS = 5;

    String closeAccount(CustomerAccount customerAccount);

    //double getAccountBalance();
    
    //double getAccountBalance(String customerName, String accountNumber);

    String getAccountBalance(CustomerAccount customerAccount);

    int getBankId();

    String getBankName();

    Map<String, CustomerAccount> getCustomersList();

    String makeDeposit(CustomerAccount customerAccount, double depositAmount);

    String openAccount(CustomerAccount customerAccount);

    void setBankId(int bankId);

    void setBankName(String bankName);

    void setCustomersList(Map<String, CustomerAccount> customersList);

    String withdrawFunds(CustomerAccount customerAccount, double withdrawalAmount);

}
