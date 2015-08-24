/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

/**
 *
 * @author Luisito
 */
public interface CustomerAccountInterface {

    double getAccountBalance();

    String getAccountNumber();

    boolean getAccountStatus();

    String getCustomerName();

    void setAccountBalance(double balance);

    void setAccountNumber(String accountNumber);

    void setAccountStatus(boolean accountStatus);

    void setCustomerName(String customerName);

}
