/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lfl.centralbank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luisito
 */
public class NameService {

    public static int REGISTER_SERVICE = 1;
    public static int REMOVE_SERVICE = 2;
    public static int LOOKUP_SERVICE = 3;
    public static int NAME_SERVICE_PORT = 5555;    
    private static ServerSocket server;
    private Map registeredServices;

    public static void main(String[] args) {
        /*
         * Start new instance of CentralBankServer and wait for connections
         */
        System.out.println("Starting Name Service...");
        NameService nameService = new NameService();
        nameService.waitForConnection();
    }

    public NameService(){
        registeredServices = new HashMap<String,Service>();
        try {
            server = new ServerSocket(NAME_SERVICE_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForConnection() {
    /*
     * Loop waiting for new connections
     */
     System.out.println("Name Service is ready to take requests...");
     listRegisteredServices();
     
        while (true) {
            try {
                Socket socket = server.accept();
                //new RequestHandler(socket);
                new RequestHandler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean registerService (Service service){
        System.out.println("Attempting to register service '" + service.getServiceName() + "'...");
        try{
            registeredServices.put(service.getServiceName(), service);            
        }catch(Exception e){
            System.out.println("Service '" + service.getServiceName() + "' failed to register successfully!");
            return false;
        }
        
        System.out.println("Service '" + service.getServiceName() + "' was registered successfully.");
        listRegisteredServices();
        return true;
    }

    public void listRegisteredServices(){
        System.out.println("---------------------------------------");
        if (registeredServices.isEmpty()){
            System.out.println("No services are currently registered");
        }else{
            System.out.println("There are " +  registeredServices.size() + " services registered:");
            System.out.println("---------------------------------------");
            Iterator it = registeredServices.values().iterator();
            while (it.hasNext()){
                Service tmp = ((Service)it.next());
                System.out.println(tmp.getServiceName());
                System.out.println("IP Address: " + tmp.getServiceIP());
                System.out.println("Port: " + tmp.getServicePort());
            }
        }
        System.out.println("---------------------------------------");
    }

    public boolean removeService(Service service){
        System.out.println("Attempting to remove service '" + service.getServiceName() + "'...");
        try{
            if (registeredServices.containsKey(service.getServiceName())){
                registeredServices.remove(service.getServiceName());
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }

        System.out.println("Service '" + service.getServiceName() + "' was removed successfully.");
        listRegisteredServices();
        
        return true;
    }
    
    public Service lookupService(Service service){
     System.out.println("Attempting to lookup service '" + service.getServiceName() + "'...");
     try{
          if (registeredServices.containsKey(service.getServiceName())){
             System.out.println("Service '" + service.getServiceName() + "' found...");
             return (Service)registeredServices.get(service.getServiceName());
           }else{
             System.out.println("Service Not Found!");
             return null;
          }
        }catch(Exception e){
            System.out.println("Error looking up service!");
            return null;
        }
        
    }

    public class RequestHandler implements Runnable, Serializable{

        private Socket socket;

        public RequestHandler(Socket socket){
            this.socket = socket;
            Thread t = new Thread(this);
            t.start();
        }

        public void run() {

            try
            {
                /*
                 * Get input form client request
                 * Client requested action should be int he following format
                 * in the received HashMap object:
                 * HashMap<Integer action>,<Service object>
                 */
                
                /*
                 * This object will be returned to the Name Service client.
                 * It is the client's responsibility for converting the object
                 * to the proper type as expected by the method called.
                 */
                Object result = new SerializableObject();

                ObjectInputStream i_str = new ObjectInputStream(socket.getInputStream());

                HashMap<Integer,List> input = (HashMap)i_str.readObject();
                System.out.println("Processing " + input.size() + " requests...");

                if (input.containsKey(NameService.LOOKUP_SERVICE)){
                    System.out.println("Found LOOKUP requests:");
                    List lookupRequests = input.get(NameService.LOOKUP_SERVICE);
                    Iterator it1 = lookupRequests.iterator();

                    while (it1.hasNext()){
                        //System.out.println(((Service)it1.next()).getServiceName());
                        result = lookupService((Service)it1.next());
                    }

                }
                if (input.containsKey(NameService.REGISTER_SERVICE)){
                    System.out.println("Found REGISTER requests:");
                    List registerRequests = input.get(NameService.REGISTER_SERVICE);
                    Iterator it2 = registerRequests.iterator();

                    while (it2.hasNext()){
                        //System.out.println(((Service)it2.next()).getServiceName());
                        result = registerService((Service)it2.next());
                    }
                }
                if (input.containsKey(NameService.REMOVE_SERVICE)){
                    System.out.println("Found REMOVE requests:");
                    List removeRequests = input.get(NameService.REMOVE_SERVICE);
                    Iterator it3 = removeRequests.iterator();

                    while (it3.hasNext()){
                        //System.out.println(((Service)it3.next()).getServiceName());
                        result = removeService((Service)it3.next());
                    }
                }

                /*
                 * Send the result to the client
                 */
                ObjectOutputStream o_str = new ObjectOutputStream(socket.getOutputStream());
                o_str.writeObject(result);

                i_str.close();
                o_str.close();
                socket.close();

                System.out.println("Name Service request finished processing...");
                listRegisteredServices();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getNameServiceHost(){
        String host = "localhost";
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NameService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return host;
    }

}
