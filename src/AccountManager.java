import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;

    private Scanner scanner;

    public AccountManager(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debit_money(long Account_no)throws SQLException {
        scanner.nextLine();
        System.out.println("Enter the Amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the Security pin :");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (Account_no != 0) {
                String query = "Select * from accounts where Account_no = ? and Security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, Account_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_Balance = resultSet.getDouble("Balance");
                    if (amount <= current_Balance) {
                        String debit_balance = "update accounts set Balance = Balance - ? where Account_no = ? ";
                        PreparedStatement debited_Balance = connection.prepareStatement(debit_balance);
                        debited_Balance.setDouble(1, amount);
                        debited_Balance.setLong(2, Account_no);
                        int rowsAffected = debited_Balance.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Amount : " + amount + " Has been Debited from your Account.");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Amount Has not been debited");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("insufficient Balance !!");
                    }

                } else {
                    System.out.println("Entered invalid Pin !!");
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }//problem

    public void credit_money(long Account_no)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter the Amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the Security pin: ");
        String pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (Account_no != 0) {
                String query = "Select * from accounts where Account_no = ? and Security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, Account_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String credit_query = "update accounts SET Balance = Balance + ? where Account_no = ?";
                    PreparedStatement credit_amount = connection.prepareStatement(credit_query);
                    credit_amount.setDouble(1,amount);
                    credit_amount.setLong(2,Account_no);
                    int rowsAffected = credit_amount.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Amount " + amount + " Has been credited");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else {
                        System.out.println("Amount Has not been Transferred");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid Security pin !!");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);

    }

    public void transfer_money(long Sender_Account_no)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Receiver Account No:");
        long Receiver_account = scanner.nextLong();
        System.out.println("Enter Amount :");
        double amount  = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the Security Pin :");
        String pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (Sender_Account_no != 0 && Receiver_account != 0) {
                String query = "Select * from accounts where Account_no = ? and Security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,Sender_Account_no);
                preparedStatement.setString(2,pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("Balance");
                    if(amount<=current_balance){
                        String Credited_query = "update accounts set Balance = Balance + ? where Account_no = ?";
                        String Debited_query = "update accounts set Balance = Balance - ? where Account_no = ?";
                        PreparedStatement Credited_Statement = connection.prepareStatement(Credited_query);
                        PreparedStatement Debited_Statement = connection.prepareStatement(Debited_query);
                        Credited_Statement.setDouble(1,amount);
                        Credited_Statement.setLong(2,Sender_Account_no);
                        Debited_Statement.setDouble(1,amount);
                        Debited_Statement.setLong(2,Sender_Account_no);
                        int rows_Credit = Credited_Statement.executeUpdate();
                        int rows_Debit = Debited_Statement.executeUpdate();
                        if(rows_Debit > 0 && rows_Credit > 0){
                            System.out.println("Amount Has been Transferred");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else {
                            System.out.println("Amount Has not been Transferred");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficient Balance !!");
                    }
                }
                else{
                    System.out.println("Invalid Pin !!");
                }
            }else {
                System.out.println("Invalid account no !!");

            }
        }
        catch (SQLException e){
            throw new RuntimeException("This Account Not Exist");
        }
        connection.setAutoCommit(true);

    }

    public void get_Balance(long Account_no)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter the Security Pin :");
        String pin = scanner.nextLine();
        try {
            if (Account_no != 0) {
                String query = "Select * from accounts where Account_no = ? and Security_pin = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, Account_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    double Balance = resultSet.getDouble("Balance");
                    System.out.println("Balance :" +Balance );

                }
                else {
                    System.out.println("Invalid Pin!!");
                }
            }
            else {
                System.out.println("Invalid Account No!!");
            }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }

    }
}

