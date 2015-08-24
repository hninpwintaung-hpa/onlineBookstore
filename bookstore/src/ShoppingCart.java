public class ShoppingCart {

	int bookObjectIndex;
	String title;
	String author;
	String publisher;
	int isbn;
	double bookPrice;
	int bookQuantity;

	void print() {
		System.out.println("(" + bookObjectIndex + ")" + "Title: " + title
				+ ", Author: " + author + ", Publisher: " + publisher
				+ ", ISBN: " + isbn + ", Price: " + bookPrice + ", Quantity: "
				+ bookQuantity);

	}

	String buildMessage() {
		String messageString;
		messageString = bookObjectIndex + "," + title + "," + author + ","
				+ publisher + "," + isbn + "," + bookPrice + "," + bookQuantity;
		return messageString;
	}
}
