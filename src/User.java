import java.sql.*;
import java.util.Scanner;

public class User {
    private Connection connection;

    private Scanner scanner;

    public User(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){

            scanner.nextLine();
            System.out.println("Enter Your Name : ");
            String name = scanner.nextLine();
            System.out.println("Enter Your Email-id : ");
            String email = scanner.nextLine();
            System.out.println("Password : ");
            String password = scanner.nextLine();
            if (UserExist(email)) {
                System.out.println("Sorry,Email-id Already Exist");
                return;
            }
            String query = "insert into user(Full_name,email_id,password) values ( ?, ? , ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("User Has been registered");
            }else {
                System.out.println("User Not been registered");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.println("Enter Your Email : ");
        String email = scanner.nextLine();
        System.out.println("Enter Your Password : ");
        String password = scanner.nextLine();
        String query  = "select * from user where email_id = ? and password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }
            else return null;

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean UserExist(String email){
        String query = "select * from user where email_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

}



