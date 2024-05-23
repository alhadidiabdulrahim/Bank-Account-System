import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class BankClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Bank bank = (Bank) registry.lookup("BankService");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Login");
                System.out.println("2. Create New Account");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        handleLogin(bank, scanner);
                        break;
                    case "2":
                        handleCreateAccount(bank, scanner);
                        break;
                    case "3":
                        System.out.println("Exiting the system. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleLogin(Bank bank, Scanner scanner) throws Exception {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        if (bank.login(username, password)) {
            System.out.println("Login successful.");
            userOperations(bank, scanner, username);
        } else {
            System.out.println("Login failed. Please check your credentials and try again.");
        }
    }

    private static void userOperations(Bank bank, Scanner scanner, String username) {
        boolean logout = false;
        while (!logout) {
            System.out.println("Operations Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Send Money");
            System.out.println("5. Logout");
            System.out.print("Select an operation: ");
            String option = scanner.nextLine().trim();

            try {
                switch (option) {
                    case "1":
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = Double.parseDouble(scanner.nextLine().trim());
                        if (bank.deposit(username, depositAmount)) {
                            System.out.println("Deposited successfully.");
                        } else {
                            System.out.println("Deposit failed.");
                        }
                        break;
                    case "2":
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = Double.parseDouble(scanner.nextLine().trim());
                        if (bank.withdraw(username, withdrawAmount)) {
                            System.out.println("Withdrawn successfully.");
                        } else {
                            System.out.println("Insufficient funds or withdrawal failed.");
                        }
                        break;
                    case "3":
                        double balance = bank.getBalance(username);
                        System.out.println("Your current balance is: " + balance);
                        break;
                    case "4":
                        System.out.print("Enter recipient's username: ");
                        String recipient = scanner.nextLine().trim();
                        System.out.print("Enter amount to send: ");
                        double sendAmount = Double.parseDouble(scanner.nextLine().trim());
                        if (bank.send(username, recipient, sendAmount)) {
                            System.out.println("Funds sent successfully.");
                        } else {
                            System.out.println("Failed to send funds. Please check the amount and recipient details.");
                        }
                        break;
                    case "5":
                        logout = true;
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error during operation: " + e.getMessage());
            }
        }
    }

    private static void handleCreateAccount(Bank bank, Scanner scanner) throws Exception {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }

        if (bank.createAccount(username, password)) {
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Failed to create account. It's possible the username is already taken.");
        }
    }
}
