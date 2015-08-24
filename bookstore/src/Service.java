
public class Service {

	//String serverName;
	String IPAddress;
	int portNumber;
	void setService (String ipaddress, int portnumber){
		//serverName = servername;
		IPAddress = ipaddress;
		portNumber = portnumber;
	}
	void print(){
		//System.out.println("The infromation service are:");
		//System.out.println(serverName);
		System.out.println("IP: "+IPAddress);
		System.out.println("Port: "+portNumber);
	}
}
