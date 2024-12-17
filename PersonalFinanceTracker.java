import java.io.*;
import java.util.*;

class Transaction 
{
    private String type;
    private double amount;
    private String description;
    private String category;
    private Date date;

    public Transaction(String type, double amount, String description, String category, Date date) 
    {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public String getType() 
    {
        return type;
    }

    public double getAmount() 
    {
        return amount;
    }

    public String getDescription() 
    {
        return description;
    }

    public String getCategory() 
    {
        return category;
    }

    public Date getDate() 
    {
        return date;
    }

    @Override
    public String toString() 
    {
        return type + " - Amount: " + amount + ", Description: " + description + ", Category: " + category + ", Date: " + date;
    }
}

class User 
{
    private String username;
    private String password;
    private String name;
    private String email;

    public User(String username, String password, String name, String email) 
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUsername() 
    {
        return username;
    }

    public String getPassword() 
    {
        return password;
    }

    public String getName() 
    {
        return name;
    }

    public String getEmail() 
    {
        return email;
    
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
}

class UserManager 
{
    private List<User> users;

    public UserManager() 
    {
        users = new ArrayList<>();
        loadUsers();
    }

    private void loadUsers() 
    {
        users.add(new User("admin", "admin123", "Admin User", "admin@example.com"));
    }

    public User login(String username, String password) 
    {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) 
            {
                return user;
            }
        }
        return null;
    }

    public void addUser(String username, String password, String name, String email) 
    {
        users.add(new User(username, password, name, email));
    }

    public void updateUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) 
            {
                u.setPassword(user.getPassword());
                u.setEmail(user.getEmail());
                u.setName(user.getName());
                break;
            }
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"))) 
        {
            out.writeObject(users);
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    public void loadUsersFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"))) 
        {
            users = (List<User>) in.readObject();

            System.out.println("Users loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users.");
        }
    }
}

class FinanceManager 
{
    private List<Transaction> transactions;
    private User currentUser;

    public FinanceManager() 
    {
        transactions = new ArrayList<>();
    }

    public void setCurrentUser(User user) 
    {
        this.currentUser = user;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    public void showTransactionsByCategory(String category) {
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getCategory().equalsIgnoreCase(category)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found in this category.");
        }
    }

    public double calculateBalance() {
        double balance = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                balance += transaction.getAmount();
            } else if (transaction.getType().equalsIgnoreCase("Expense")) {
                balance -= transaction.getAmount();
            }
        }
        return balance;
    }

    public void showSummary() {
        double totalIncome = 0;
        double totalExpense = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                totalIncome += transaction.getAmount();
            } else if (transaction.getType().equalsIgnoreCase("Expense")) {
                totalExpense += transaction.getAmount();
            }
        }
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpense);
        System.out.println("Net Balance: " + (totalIncome - totalExpense));
    }

    public void searchTransactionByDescription(String keyword) {
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found with the given description.");
        }
    }

    public void saveTransactionsToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("transactions.dat"))) {
            out.writeObject(transactions);
            System.out.println("Transactions saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving transactions.");
        }
    }

    public void loadTransactionsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("transactions.dat"))) {
            transactions = (List<Transaction>) in.readObject();
            System.out.println("Transactions loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading transactions.");
        }
    }
}

public class PersonalFinanceTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static FinanceManager financeManager = new FinanceManager();
    private static User currentUser;

    public static void main(String[] args) {
        System.out.println("Welcome to the Personal Finance Tracker!");
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            currentUser = userManager.login(username, password);
            if (currentUser != null) {
                System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
                financeManager.setCurrentUser(currentUser);
                showMenu();
                break;
            } else {
                System.out.println("Invalid login. Please try again.");
            }
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Show Transactions");
            System.out.println("4. Show Transactions by Category");
            System.out.println("5. Search Transactions by Description");
            System.out.println("6. Show Balance");
            System.out.println("7. Show Summary");
            System.out.println("8. Save Transactions");
            System.out.println("9. Load Transactions");
            System.out.println("10. Update Profile");
            System.out.println("11. Logout");
            System.out.print("Select an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addExpense();
                    break;
                case 3:
                    financeManager.showTransactions();
                    break;
                case 4:
                    showTransactionsByCategory();
                    break;
                case 5:
                    searchTransactionsByDescription();
                    break;
                case 6:
                    showBalance();
                    break;
                case 7:
                    financeManager.showSummary();
                    break;
                case 8:
                    financeManager.saveTransactionsToFile();
                    break;
                case 9:
                    financeManager.loadTransactionsFromFile();
                    break;
                case 10:
                    updateUserProfile();
                    break;
                case 11:
                    logout();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addIncome() {
        System.out.print("Enter income amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter income description: ");
        String description = scanner.nextLine();
        System.out.print("Enter income category (e.g., Salary, Bonus): ");
        String category = scanner.nextLine();
        Transaction transaction = new Transaction("Income", amount, description, category, new Date());
        financeManager.addTransaction(transaction);
        System.out.println("Income added successfully.");
    }

    private static void addExpense() 
    {
        System.out.print("Enter expense amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();
        System.out.print("Enter expense category (e.g., Groceries, Utilities): ");
        String category = scanner.nextLine();
        Transaction transaction = new Transaction("Expense", amount, description, category, new Date());
        financeManager.addTransaction(transaction);
        System.out.println("Expense added successfully.");
    }

    private static void showTransactionsByCategory() 
    {
        System.out.print("Enter category to search for (e.g., Salary, Groceries): ");
        String category = scanner.nextLine();
        financeManager.showTransactionsByCategory(category);
    }

    private static void searchTransactionsByDescription() 
    {
        System.out.print("Enter description keyword to search for: ");
        String keyword = scanner.nextLine();
        financeManager.searchTransactionByDescription(keyword);
    }

    private static void showBalance() 
    {
        double balance = financeManager.calculateBalance();
        System.out.println("Your current balance is: " + balance);
    }

    private static void updateUserProfile() 
    {
        System.out.println("Update Profile:");
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();

        currentUser.setPassword(password);
        currentUser.setEmail(email);
        currentUser.setName(name);
        userManager.updateUser(currentUser);
        System.out.println("Profile updated successfully.");
    }

    private static void logout() {
        System.out.println("Logging out...");
        currentUser = null;
    }
}
