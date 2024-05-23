public class Account {
    private String username;
    private String password;
    private double balance;

    public Account(String username, String password){
        this.username = username;
        this.password = password;
        this.balance = 0.0;
    }

    public Account(String username, String password, double balance){
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public boolean verifyPassword(String password){
        return this.password.equals(password);
    }

    public void deposit(double amount){
        balance += amount;
    }

    public void withdraw(double amount){
        balance -= amount;
    }

    public double getBalance(){
        return balance;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
