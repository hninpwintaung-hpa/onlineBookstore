import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
import java.util.StringTokenizer;

public class NameService implements Runnable {
	Socket socket = null;
	// Service service = new Service();
	static String[][] table = new String[2][3];
	// first row for bookstore and second row for bankserver
	// HashMap<String, Service> hashMap = new HashMap<String, Service>();
	String message, method, serverName, IP, port;// method is either invoke or
													// register

	// HashMap<String, service> hashMap;

	NameService(Socket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) {
		int port = 4444;
		try {
			// making server socket
			ServerSocket serverSocket = new ServerSocket(port);
			InetAddress address = InetAddress.getLocalHost();
			System.out.println("Waiting for connections on IP: "
					+ address.getHostAddress() + " and port:" + port + "...");
			// waiting for the client
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Connected");
				new Thread(new NameService(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			// creates the input and output stream objects
			ObjectInputStream inputStream = new ObjectInputStream(
					socket.getInputStream());
			ObjectOutputStream outputStream = new ObjectOutputStream(
					socket.getOutputStream());
			System.out.println("The connection is from client: "
					+ socket.getInetAddress().getHostName());
			message = (String) inputStream.readObject();
			// message = (String) inputStream.readObject();
			// message format is invoke/register,bankserver/bookstore,IP,port
			StringTokenizer tokenizer = new StringTokenizer(message, ",");
			method = tokenizer.nextToken();
			if (method.equals("invoke")) {
				invoke();
				outputStream.writeObject(message);
			}
			if (method.equals("register")) {
				register();
			}

			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	void invoke() {
		StringTokenizer tokenizer = new StringTokenizer(message, ",");
		method = tokenizer.nextToken();
		serverName = tokenizer.nextToken();
		// IP = tokenizer.nextToken();
		// port = Integer.parseInt(tokenizer.nextToken());
		// if (serverName.equals("bookstore")) {// we found it
		// Service service = new Service();
		// service = hashMap.get(serverName);
		if (table.length != 0) {
			if (serverName.equals("bookstore")) {
				serverName = table[0][0];
				IP = table[0][1];
				port = table[0][2];
			}
			if (serverName.equals("BANK")) {
				serverName = table[1][0];
				IP = table[1][1];
				port = table[1][2];
			}
			// IP = service.IPAddress;
			// port = service.portNumber;
			message = "result" + "," + serverName + "," + IP + "," + port;
			System.out.println("IP and Port invoked by client.");
			System.out.println(message);
		} else {
			message = "noresult" + "," + "noresult" + "," + "noresult" + ","
					+ Integer.toString(0);
		}
		listServices();
	}

	void register() {
		// register
		StringTokenizer tokenizer = new StringTokenizer(message, ",");
		method = tokenizer.nextToken();
		serverName = tokenizer.nextToken();
		// IP = socket.getInetAddress().getHostName();
		IP = tokenizer.nextToken();
		port = tokenizer.nextToken();
		// Service service = new Service();
		if (serverName.equals("bookstore")) {
			table[0][0] = serverName;
			table[0][1] = IP;
			table[0][2] = port;
		}
		if (serverName.equals("BANK")) {
			table[1][0] = serverName;
			table[1][1] = IP;
			table[1][2] = port;
		}
		// service.setService(IP, port);
		// service.print();
		// this will add the info to the hashmap
		// hashMap.put(serverName, service);
		System.out.println("IP and Port registered by client.");
		listServices();

	}

	void listServices() {
		System.out.println("The services registered in NameService are:");
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j <= 2; j++) {
				if (table[i][j] != null) {
					System.out.println(table[i][j]);
				}
			}
			System.out.println();
		}
	}
}