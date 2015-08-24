import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Client {
	String searchMode = null;
	String searchString = null;
	int inputSelection = 0;
	String nameServiseIP = "129.115.26.222";
	int nameServicePort = 4444;
	String bookStoreIP = "localhost";
	int bookStorePort = 2222;
	// String result=null;
	String message = "0";
	Socket bkSocket = null;
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;

	public static void main(String[] args) {
		Client client = new Client();
		// get the name service IP address
		//--------------------
		client.invokeIP();
		//-------------------
		// end of get the name service IP address
		try {
			// make the socket to the bookStore
			client.bkSocket = new Socket(client.bookStoreIP,
					client.bookStorePort);
			// client.bkSocket = new Socket("localhost", 2222);
			client.outputStream = new ObjectOutputStream(
					client.bkSocket.getOutputStream());
			client.inputStream = new ObjectInputStream(
					client.bkSocket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// while(!client.message.equals("bye")){
		client.inputInitializer();// }
	}

	private void invokeIP() {
		/*System.out
				.println("Give nameservice IP address or press enter to be local host:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			nameServiseIP = reader.readLine();
			// reader.close();
			if (nameServiseIP.equals("")) {
				nameServiseIP = "localhost";
			}*/
		try{
			Socket socket = new Socket(nameServiseIP, nameServicePort);
			ObjectOutputStream outputStream = new ObjectOutputStream(
					socket.getOutputStream());
			message = "invoke" + "," + "bookstore";
			outputStream.writeObject(message);
			ObjectInputStream inputStream = new ObjectInputStream(
					socket.getInputStream());
			message = (String) inputStream.readObject();
			StringTokenizer tokenizer = new StringTokenizer(message, ",");
			String resultString;
			resultString = tokenizer.nextToken();
			if (resultString.equals("result")) {
				tokenizer.nextToken();
				bookStoreIP = tokenizer.nextToken();
				bookStorePort = Integer.parseInt(tokenizer.nextToken());
				System.out.println("IP and Port invoked from NameService.");
				System.out.println("IP is:" + bookStoreIP + " and Port is:"
						+ bookStorePort + " .");
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

	void inputInitializer() {
		System.out.println("Select the action by the specified number:");
		System.out.println("(1)Show all books");
		System.out.println("(2)Search");
		System.out.println("(3)Cart");
		System.out.println("(4)Exit");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			inputSelection = Integer.parseInt(reader.readLine());
			// reader.close();
			// System.in.close();
			if (inputSelection == 1 || inputSelection == 2
					|| inputSelection == 3 || inputSelection == 4) {
				switch (inputSelection) {
				case 1:
					readAllBooks();
					break;
				case 2:
					searchBooksInit();
					break;
				case 3:
					cartInit();
					break;
				case 4:
					exit();
					break;

				}
			} else {
				System.out
						.println("Plese select from the specified number for selections.");
				inputInitializer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void searchBooksInit() {
		// get the search mode title or author or publisher with the string to
		// send
		// to the bookserver to search
		System.out.println("Select the search mode by the specified number:");
		System.out.println("(1)Title");
		System.out.println("(2)Author");
		System.out.println("(3)Publisher");
		System.out.println("(4)Main menu");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		try {
			inputSelection = Integer.parseInt(reader.readLine());
			// reader.close();
			// System.in.close();
			if (inputSelection == 1 || inputSelection == 2
					|| inputSelection == 3 || inputSelection == 4) {
				switch (inputSelection) {
				case 1:
					searchBooks(inputSelection);
					break;
				case 2:
					searchBooks(inputSelection);
					break;
				case 3:
					searchBooks(inputSelection);
					break;
				case 4:
					inputInitializer();
					break;
				}
			} else {
				System.out
						.println("Plese select from the specified number for selections.");
				searchBooksInit();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error!");
		}

	}

	void searchBooks(int searchSelection) {
		// send the search by title, author or publisher
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		switch (searchSelection) {
		case 1:
			searchMode = "title";
			System.out.println("Please type the title to search.");
			break;
		case 2:
			searchMode = "author";
			System.out.println("Please type the author to search.");
			break;
		case 3:
			searchMode = "publisher";
			System.out.println("Please type the publisher to search.");
			break;

		}
		try {
			searchString = reader.readLine();
			message = ("2," + searchMode + "," + searchString);
			send(message);
			receive();
			// here should call a method to send it to the server and wait for
			// the result the show the result to the user
			// then should select the index of the book object to add to cart or
			// go to the main menu
			// first check if it has result the do the rest
			if (message.equals("not found")) {
				// reader.close();
				searchBooksInit();
			} else {
				System.out.println("(1)Add to the cart.");
				System.out.println("(2)Main menu.");
				inputSelection = Integer.parseInt(reader.readLine());
				// reader.close();
				if (inputSelection == 1 || inputSelection == 2) {
					switch (inputSelection) {
					case 1:
						addToCart();
						break;
					case 2:
						inputInitializer();
						break;
					}
				} else {
					System.out
							.println("Plese select from the specified number for selections.");
					searchBooks(searchSelection);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error!");
		}

	}

	void cartInit() {
		// this will go to the selections and make the cart options available
		System.out.println("Select the Cart action by the specified number:");
		System.out.println("(1)Show Cart");
		System.out.println("(2)Empty Cart");
		System.out.println("(3)Add to Cart");
		System.out.println("(4)Checkout");
		System.out.println("(5)Main menu");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			inputSelection = Integer.parseInt(reader.readLine());
			// reader.close();
			if (1 <= inputSelection && inputSelection <= 5) {
				switch (inputSelection) {
				case 1:
					showCartItems();
					break;
				case 2:
					emptyCart();// to call Cart init
					break;
				case 3:
					addToCart();
					break;
				case 4:
					checkOut();
					break;
				case 5:
					inputInitializer();
					break;
				}
			} else {
				System.out
						.println("Plese select from the specified number for selections.");
				cartInit();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error!");
		}

	}

	void readAllBooks() {
		// call the method in the bookstore to show all the books
		message = "1,";// show all books
		send(message);
		receive();
		inputInitializer();
	}

	void addToCart() {
		// use remote method to add books to the cart
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out
				.println("Please select the index of the book at the beginning of the line.");
		try {
			String index = reader.readLine();
			System.out.println("Please type the quantity.");
			String quantity = reader.readLine();
			// reader.close();
			message = ("3," + index + "," + quantity);
			send(message);
			receive();
			// System.out.println("The selected book has been added.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
		cartInit();

	}

	void showCartItems() {
		// show the books in the cart by RO
		message = ("6,");
		System.out.println("The books in the Cart are:");
		send(message);
		receive();
		cartInit();
	}

	void emptyCart() {
		// use ROs to empty the cart
		message = ("5,");
		send(message);
		receive();
		// System.out.println("The books in the cart has been deleted.");
		
			cartInit();
	}

	void checkOut() {
		// checkout the books by ROs
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		boolean rcvResult = false;
		try {
			System.out.println("Please type your name.");
			String name = reader.readLine();
			System.out.println("Please type your account number.");
			String routingNumber = reader.readLine();
			System.out.println("(1)Capital One Bank.");
			System.out.println("(2)Ctizens Bank.");
			System.out.println("(3)USAA Federal Savings Bank.");
			System.out.println("Please select your bank by the index.");
			String bankID = reader.readLine();
			message = ("4," + name + "," + routingNumber+ "," + bankID);
			send(message);
			rcvResult = receive();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error!");
		}
		if (rcvResult == true) {
			System.out.println("your purchase completed.");
		} else {
			System.out.println("insufficent balance.");
		}
		cartInit();
	}

	boolean receive() {
		// receive the RO and do unmarshaling
		boolean b = false;
		try {
			message = (String) inputStream.readObject();
			StringTokenizer tokenizer = new StringTokenizer(message, "!");
			switch (Integer.parseInt(tokenizer.nextToken())) {
			case 1:
				message = tokenizer.nextToken();
				System.out.println("The books in book store are:");
				proxy(message);

				break;
			case 2:
				message = tokenizer.nextToken();
				System.out.println("The result of search is:");
				if (message.equals("not found")) {
					System.out
							.println("There is no such a book in book store.");
				} else {
					proxy(message);
				}
				break;
			case 3:
				message = tokenizer.nextToken();
				// System.out.println("The result of search is:");
				if (message.equals("no quantity")) {
					System.out
							.println("There is not enough quantity of the book.");
				} else {
					System.out.println("The book has been added.");
					proxy(message);
				}
				break;
			case 4:
				message = tokenizer.nextToken();
				StringTokenizer tk = new StringTokenizer(message, ",");
				if (tk.nextToken().equals("purchase complete")) {
					// System.out.println("Purchase has been completed.");
					b = true;
				}
				if (tk.nextToken().equals("insufficent balance")) {
					// System.out
					// .println("There is not engough balance to purchase.");
					b = false;
				}
				break;
			case 5:
				message = tokenizer.nextToken();
				if (message.equals("empty cart")) {
					System.out.println("The cart items has been deleted.");
				}

				break;
			case 6:
				message = tokenizer.nextToken();
				if (message.equals("empty cart")) {
					System.out.println("The cart is empty.");
				} else {
					proxy(message);
				}
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return b;
	}

	void exit() {
		// the client application ends in this by send bye to server and close
		// the connection
		try {
			send("bye");
			inputStream.close();
			outputStream.close();
			bkSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void send(String msg) {
		// send requests to the bookstore
		try {
			message = msg;
			outputStream.writeObject(message);
			outputStream.flush();
			// System.out.println("bookStore-> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
			ioException.printStackTrace();
		}
	}

	void proxy(String msg) {
		message = msg;
		StringTokenizer tokenizer = new StringTokenizer(message, "|");
		while (tokenizer.hasMoreTokens()) {
			System.out.println(tokenizer.nextToken());
		}
	}
}
