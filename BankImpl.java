import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class BankImpl extends UnicastRemoteObject implements Bank{
    private static final String ACCOUNT_FILE = "accounts.txt";
    private Map<String, Account> accounts;

    public BankImpl() throws RemoteException{
        super();
        accounts = new HashMap<>();
        loadAccountsFromFile();
    }

    @Override
    public synchronized boolean createAccount(String username, String password) throws RemoteException{
        if (accounts.containsKey(username)){
            return false;
        }
        accounts.put(username, new Account(username, password));
        saveAccountsToFile();
        return true;
    }

    @Override
    public synchronized boolean login(String username, String password) throws RemoteException{
        if (!accounts.containsKey(username)){
            return false;
        }
        Account account = accounts.get(username);
        return account.verifyPassword(password);
    }

    @Override
    public synchronized boolean deposit(String username, double amount) throws RemoteException{
        if (!accounts.containsKey(username)){
            return false;
        }
        accounts.get(username).deposit(amount);
        saveAccountsToFile();
        return true;
    }

    @Override
    public synchronized boolean withdraw(String username, double amount) throws RemoteException{
        if (!accounts.containsKey(username)){
            return false;
        }
        Account account = accounts.get(username);
        if (account.getBalance() < amount){
            return false;
        }
        account.withdraw(amount);
        saveAccountsToFile();
        return true;
    }

    @Override
    public synchronized double getBalance(String username) throws RemoteException{
        if (!accounts.containsKey(username)){
            throw new RemoteException("Account not found");
        }
        return accounts.get(username).getBalance();
    }

    @Override
    public synchronized boolean send(String fromUser, String toUser, double amount) throws RemoteException{
        if (!accounts.containsKey(fromUser)){
            throw new RemoteException("Sender account does not exist");
        }
        if (!accounts.containsKey(toUser)){
            throw new RemoteException("Recipient account does not exist");
        }

        Account sender = accounts.get(fromUser);
        Account recipient = accounts.get(toUser);

        if (sender.getBalance() < amount){
            return false;
        }

        sender.withdraw(amount);
        recipient.deposit(amount);

        saveAccountsToFile();
        return true;
    }

    private void loadAccountsFromFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if (parts.length == 3){
                    String username = parts[0];
                    String password = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    accounts.put(username, new Account(username, password, balance));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveAccountsToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))){
            for (Account account : accounts.values()){
                writer.write(account.getUsername() + "," + account.getPassword() + "," + account.getBalance());
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
