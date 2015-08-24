#4/21/2011 - Comments by Luis Landivar

The only functional pieces in this package are CentralBankServer.java and NameService.java

How to test it:

1. Run NameService.java - it will start the name service listening on port 5555
2. Run CentralBankServer.java - it will start the bank server listening on port 7777

Note, that if the Name Service is not running, the CentralBankServer will not start since it needs the naming service to register its own services.

Within the CentralBankServer.java, look at the registerServices() method, that is were the calls to the naming service is made to register the services provided by the bank.

Also, note that I created a Service class which is used for the purpose of creating, modifying or looking up services.

Read through the code, I tried to put as many comments as possible.