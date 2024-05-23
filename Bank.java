import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote{
    boolean createAccount(String username, String password) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;
    boolean deposit(String username, double amount) throws RemoteException;
    boolean withdraw(String username, double amount) throws RemoteException;
    double getBalance(String username) throws RemoteException;
    boolean send(String fromUser, String toUser, double amount) throws RemoteException;
}
