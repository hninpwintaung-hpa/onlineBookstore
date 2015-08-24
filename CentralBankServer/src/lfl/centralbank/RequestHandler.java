/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Private class RequestHandler receives the client connections
 * and processes the input stream as well as returning the output
 * to the client
 */
class RequestHandler implements Runnable {

    private Socket socket;
    public static int ADD = 0;
    public static int SUBSTRACT = 1;
    public static int MULTIPLY = 2;
    public static int DIVIDE = 3;
    public static int LOGARITHM = 4;
    public static int COSINE = 5;
    
    public RequestHandler(Socket socket) {
        
        this.socket = socket;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {

        ObjectOutputStream o_str = null;
        ObjectInputStream i_str = null;

        try
        {
            /*
             * Get input form client request
             */
            i_str = new ObjectInputStream(socket.getInputStream());
            //SerializableObject message_in = new SerializableObject();
            //message_in.setObject(i_str.readObject());
            String message_in = i_str.readObject().toString();
            //String message_in = "";
            String message_out = "";
            //message_in = i_str.readObject().toString();
            //String received_string = (((SerializableObject)message_in.getObject())).toString();

            System.out.println("This is message_in in RequestHandler: " + message_in);
            
            String[] result = message_in.split(":");
            String action = "";
            //System.out.println("This is result.length: " + result.length);
            CustomerAccount custAcct = new CustomerAccount();

            //Parm used for some options like makeDeposit or withdraw
            double parm6 = 0;

            for (int x=0; x<result.length; x++){

                switch(x){
                    case 0: if (result[x] != null){action = result[x];} break;
                    case 1: if (result[x] != null){custAcct.setBankId(Integer.valueOf(result[x]));} break;
                    case 2: if (result[x] != null){custAcct.setAccountNumber(result[x]);} break;
                    case 3: if (result[x] != null){custAcct.setCustomerName(result[x]);} break;
                    case 4: if (result[x] != null){custAcct.setAccountStatus(Boolean.valueOf(result[x]));} break;
                    case 5: if (result[x] != null){custAcct.setAccountBalance(Double.parseDouble(result[x].trim()));} break;
                    case 6: if (result[x] != null){parm6 = Double.parseDouble(result[x].trim());} break;
                    default: System.out.println("Unknown property specified!"); break;
                }

            }

            if (action.equals("getAccountBalance")){
               //message_out = String.valueOf(CentralBankServer.getInstance().getAccountBalance(1, custAcct.getCustomerName(), custAcct.getAccountNumber()));
               message_out = String.valueOf(((Bank)CentralBankServer.getInstance().availableBanks().get(custAcct.getBankId())).getAccountBalance(custAcct));
            }else if (action.equals("openAccount")){
               //Bank bank = (Bank)CentralBankServer.getInstance().availableBanks().get(1);
               message_out = String.valueOf(((Bank)CentralBankServer.getInstance().availableBanks().get(custAcct.getBankId())).openAccount(custAcct));
               //message_out = String.valueOf(bank.openAccount(custAcct));
               //message_out = String.valueOf()
            }else if (action.equals("makeDeposit")){
               //Bank bank = (Bank)CentralBankServer.getInstance().availableBanks().get(1);
               message_out = String.valueOf(((Bank)CentralBankServer.getInstance().availableBanks().get(custAcct.getBankId())).makeDeposit(custAcct, parm6));
               //message_out = String.valueOf(bank.openAccount(custAcct));
               //message_out = String.valueOf()
            }else if (action.equals("withdrawFunds")){
               //Bank bank = (Bank)CentralBankServer.getInstance().availableBanks().get(1);
               message_out = String.valueOf(((Bank)CentralBankServer.getInstance().availableBanks().get(custAcct.getBankId())).withdrawFunds(custAcct, parm6));
               //message_out = String.valueOf(bank.openAccount(custAcct));
               //message_out = String.valueOf()
            }else if (action.equals("closeAccount")){
               //Bank bank = (Bank)CentralBankServer.getInstance().availableBanks().get(1);
               message_out = String.valueOf(((Bank)CentralBankServer.getInstance().availableBanks().get(custAcct.getBankId())).closeAccount(custAcct));
               //message_out = String.valueOf(bank.openAccount(custAcct));
               //message_out = String.valueOf()
            }else if (action.equals("availableBanks")){
                Map availableBanks = CentralBankServer.getInstance().availableBanks();
                Iterator it = availableBanks.values().iterator();
                while (it.hasNext()){
                    Bank tmpBank = (Bank)it.next();
                    message_out = message_out + "\n" + tmpBank.getBankId() + " - " + tmpBank.getBankName();
                    System.out.println(message_out);
                }
                //Set keySet = ((Bank)CentralBankServer.getInstance().availableBanks().keySet();
                
                //Bank bank = (Bank)CentralBankServer.getInstance().availableBanks().get(1);
               //message_out = String.valueOf(((Bank)CentralBankServer.getInstance().availableBanks());
               //message_out = String.valueOf(bank.openAccount(custAcct));
               //message_out = String.valueOf()
            }else{
               message_out = "Unknown method specified";
            }
            //System.out.println("Received object: " + (((SerializableObject)message_in.getObject())).toString());
            //CustomerAccount custAcct = new CustomerAccount("Billy Dean","0101",true,111230.00);
            //message_out.setObject(custAcct);
            //message_out.setObject("This is a response from the server!");

            //message_out = "This is a response from the server!";
            //message_out = String.valueOf(balance);
            /*
             * Send the result to the client
             */
            o_str = new ObjectOutputStream(socket.getOutputStream());
            o_str.writeObject(message_out);

            i_str.close();
            o_str.close();
            socket.close();

            System.out.println("Server ready for more requests...");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            if (o_str != null){
                try {
                    o_str.close();
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (i_str != null){
                try {
                    i_str.close();
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
