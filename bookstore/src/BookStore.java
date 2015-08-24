import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class BookStore implements Runnable {

	static Book[] bookArr = null;
	static LinkedList<ShoppingCart> shoppingCartList = null;
	ShoppingCart shoppingCart = null;
	// holds the book objects that has been found in the search
	boolean searchResultArr[] = null;
	static Bank bank = null;
	static int lineConuter;// the number of books
	// index of the book object in the book array that we should get from the
	// client within the request to add a book to the shopping cart
	static int bookObjectIndex;
	String nameServiseIP = null;
	int nameServicePort = 4444;
	String message = null;// the message received or to be send
	int method = 0;// action from client
	String searchMode = null;// title, author, publisher
	String searchString = null;
	// Booksotre information to be registered
	String bookStoreIP = "129.115.26.230";
	int bookStorePort = 2222;
	static String bankServerIP = null;
	static int bankServerPort = 0;
	Socket socket = null;
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;

	BookStore() {
		// empty constructor
	}

	BookStore(Socket socket) {
		// constructor in case of client connection
		this.socket = socket;
	}

	public static void main(String[] args) {

		BookStore bookStore = new BookStore();
		// initializes the bank object
		// should be remove in case merge to the BankServer
		// bookStore.bank = new Bank();
		// bookStore.initBank();
		// calls the method to initialize the book objects
		bookStore.initBookArr();
		// get the bookStore IP address
		// ---------------------
		bookStore.registerIP();
		// ----------------------
		// get the Bank IP address
		// ---------------------
		bookStore.invokeIP();
		// ----------------------
		// start the server socket to accept the calls
		try {
			ServerSocket serverSocket = new ServerSocket(
					bookStore.bookStorePort);
			InetAddress address = InetAddress.getLocalHost();
			System.out.println("Waiting for clients on IP: "
					+ address.getHostAddress() + " and port:"
					+ bookStore.bookStorePort + "...");
			// waiting for client
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Connected");
				new Thread(new BookStore(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * // to be removed bookStore.search("title", "Operating Systems"); //
		 * creates the new shopping cart list for the client
		 * bookStore.addToCart(2, 2); bookStore.addToCart(0, 1);
		 * bookStore.addToCart(1, 3); bookStore.showCartItems(); //
		 * bookStore.emptyCart(); // bookStore.showCartItems();
		 * bookStore.initBank(); // bookStore.checkOut(); // to be removed
		 */
	}

	private void registerIP() {
		// connects to the Nameservice and register the IP and port of the
		// bookstore
		/*
		 * System.out
		 * .println("Give nameservice IP address or press enter to be local host:"
		 * ); BufferedReader reader = new BufferedReader(new InputStreamReader(
		 * System.in)); try { nameServiseIP = reader.readLine(); if
		 * (nameServiseIP.equals("")) { nameServiseIP = "localhost"; }
		 */
		try {
			Socket nameServiceSocket = new Socket(nameServiseIP,
					nameServicePort);

			ObjectOutputStream outputStream = new ObjectOutputStream(
					nameServiceSocket.getOutputStream());
			InetAddress address = InetAddress.getLocalHost();
			message = "register" + "," + "bookstore" + "," + bookStoreIP + ","
					+ Integer.toString(bookStorePort);
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

	private void invokeIP() {
		/*
		 * System.out
		 * .println("Give nameservice IP address or press enter to be local host:"
		 * ); BufferedReader reader = new BufferedReader(new InputStreamReader(
		 * System.in)); try { nameServiseIP = reader.readLine(); //
		 * reader.close(); if (nameServiseIP.equals("")) { nameServiseIP =
		 * "localhost"; }
		 */
		try {
			Socket socket = new Socket(nameServiseIP, nameServicePort);
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
				tokenizer.nextToken();
				bankServerIP = tokenizer.nextToken();
				bankServerPort = Integer.parseInt(tokenizer.nextToken());
				System.out.println("IP and Port invoked from NameService.");
				System.out.println("IP is:" + bankServerIP + " and Port is:"
						+ bankServerPort + " .");
			} else if (resultString.equals("noresult")) {
				System.err.println("The IP and Port can not be found.");
			}
			outputStream.close();
			inputStream.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Wrong IP address!");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		// thread for each client connects to the bookstore
		shoppingCartList = new LinkedList<ShoppingCart>();

		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("The connection is from client: "
					+ socket.getInetAddress().getHostName());
			// message = (String) inputStream.readObject();
			do {
				try {
					message = (String) inputStream.readObject();
					System.out.println("BookClient-> " + message);
					// get the string and make it to integer
					if (message.equals("bye")) {
						send("bye");
					} else {
						dispatcher(message);

						// message = calculator(message);
						// out.writeObject(message);
						// out.flush();
						// System.out.println("Server-> " + message);
					}

				} catch (ClassNotFoundException classnot) {
					classnot.printStackTrace();
					System.err.println("data received is unkown format!");
				}
			} while (!message.equals("bye"));
			// end of while

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initBookArr() {
		// this method read the books_text and create the book
		try {
			// to get the number of lines in the text
			FileInputStream fio = new FileInputStream("books_text");
			DataInputStream in = new DataInputStream(fio);
			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(in));
			FileInputStream fio2 = new FileInputStream("books_text");
			DataInputStream in2 = new DataInputStream(fio2);
			BufferedReader buffer2 = new BufferedReader(new InputStreamReader(
					in2));
			lineConuter = 0;
			String line;
			while ((line = buffer2.readLine()) != null) {
				lineConuter++;
			}
			in2.close();
			// System.out.println(lineConuter);
			int i = 0;
			bookArr = new Book[lineConuter];
			while ((line = buffer.readLine()) != null) {

				// System.out.println(line);
				StringTokenizer st = new StringTokenizer(line, ",");
				bookArr[i] = new Book();
				bookArr[i].index = i;
				bookArr[i].title = st.nextToken();
				// System.out.println(book[i].title);
				bookArr[i].author = st.nextToken();
				// System.out.println(book[i].author);
				bookArr[i].publisher = st.nextToken();
				bookArr[i].isbn = Integer.parseInt(st.nextToken());
				bookArr[i].price = Double.parseDouble(st.nextToken());
				bookArr[i].quantity = Integer.parseInt(st.nextToken());
				i++;
			}
			// bookArr[0].print();
			// bookArr[1].print();
			// bookArr[2].print();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}

	}

	void search(String searchField, String searchString) {
		// this method will search for the book within specified search
		// the searchField is the title or author or publisher and the
		// searchString is the actual value
		int i = 0;
		message = "2!";// code for method type
		/*
		 * searchResultArr = new boolean[lineConuter]; for (i = 0; i <
		 * lineConuter; i++) {// sets all to false each found then // set to
		 * true searchResultArr[i] = false; }
		 */
		// System.out.println(lineConuter);
		if (searchField.equals("title")) {
			System.out.println("The search result is:");
			boolean seenResult = false;
			for (i = 0; i < lineConuter; i++) {
				if (bookArr[i].title.equals(searchString)) {
					message = message + (bookArr[i].buildMessage());
					if (i != (lineConuter - 1)) {
						message = message + ("|");
					}
					seenResult = true;
					// searchResultArr[i] = true;
					bookArr[i].print();
				}
			}
			if (!seenResult) {
				System.out.println("The book has not been found.");
				message = message + ("not found");
			}
		} else if (searchField.equals("author")) {
			System.out.println("The search result is:");
			boolean seenResult = false;
			for (i = 0; i < lineConuter; i++) {
				if (bookArr[i].author.equals(searchString)) {
					message = message + (bookArr[i].buildMessage());
					if (i != (lineConuter - 1)) {
						message = message + ("|");
					}
					seenResult = true;
					// searchResultArr[i] = true;
					bookArr[i].print();
				}
			}
			if (!seenResult) {
				System.out.println("The book has not been found.");
				message = message + ("not found");
			}
		} else if (searchField.equals("publisher")) {
			System.out.println("The search result is:");
			boolean seenResult = false;
			for (i = 0; i < lineConuter; i++) {
				if (bookArr[i].publisher.equals(searchString)) {
					message = message + (bookArr[i].buildMessage());
					if (i != (lineConuter - 1)) {
						message = message + ("|");
					}
					seenResult = true;
					// searchResultArr[i] = true;
					bookArr[i].print();
				}
			}
			if (!seenResult) {
				System.out.println("The book has not been found.");
				message = message + ("not found");
			}
		} else {

			System.out
					.println("Wrong specification, you should search with title, author or publisher.");
		}
		// return searchResultArr;
		send(message);
	}

	void addToCart(int bookObjIndx, int cartBookQuantity) {
		// based in the book index and quantity it will add book to cart
		bookObjectIndex = bookObjIndx;
		message = "3!";
		try {
			FileInputStream fio = new FileInputStream("books_text");
			DataInputStream in = new DataInputStream(fio);
			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(in));
			String line;
			int ISBN = 0;
			while ((line = buffer.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				st.nextToken();
				st.nextToken();
				st.nextToken();
				ISBN = Integer.parseInt(st.nextToken());
				if (ISBN == bookArr[bookObjIndx].isbn) {// we found the book to
														// modify
					// if we have enough quantity
					if (bookArr[bookObjIndx].quantity >= cartBookQuantity) {
						// update the new quantity for the bookArr object
						bookArr[bookObjIndx].quantity = ((bookArr[bookObjIndx].quantity) - (cartBookQuantity));
						shoppingCart = new ShoppingCart();
						copyBookToCart(cartBookQuantity);
						shoppingCartList.addLast(shoppingCart);
						in.close();
						updateTextFile();
						Iterator<ShoppingCart> iterator = shoppingCartList
								.iterator();
						while (iterator.hasNext()) {
							message = message
									+ (iterator.next().buildMessage());
							if (iterator.hasNext()) {
								message = message + ("|");
							}
						}
						break;
					} else {
						System.out.println("there is not enough quantity.");
						message = message + ("no quantity");
					}

				}
			}
			send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	void updateTextFile() {
		// whenever there is a change in the book object this will update the
		// file for concurrency
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					"books_text"));
			int i;
			for (i = 0; i < lineConuter; i++) {
				bufferedWriter.write(bookArr[i].title + "," + bookArr[i].author
						+ "," + bookArr[i].publisher + "," + bookArr[i].isbn
						+ "," + bookArr[i].price + "," + bookArr[i].quantity);
				if (i < (lineConuter - 1)) {
					bufferedWriter.newLine();
				} else {
					bufferedWriter.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	void showCartItems() {
		// shows the current items in shopping cart
		message = "6!";
		if (shoppingCartList.isEmpty()) {
			System.out.println("The shopping cart is empty.");
			message = message + ("empty cart");
		}
		Iterator<ShoppingCart> iterator = shoppingCartList.iterator();
		while (iterator.hasNext()) {
			message = message + (iterator.next().buildMessage());
			if (iterator.hasNext()) {
				message = message + ("|");
			}
		}
		send(message);
	}

	void showAll() {
		// this method gives the bookstore books
		message = "1!";
		for (int i = 0; i < lineConuter; i++) {
			message = (message + (bookArr[i].buildMessage()));
			if (i != (lineConuter - 1)) {
				message = (message + "|");
			}
		}
		send(message);

	}

	String chargeAccount(String name, String routingNumber, int bankID,
			double totalPrice) {

		String message_in = ""; // this is the variable that holds the return
								// from the server
		Object message_out = new String();

		// Generate string to send to the central bank server

		message_out = "withdrawFunds:" + bankID + ":" + routingNumber + ":"
				+ name + ":true:0.0:" + totalPrice;
		// withdrawFunds:bankId:accountNumber:customerName:true:0.0:totalPrice

		try {

			/*
			 * Connect to the server
			 */

			// InetAddress host = InetAddress.getLocalHost();
			Socket socket = new Socket(bankServerIP, bankServerPort);

			/*
			 * Send the input to the server. This contains the values randomly
			 * generated by the generateInput() method.
			 */
			ObjectOutputStream o_str = new ObjectOutputStream(
					socket.getOutputStream());
			o_str.writeObject(message_out);

			/*
			 * Get the result from the server and print it out on the console
			 */
			ObjectInputStream i_str = new ObjectInputStream(
					socket.getInputStream());
			// Object myObject = i_str.readObject();
			// System.out.println("Setting message_in in BankComm!");
			// message_in.setObject(i_str.readObject());
			message_in = i_str.readObject().toString();
			// System.out.println("This is what the server responded: " +
			// message_in.getObject().toString());
			// System.out.println("This is what the server responded: " +
			// message_in);
			// System.out.println("Received from server: " +
			// ((CustomerAccount)message_in.getObject()).toString());
			// System.out.println("Result: " + myObject.toString());

			/*
			 * Close input and output streams
			 */
			i_str.close();
			o_str.close();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return message_in;

	}

	void checkOut(String name, String routingNumber, int bankID) {
		// send the name and routing number and get the balance
		String bankResponse = "";
		//
		message = "4!";
		ListIterator<ShoppingCart> iterator = shoppingCartList.listIterator();
		double totalPrice = 0;
		while (iterator.hasNext()) {
			totalPrice = iterator.next().bookPrice + totalPrice;
		}

		bankResponse = chargeAccount(name, routingNumber, bankID, totalPrice);

		if (bankResponse.toLowerCase().contains("successful")) {
			// when the charge is gone through
			System.out.println("The total of " + totalPrice
					+ " dollars subtracted from your account.");
			// bank.currentBalance = bank.currentBalance - totalPrice;
			shoppingCartList.clear();
			message = message + ("purchase complete," + totalPrice);

		} else {
			System.out.println("Insufficent balance for purchase.");
			message = message + ("insufficent balance," + totalPrice);
		}
		/*
		 * get the bank IP from nameService, then connect to bank get the info
		 * for name and routing number
		 */

		// withdrawFunds:bankId:accountNumber:customerName:true:0.0:totalPrice
		/*
		 * if (bank.currentBalance >= totalPrice) {
		 * System.out.println("The total of " + totalPrice +
		 * " dollars subtracted from your account."); bank.currentBalance =
		 * bank.currentBalance - totalPrice; shoppingCartList.clear(); message =
		 * message + ("purchase complete," + totalPrice); } else {
		 * System.out.println("Insufficent balance for purchase."); message =
		 * message + ("insufficent balance," + totalPrice); }
		 */
		send(message);
	}

	void emptyCart() {
		// empty the cart items
		message = "5!";
		int bookIndx;
		ListIterator<ShoppingCart> iterator = shoppingCartList.listIterator();
		while (iterator.hasNext()) {
			bookIndx = iterator.next().bookObjectIndex;
			iterator.previous();
			bookArr[bookIndx].quantity = bookArr[bookIndx].quantity
					+ iterator.next().bookQuantity;
		}
		updateTextFile();
		shoppingCartList.clear();
		message = message + ("empty cart");
		send(message);
	}

	void copyBookToCart(int cartBookQuantity) {
		// it copies the selected book information to the shopping cart object
		// that
		// is going to be added to the shopping cart list
		shoppingCart.bookObjectIndex = bookArr[bookObjectIndex].index;
		shoppingCart.title = bookArr[bookObjectIndex].title;
		shoppingCart.author = bookArr[bookObjectIndex].author;
		shoppingCart.publisher = bookArr[bookObjectIndex].publisher;
		shoppingCart.isbn = bookArr[bookObjectIndex].isbn;
		shoppingCart.bookPrice = (bookArr[bookObjectIndex].price)
				* cartBookQuantity;
		shoppingCart.bookQuantity = cartBookQuantity;
		shoppingCart.print();

	}

	void initBank() {
		// this is temp method to test the checkout method
		bank.accountNumber = 0231043;
		bank.customerName = "smith";
		bank.currentBalance = 5422.23;
	}

	void dispatcher(String msg) {
		// select right action for received messages
		message = msg;
		StringTokenizer tokenizer = new StringTokenizer(message, ",");
		method = Integer.parseInt(tokenizer.nextToken());
		switch (method) {
		case 1:// showAll
			showAll();
			break;
		case 2:// search
				// String searchfield = tokenizer.nextToken();
				// String searchstring = tokenizer.nextToken();
			search(tokenizer.nextToken(), tokenizer.nextToken());
			break;
		case 3:// addtocart
				// int index = Integer.parseInt(tokenizer.nextToken());
				// int quantity = Integer.parseInt(tokenizer.nextToken());
			addToCart(Integer.parseInt(tokenizer.nextToken()),
					Integer.parseInt(tokenizer.nextToken()));
			break;
		case 4:// checkout
			checkOut(tokenizer.nextToken(),
					tokenizer.nextToken(),
					Integer.parseInt(tokenizer.nextToken()));
			break;
		case 5:// emptycart
			emptyCart();
			break;
		case 6:
			showCartItems();
			break;
		}
	}

	void send(String msg) {
		message = msg;
		try {
			outputStream.writeObject(message);
			outputStream.flush();
			if (msg.equals("bye")) {
				inputStream.close();
				outputStream.close();
				socket.close();
			}
			System.out.println("bookStore-> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
			ioException.printStackTrace();
		}
	}

}
