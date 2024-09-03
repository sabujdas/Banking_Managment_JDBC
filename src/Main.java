import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "Sabuj12345*";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection made successfully!!");
            Scanner scanner = new Scanner(System.in);

            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long Account_no;

            while(true) {

                System.out.println("***** HELLO WELCOME TO BAKING SYSTEM *****");
                System.out.println();
                System.out.println("1.Register ");
                System.out.println("2.Login ");
                System.out.println("3.Exit ");
                System.out.println("Enter Your Choice :");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email!= null) {
                            System.out.println();
                            System.out.println("User Logged In !!");
                            //If the User is new
                            if (!accounts.UserExist(email)) {
                                System.out.println();
                                System.out.println("1.Open Bank Account");
                                System.out.println("2. Exit");
                                if (scanner.nextInt() == 1) {
                                    Account_no = accounts.open_account(email);
                                    System.out.println("Account Has been Created Successfully");
                                    System.out.println("Your Account No :" + Account_no);
                                } else {
                                    break;
                                }
                            }
                                Account_no = accounts.get_account_no(email);
                                int choice2 = 0;
                                while (choice2 != 5) {
                                    System.out.println();
                                    System.out.println("1.Debit Money");
                                    System.out.println("2.Credit Money");
                                    System.out.println("3.Transfer Money");
                                    System.out.println("4.Check Balance");
                                    System.out.println("5.LogOut");
                                    choice2 = scanner.nextInt();
                                    switch (choice2) {
                                        case 1:
                                            accountManager.debit_money(Account_no);
                                            break;
                                        case 2:
                                            accountManager.credit_money(Account_no);
                                            break;
                                        case 3:
                                            accountManager.transfer_money(Account_no);
                                            break;
                                        case 4:
                                            accountManager.get_Balance(Account_no);
                                            break;
                                        case 5:
                                            break;
                                        default:
                                            System.out.println("Invalid Option!");
                                            break;
                                    }
                                }
                            }
                        else {
                            System.out.println("Invalid Email-id or Password!!");
                        }

                            case 3:
                                int a = 5;
                                System.out.println("THANK YOU FOR USING BANKING SYSTEM");
                                System.out.println("Exiting System");
                                while (a != 0) {
                                    try {
                                        System.out.println(".");
                                        Thread.sleep(450);
                                        a = a - 1;
                                    } catch (InterruptedException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                                System.out.println("Thank You For Your Patience");
                                break;
                    default:
                        System.out.println("Enter a valid Choice!!");
                        break;
                        }
                        break;
                }

            }
        catch(SQLException e){
        e.printStackTrace();
    }
}
}