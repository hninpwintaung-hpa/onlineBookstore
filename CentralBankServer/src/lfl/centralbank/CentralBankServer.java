package lfl.centralbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CentralBankServer implements CentralBankInterface {

    private static CentralBankServer instance = null;
    public static int CENTRAL_BANK_PORT = 7777;
    public static String CENTRAL_BANK_IP = "129.115.26.230";
    public static int REGISTER_SERVICE = 1;
    public static int REMOVE_SERVICE = 2;
    public static int LOOKUP_SERVICE = 3;
    //public static int NAME_SERVICE_PORT = 5555;
    public static int NAME_SERVICE_PORT = 4444;
    public static String NAME_SERVICE_IP = "127.0.0.1";

    private ServerSocket server;
    private List availableServices;
    private Map banksList;

    /*
     * Constructor
     */
    public CentralBankServer() {

        //Initialize array to hold registered services
        availableServices = new ArrayList();
         //Load banks
        loadBanks();
        //Load customer accounts onto banks
        loadCustomerAccounts();
        try {
            server = new ServerSocket(CENTRAL_BANK_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*
         * Start new instance of CentralBankServer and wait for connections
         */
        System.out.println("Starting CentralBankServer...");
        CentralBankServer server = CentralBankServer.getInstance();
        //try {
             //server.registerServices();
        //Modified to use Navid's name service
        	server.registerIP();
        /*} catch (ClassNotFoundException ex) {
            Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        server.waitForConnection();
    }

    public void waitForConnection() {
        /*
         * Loop waiting for new connections
         */
        
        System.out.println("Central Bank Server ready for requests...");
        
        while (true) {
            try {
                Socket socket = server.accept();
                new RequestHandler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBanks(){

        System.out.println("Loading Banks...");

        banksList = new HashMap<Integer,Bank>();
        //Hard-coded banks to load
        //banksList.put(1,new Bank(1,"Banco Santander", new HashMap<String,CustomerAccount>()));
        banksList.put(1,new Bank(1,"Capital One Bank", new HashMap<String,CustomerAccount>()));
        banksList.put(2,new Bank(2,"Citizens Bank", new HashMap<String,CustomerAccount>()));
        banksList.put(3,new Bank(3,"USAA Federal Savings Bank", new HashMap<String,CustomerAccount>()));
    }

    private void loadCustomerAccounts(){

        System.out.println("Loading Customer Accounts...");
        //Hard-coded customer accounts to load
        //String customerName, String accountNumber, boolean accountStatus, double balance
        //Capital One customers
        Map<String,CustomerAccount> customerList1 = new HashMap<String,CustomerAccount>();
        customerList1.put("0101",new CustomerAccount(1,"Billy Dean", "0101",true,238.03));
        customerList1.put("0132",new CustomerAccount(1,"Jackie Morris", "0132",true,15.37));
        customerList1.put("0111",new CustomerAccount(1,"Marcela Rivera", "0111",true,1564.98));

        //Citizens Bank customers
        Map<String,CustomerAccount> customerList2 = new HashMap<String,CustomerAccount>();
        customerList2.put("10201",new CustomerAccount(2,"Homer Simpson", "10201",true,-23.48));
        customerList2.put("10023",new CustomerAccount(2,"Mickey Mouse", "10023",true,28323.20));
        customerList2.put("10908",new CustomerAccount(2,"Donald Duck", "10908",true,983.20));

        //USAA customers
        Map<String,CustomerAccount> customerList3 = new HashMap<String,CustomerAccount>();
        customerList3.put("201223",new CustomerAccount(3,"G.I Joe", "201223",true,15433.48));
        customerList3.put("2383",new CustomerAccount(3,"Richard Roberts", "2383",true,0.48));
        customerList3.put("23849",new CustomerAccount(3,"Pamela Lee", "23849",true,8420.00));

        int size = availableBanks().size();

        //System.out.println("banksList size: " + size);
           for (int i=1; i <size; i++){
               if (((Bank)banksList.get(i)).getBankName().equals("Capital One Bank")){
                ((Bank)banksList.get(i)).setCustomersList(customerList1);
               }
               if (((Bank)banksList.get(i)).getBankName().equals("Citizens Bank")){
                ((Bank)banksList.get(i)).setCustomersList(customerList2);
               }
               if (((Bank)banksList.get(i)).getBankName().equals("USAA Federal Savings Bank")){
                ((Bank)banksList.get(i)).setCustomersList(customerList3);
               }
           }

    }

    public Map availableBanks(){
        return banksList;
    }

    private void registerIP() {
    	String message = "";
		// connects to the Nameservice and register the IP and port of the
		// bookstore
		/*System.out
				.println("Give nameservice IP address or press enter to be local host:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			NAME_SERVICE_IP = reader.readLine();
			if (NAME_SERVICE_IP.equals("")) {
				NAME_SERVICE_IP = "localhost";
			}*/
    	try{
			Socket nameServiceSocket = new Socket(NAME_SERVICE_IP,
					NAME_SERVICE_PORT);

			ObjectOutputStream outputStream = new ObjectOutputStream(
					nameServiceSocket.getOutputStream());
			InetAddress address = InetAddress.getLocalHost();
			message = "register" + "," + "BANK" + ","
					+ CENTRAL_BANK_IP + ","
					+ Integer.toString(CENTRAL_BANK_PORT);
			outputStream.writeObject(message);
			ObjectInputStream inputStream = new ObjectInputStream(
					nameServiceSocket.getInputStream());
			System.out.println("IP and port has been registerd.");
			outputStream.close();
			inputStream.close();
			nameServiceSocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Wrong IP address!");
			System.exit(1);
		}

	}
    
    private void registerServices() throws ClassNotFoundException{
        try {
            /*
             * Connect to Naming Service
             */
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = null;
            try {
                socket = new Socket(CentralBankServer.NAME_SERVICE_IP, CentralBankServer.NAME_SERVICE_PORT);
            } catch (IOException ex) {
                System.out.println("Name Service is not available! Please verify it is running...");
                System.out.println("Terminating server...");
                //Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
                System.out.println("Server terminated successfully");
            }

            System.out.println("Registering CentralBank service...");

            availableServices.add(new Service("BANK",CENTRAL_BANK_IP,CENTRAL_BANK_PORT));

            HashMap<Integer,List> requestAction = new HashMap<Integer,List>();
            requestAction.put(NameService.REGISTER_SERVICE, availableServices);
            //requestAction.put(NameService.REMOVE_SERVICE, availableServices);
            //requestAction.put(NameService.LOOKUP_SERVICE, availableServices);
            ObjectOutputStream o_str = null;

            try {

                o_str = new ObjectOutputStream(socket.getOutputStream());
                o_str.writeObject(requestAction);

            /*
             * Close input and output streams
             */

            } catch (IOException ex) {
                Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*
             * Get the result from the server and print it out on the console
             */
            ObjectInputStream i_str = null;
            try {
                i_str = new ObjectInputStream(socket.getInputStream());
                System.out.println("Name Service registration successful: " + ((Boolean)i_str.readObject()).toString());

            } catch (IOException ex) {
                Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
            }


            /*
             * Close input and output streams
             */

            try{
                if (i_str != null){
                    i_str.close();
                }
                if (o_str != null){
                    o_str.close();
                }
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
            }



        } catch (UnknownHostException ex) {
            Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static synchronized CentralBankServer getInstance(){
        if (instance == null){
            instance = new CentralBankServer();
        }
        return instance;
    }
}

