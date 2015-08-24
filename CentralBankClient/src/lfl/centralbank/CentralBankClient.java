package lfl.centralbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CentralBankClient {

    /*
     * For simplicity purposes, random number value generation capped at 100.
     */
    private static int MAX_RANDOM = 100;
    public static int REGISTER_SERVICE = 1;
    public static int REMOVE_SERVICE = 2;
    public static int LOOKUP_SERVICE = 3;
    //public static int NAME_SERVICE_PORT = 5555;
    public static String NAME_SERVICE_IP = "129.115.26.222";
    public static int NAME_SERVICE_PORT = 4444;
    
    private Service bankService;

    /*
     * Start client. You may specify # of client requests to make or use
     * the default value of 10.
     */
    public static void main(String[] args) {

        CentralBankClient bankClient = new CentralBankClient();

    }

    /*
     * This method implements both requirements for the base project
     * (with the .5 second wait) and the performance measurements
     * for the EXTRA CREDIT.
     */

    public CentralBankClient(){

        //BankProxy bankProxy = null;

        /*try {*/
            //Lookup BANK service
            //bankService = lookupService();
        	//Performs the lookup of the BANK service on Navid's name service
    		bankService = new Service();
        	lookup();
            System.out.println("CentralBankClient found service " + bankService.getServiceName() + "@ " + bankService.getServiceIP() + " port " + bankService.getServicePort());
        /*} catch (ClassNotFoundException ex) {
            Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        mainMenu();
        
        /*for (int i=0;i < number_of_requests; i++){
        Thread t = new Thread(this);
        t.start();
        /*
         * Comment out the try-catch block below to run the code with
         * the performance measurements for extra credit.
         *
        try {
        this.wait(500);
        } catch (InterruptedException ex) {
        Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }/
        }*/


    }

    private CustomerAccount getAccountInfo(){

        //prompt the user to enter their name
        //Customer Account
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setBankId(selectBank());
        customerAccount.setAccountBalance(0.0);
        customerAccount.setAccountStatus(false);
        
        System.out.println("");
        System.out.print("Full Name: ");
        //open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        while (input ==  null || input.equals("")){
            try {
                input = br.readLine();
                if (input == null || input.equals("")){
                    System.out.print("Invalid name. Please try again!");
                }else{
                    customerAccount.setCustomerName(input);
                }
            } catch (IOException ex) {
                System.out.print("Error getting name!");
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Amount to open account with
        System.out.print("Account Number: ");
        //  open up standard input
        br = new BufferedReader(new InputStreamReader(System.in));
        String input2 = null;
        
        while (input2 ==  null || input.equals("")){
            try {
                input2 = br.readLine();
                if (input2 == null || input2.equals("")){
                    System.out.print("Invalid name. Please try again!");
                }else{
                    customerAccount.setAccountNumber(input2);
                }
            } catch (IOException ex) {
                System.out.print("Error getting name!");
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return customerAccount;

    }

    private CustomerAccount getOpenAccountInput(){

        System.out.println("");
        System.out.println("######################### OPEN ACCOUNT ##########################");
        System.out.println("");
        //prompt the user to enter their name
        //Customer Account
        CustomerAccount newCustomerAccount = new CustomerAccount();
        newCustomerAccount.setBankId(selectBank());
        System.out.print("Full Name: ");
        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        while (input ==  null || input.equals("")){
            try {
                input = br.readLine();
                if (input == null || input.equals("")){
                    System.out.print("Invalid name. Please try again!");
                }else{
                    newCustomerAccount.setCustomerName(input);
                }
            } catch (IOException ex) {
                System.out.print("Error getting name!");
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Amount to open account with
        System.out.print("Initial Deposit Amount ($US): ");
        double initialDeposit = 0.00;
        //  open up standard input       
        br = new BufferedReader(new InputStreamReader(System.in));

        try {
             input = br.readLine();
             if (input == null || input.equals("")){
                 System.out.print("Field left blank.");
                 newCustomerAccount.setAccountBalance(initialDeposit);
             }else{
                 //Check if input is double (not strings and such)
                 if(isDouble(input)){
                    newCustomerAccount.setAccountBalance(Double.valueOf(input));
                    System.out.println("");
                    System.out.print("Initial amount of $" + newCustomerAccount.getAccountBalance() + " accepted.\n");//
                 }else{
                    System.out.println("");
                    System.out.print("Opening account with zero balance (default).\n");
                    System.out.println("");
                    newCustomerAccount.setAccountBalance(initialDeposit);
                 }
             }
             //System.out.print("Initial account balance of $" + newCustomerAccount.getAccountBalance() + "\n");
        } catch (IOException ex) {
            System.out.println("");
            System.out.print("Error getting input!");
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Create a random account number with a max # of 99999
        newCustomerAccount.setAccountNumber(String.valueOf(new Random().nextInt(99999)));
        //Set the account status to OPEN (true)
        newCustomerAccount.setAccountStatus(true);

        return newCustomerAccount;

    }

    private boolean isDouble(String number){

        try{
            Double.valueOf(number);
        }catch(Exception e){
            return false;
        }

        return true;
    }

    public boolean isInteger(String number){

        try{
            Integer.getInteger(number);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    private int selectBank(){

        CentralBankInterface centralBankInt = null;
        Bank bank = null;
        Object result = null;
        centralBankInt = (CentralBankInterface)Proxy.newProxyInstance(
        CentralBankInterface.class.getClassLoader(),
            new Class[]{CentralBankInterface.class},
            new CentralBankProxy(new CentralBankImpl(getBankService())));

        result = centralBankInt.availableBanks();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("");
        System.out.println("-------------- Available Banks ------------");
        System.out.println("");
        System.out.println(result.toString());
        System.out.println("");
        System.out.println("Please select your Banking Institution: ");
        int selection = 0;
        try {
            selection = Integer.valueOf(br.readLine());
        } catch (IOException ex) {
            Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        return selection;
    }
    private void mainMenu(){

        //getAvailableBanks();
        //prompt the user to enter their name
        //System.out.print("          Central Bank System Menu:\n");
        //System.out.println("");
        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int selection = -1;
        boolean loop = true;
        while (loop) {
            System.out.println("");
            System.out.print("########################### WELCOME #################################");
            System.out.println("");
            System.out.println("");
            System.out.print("          Central Bank System Menu:\n");
            System.out.println("");

            //  read the selectedOption from the command-line; need to use try/catch with the
            //  readLine() method
            System.out.println("            "+ BankInterface.OPEN_ACCOUNT + " - Open Account");
            System.out.println("            "+ BankInterface.CHECK_BALANCE + " - Check Balance");
            System.out.println("            "+ BankInterface.MAKE_DEPOSIT + " - Make a Deposit");
            System.out.println("            "+ BankInterface.WITHDRAW_FUNDS + " - Withdrawal");
            System.out.println("            "+ BankInterface.CLOSE_ACCOUNT + " - Close Account");
            //System.out.println(BankInterface.LIST_ACCOUNTS + " - List Accounts");//Hidden
            System.out.println(" ");
            System.out.println("            X - Exit");
            System.out.println(" ");
            try {

                System.out.print("Please select an option: ");

                String input = br.readLine();
                if (input.equalsIgnoreCase("x")){
                    loop = false;
                    System.out.println("Good bye!");
                    System.exit(0);
                }else{
                    if (isInteger(input)){
                        selection = Integer.valueOf(input);
                        processInput(selection);
                    }else{
                        System.out.println("");
                        System.out.println("Invalid selection. Please try again!\n");
                        System.out.println("");
                        mainMenu();
                    }
                }
            } catch (IOException e) {
                System.out.println("");
                System.out.println("Invalid selection. Please try again!\n");
                System.out.println("");
            }
        }
    }

 public void processInput(int selection){

     //reader used to pause the screen to wait for user to continue
     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
     try{
            // This fragment creates a proxy for a BankInterface object.
            BankInterface bankInt = null;
            // This fragment creates a proxy for a CustomerAccountInterface object.
            CustomerAccountInterface customerAccountInt = null;
            CustomerAccount customerAccount = null;//getOpenAccountInput();
            Bank bank = null;
            Object result = null;

            switch (selection) {
                case 0:
                    customerAccount = getAccountInfo();
                    bankInt = (BankInterface)Proxy.newProxyInstance(
                    BankInterface.class.getClassLoader(),
                    new Class[]{BankInterface.class},
                    new BankProxy(new BankImpl(getBankService())));
                    result = bankInt.closeAccount(customerAccount);

                    System.out.println(" ");
                    System.out.println("--------- Account Status ---------- ");
                    System.out.println(" Acct# " + customerAccount.getAccountNumber());
                    System.out.println(" Name: " + customerAccount.getCustomerName());
                    System.out.println(" ");
                    System.out.println(result.toString());
                    System.out.println("----------------------------------- ");
                    System.out.println(" ");
                    System.out.println("Press ENTER to continue...");

                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 1:
                    //bankProxy = new BankProxy(selection, getOpenAccountInput(), bankService);
                    customerAccount = getOpenAccountInput();
                    bankInt = (BankInterface)Proxy.newProxyInstance(
                    BankInterface.class.getClassLoader(),
                    new Class[]{BankInterface.class},
                    new BankProxy(new BankImpl(getBankService())));
                    result = bankInt.openAccount(customerAccount);

                    System.out.println("");
                    System.out.println(result.toString());
                    System.out.println("");
                    System.out.println("Press ENTER to continue...");
                    System.out.println("");
                    /*if (result.equals("true")){
                        System.out.println("");
                        System.out.println("---------- New Account Information ---------");
                        System.out.println(" Acct# " + customerAccount.getAccountNumber());
                        System.out.println(" Name: " + customerAccount.getCustomerName());
                        System.out.println(" ");
                        System.out.println(" Balance: $" + result.toString());
                        System.out.println("----------------------------------- ");
                        System.out.println(" ");

                    }*/
                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 2:

                    System.out.println("");
                    System.out.println("######################### CHECK ACCOUNT BALANCE ##########################");
                    System.out.println("");

                    customerAccount = getAccountInfo();
                    bankInt = (BankInterface)Proxy.newProxyInstance(
                    BankInterface.class.getClassLoader(),
                    new Class[]{BankInterface.class},
                    new BankProxy(new BankImpl(getBankService())));
                    result = bankInt.getAccountBalance(customerAccount);

                    System.out.println(" ");
                    System.out.println("--------- Account Balance --------- ");
                    System.out.println(" Acct# " + customerAccount.getAccountNumber());
                    System.out.println(" Name: " + customerAccount.getCustomerName());
                    System.out.println(" ");
                    if (!result.toString().contains("Account Not Found")){
                        System.out.println(" Balance: $" + result.toString());
                    }else{
                        System.out.println(result.toString());
                    }
                    
                    System.out.println("----------------------------------- ");
                    System.out.println(" ");
                    System.out.println("Press ENTER to continue...");
                    
                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 3:
                    customerAccount = getAccountInfo();
                    bankInt = (BankInterface)Proxy.newProxyInstance(
                    BankInterface.class.getClassLoader(),
                    new Class[]{BankInterface.class},
                    new BankProxy(new BankImpl(getBankService())));

                    br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter deposit amount: $");
                    double depositAmount = 0;
                    try {
                        depositAmount = Double.parseDouble(br.readLine());
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    result = bankInt.makeDeposit(customerAccount, depositAmount);

                    System.out.println(" ");
                    System.out.println("--------- Account Balance --------- ");
                    System.out.println(" Acct# " + customerAccount.getAccountNumber());
                    System.out.println(" Name: " + customerAccount.getCustomerName());
                    System.out.println(" ");
                    System.out.println(result.toString());
                    System.out.println("----------------------------------- ");
                    System.out.println(" ");
                    System.out.println("Press ENTER to continue...");

                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 4:
                    customerAccount = getAccountInfo();
                    bankInt = (BankInterface)Proxy.newProxyInstance(
                    BankInterface.class.getClassLoader(),
                    new Class[]{BankInterface.class},
                    new BankProxy(new BankImpl(getBankService())));

                    br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Enter withdrawal amount: $");
                    double withdrawalAmount = 0;
                    try {
                        withdrawalAmount = Double.parseDouble(br.readLine());
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    result = bankInt.withdrawFunds(customerAccount, withdrawalAmount);

                    System.out.println(" ");
                    System.out.println("--------- Account Balance --------- ");
                    System.out.println(" Acct# " + customerAccount.getAccountNumber());
                    System.out.println(" Name: " + customerAccount.getCustomerName());
                    System.out.println(" ");
                    System.out.println(result.toString());
                    System.out.println("----------------------------------- ");
                    System.out.println(" ");
                    System.out.println("Press ENTER to continue...");

                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                default:
                    System.out.println("");
                    System.out.println("Invalid selection. Please try again!\n");
                    System.out.println("");
                    break;
            }

            //System.out.println("Result received by client: " + result.toString());
      }catch(RuntimeException e){
        System.out.println("Error " + e.getMessage());
          //mainMenu();
      }
      //Go back to main menu
      //mainMenu();
    }
    

    public Service getBankService(){
        return bankService;
    }

    //Find BANK service using strings
    
    private void lookup() {
    	
    	String message = "";
		/*System.out
				.println("Give nameservice IP address or press enter to be local host:");*/
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			/*NAME_SERVICE_IP = reader.readLine();
			// reader.close();
			if (nameServiseIP.equals("")) {
				nameServiseIP = "localhost";
			}*/
			Socket socket = new Socket(NAME_SERVICE_IP, NAME_SERVICE_PORT);
			ObjectOutputStream outputStream = new ObjectOutputStream(
					socket.getOutputStream());
			message = "invoke" + "," + "BANK";
			outputStream.writeObject(message);
			ObjectInputStream inputStream = new ObjectInputStream(
					socket.getInputStream());
			message = (String) inputStream.readObject();
			StringTokenizer tokenizer = new StringTokenizer(message, ",");
			String resultString;
			resultString = tokenizer.nextToken();
			if (resultString.equals("result")) {
				//System.out.println(tokenizer.nextToken());
				bankService.setServiceName(tokenizer.nextToken());
				bankService.setServiceIP(tokenizer.nextToken());
				bankService.setServicePort(Integer.valueOf(tokenizer.nextToken()));
				//System.out.println(tokenizer.nextToken());
				//System.out.println(tokenizer.nextToken());
				//Integer.parseInt(tokenizer.nextToken())
				//bankService.setServiceIP(tokenizer.nextToken());
				//bankService.setServicePort(Integer.parseInt(tokenizer.nextToken()));
				/*System.out.println("IP and Port invoked from NameService");
				System.out.println("IP is:" + bankService.getServiceIP() + " and Port is:"
						+ bankService.getServicePort() + " .");*/
			} else if (resultString.equals("noresult")) {
				System.err.println("The IP and Port can not be found.");
			}
			outputStream.close();
			inputStream.close();
			socket.close();
		}catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Wrong IP address!");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
    
    
    /*
     * Find the BANK service (using Service object)
     */

    private Service lookupService() throws ClassNotFoundException{

        Object result = new SerializableObject();

            /*
             * Connect to Naming Service
             */
            //InetAddress host = InetAddress.getLocalHost();
            Socket socket = null;
            try {
                socket = new Socket(CentralBankClient.NAME_SERVICE_IP, CentralBankClient.NAME_SERVICE_PORT);
            } catch (IOException ex) {
                System.out.println("");
                System.out.println("Name Service is not available! Please verify it is running...");
                System.out.println("");
                //Logger.getLogger(CentralBankServer.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }

            System.out.println("");
            System.out.println("Searching for service...");
            System.out.println("");

            /*
             * Lookup Central Bank Service (BANK)
             */

            List<Service> findService = new ArrayList<Service>();
            findService.add(new Service("BANK"));

            HashMap<Integer,List> requestAction = new HashMap<Integer,List>();
            requestAction.put(CentralBankClient.LOOKUP_SERVICE, findService);

            ObjectOutputStream o_str = null;

            try {

                o_str = new ObjectOutputStream(socket.getOutputStream());
                o_str.writeObject(requestAction);

            /*
             * Close input and output streams
             */

            } catch (IOException ex) {
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*
             * Get the result from the server and print it out on the console
             */
            ObjectInputStream i_str = null;
            try {
                i_str = new ObjectInputStream(socket.getInputStream());
                result = i_str.readObject();
            } catch (IOException ex) {
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(CentralBankClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            
        return (Service)result;
    }

}
