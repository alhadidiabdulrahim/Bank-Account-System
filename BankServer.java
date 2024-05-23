import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BankServer{
    public static void main(String[] args) {
        try{
            BankImpl bank = new BankImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BankService", bank);
            System.out.println("Bank server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
