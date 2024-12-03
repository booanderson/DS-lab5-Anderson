/*Usage: 
 *
 * 1. compile with javac server.java client.java
 *
 * 2. run server with java server
 *
 * 3. run client with java client
 */


import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

//interface for the timeserver
interface TimeServer extends Remote {
    long getTime() throws RemoteException;
    void register(String processName) throws RemoteException;
}

//timeserver class to impliment the timeserver interface
class TimeServerImpl extends UnicastRemoteObject implements TimeServer {
    private final List<String> registeredProcesses;

    //constructor for the timeserver implimentation
    protected TimeServerImpl() throws RemoteException {
	//making sure object is exported to the RMI 
        super();
	//initializing the list storing the registered processes (clients)
        registeredProcesses = new ArrayList<>();
    }

    //function to get the current time from the server systems clock in milliseconds
    public long getTime() throws RemoteException {
        return System.currentTimeMillis();
    }

    //method to register the clients and print message when connected
    public void register(String processName) throws RemoteException {
        registeredProcesses.add(processName);
        System.out.println("Process registered: " + processName);
    }
}

//main server class
public class server {
    public static void main(String[] args) {
        try {
            //starting the RMI from code instead of terminal on port 1099
            LocateRegistry.createRegistry(1099);
            
            //creating an instance of the time server
            TimeServer timeServer = new TimeServerImpl();
            
            //binding the timeserver object to the RMI registry
            Naming.rebind("TimeServer", timeServer);
            
            System.out.println("Time Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

