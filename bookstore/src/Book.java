public class Book {
	int index;
	String title;
	String author;
	String publisher;
	int isbn;
	double price;
	int quantity;

	void book(int new_index,String new_title, String new_author, String new_publisher,
			int new_isbn, double new_price, int new_quantity) {
		index = new_index;
		title = new_title;
		publisher = new_author;
		author = new_author;
		isbn = new_isbn;
		price = new_price;
		quantity = new_quantity;
	}

	void print() {
		System.out.println("Index:" + index + "Title:" + title + ", Author:"
				+ author + ", Publisher:" + publisher + ", ISBN:" + isbn
				+ ", Price:" + price + ", Quantity:" + quantity);

	}

	void decreaseQuantity() {
		quantity--;
	}

	void increaseQuantity() {
		quantity++;
	}

	String buildMessage() {
		String message;
		message = index + "," + title + "," + author + "," + publisher + ","
				+ isbn + "," + price + "," + quantity;
		return message;
	}
}
