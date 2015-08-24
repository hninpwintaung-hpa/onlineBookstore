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
public class CentralBankImpl implements Serializable, CentralBankInterface{

    private Service bankService;
    private BankComm comm;

    public CentralBankImpl(Service bankService){
        this.bankService = bankService;
    }


    public String availableBanks() {
        String message_in = "availableBanks";
        String message_out = "";
        //Communication module is instantiated to access remote server
        comm = new BankComm(bankService, message_in);
        //success = Boolean.valueOf(comm.getResponse().toString());
        message_out = comm.getResponse().toString();
        //System.out.println("success = " + success);
        return message_out;
    }

    
}
