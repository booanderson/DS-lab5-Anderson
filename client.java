import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//pricess interface
interface Process extends Remote {
    void synchronize() throws RemoteException;
}

//process implimentation using process interface
class ProcessImpl extends UnicastRemoteObject implements Process {
    private long localClock;
    private TimeServer timeServer;

    //setter method for each process
    protected ProcessImpl(TimeServer timeServer) throws RemoteException {
        super();
	//initializing processes time server instance 
        this.timeServer = timeServer;
	//initialize the local clock with the current systems time
        this.localClock = System.currentTimeMillis(); 
    }

    //function to synchronize the clocks
    public void synchronize() throws RemoteException {
        //getting the current time from the the remotte server
	long serverTime = timeServer.getTime();
	//calculating the difference between the servers time and the local time
        long offset = serverTime - localClock;

        //adjusting the local clocks time
        localClock += offset;
        System.out.println("Clocks synchonized with offset: " + offset + " ms");
        System.out.println("Current local clock time: " + localClock);
    }

    //main
    public static void main(String[] args) {
        try {
            //getting the time server from RMI
            TimeServer timeServer = (TimeServer) Naming.lookup("rmi://localhost/TimeServer");
            
            //creating the client process
            ProcessImpl process = new ProcessImpl(timeServer);
            
            //registering the process with the server
            timeServer.register("Process1");

            //calling function to synchronize the clocks
            process.synchronize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

