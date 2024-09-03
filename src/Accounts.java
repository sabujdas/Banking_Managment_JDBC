import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;

    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email){
        if(!UserExist(email)){
            scanner.nextLine();
            System.out.println("Enter Your Name :");
            String name = scanner.nextLine();
            System.out.println("Enter Your Initial Amount : ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter Your Security Pin : ");
            String pin = scanner.nextLine();
            String query = "insert into accounts(Full_name,email_id,Account_no,Balance,Security_pin) Values ( ? , ? ,? ,? ,?)";
            try {
                long Account_no = generate_account_no();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setLong(3,Account_no);
                preparedStatement.setDouble(4,amount);
                preparedStatement.setString(5,pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0){
                    return Account_no;
                }
                else {
                    System.out.println("Account Creation Failed");
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist!!");
    }

    public long get_account_no(String email){
        String query = "select Account_no from accounts where email_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("Account_no");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Doesn't Exist!!");
    }

    public long generate_account_no(){
        String query = "select Account_no from accounts order by Account_no DESC limit 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                long last_account_no = resultSet.getLong("Account_no");
                return last_account_no + 1;
            }else return 1011100110;

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 1011100110;
    }

    public boolean UserExist(String email){
        String query = "select Account_no from accounts where email_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else return false;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
