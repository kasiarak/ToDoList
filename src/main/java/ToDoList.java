import java.sql.*;
import java.util.Scanner;

public class ToDoList {
    static String url = "jdbc:mysql://localhost:3306/ToDoList";
    static String username;
    static String password;

    static boolean Login(){
        boolean userIsLoggedin = false;
        while(!userIsLoggedin) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter your login.");
                username = scanner.next();
                System.out.println("Enter your password.");
                password = scanner.next();
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url,username,password);
                connection.close();
                userIsLoggedin = true;
            } catch (Exception e) {
                System.out.println("The login or password is incorrect. Please try entering again.");
            }
        }
        return true;
    }
    static void Registration(){
        boolean userIsRegistered = false;
        while(!userIsRegistered) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter your login.");
                username = scanner.next();
                System.out.println("Enter your password.");
                password = scanner.next();
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, "root", "");

                PreparedStatement preparedStatement1 = connection.prepareStatement("CREATE USER ?@'%' IDENTIFIED BY ?");
                preparedStatement1.setString(1, username);
                preparedStatement1.setString(2, password);
                preparedStatement1.executeUpdate();

                PreparedStatement preparedStatement2 = connection.prepareStatement("GRANT SELECT, INSERT, UPDATE, DELETE ON todolist.* TO ?@'%'");
                preparedStatement2.setString(1, username);
                preparedStatement2.executeUpdate();

                PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?,?);");
                preparedStatement3.setString(1,username);
                preparedStatement3.setString(2,password);
                preparedStatement3.executeUpdate();

                PreparedStatement preparedStatement4 = connection.prepareStatement("FLUSH PRIVILEGES");
                preparedStatement4.executeUpdate();
                connection.close();
                userIsRegistered = true;
            }catch (Exception e){
                System.out.println("Something went wrong. It is possible that the login you entered already exists or does not meet the requirements. " +
                        "Check that your login is a maximum of 16 characters and does not contain any special characters except \"_\".");
            }
        }
    }
    static void addTask(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a title for your new task (max 50 characters).");
            String title = scanner.nextLine();
            System.out.println("What is the status of your task? Enter one of these: \"Not done\", \"In progress\", \"Done\".");
            String status = scanner.nextLine();
            System.out.println("What is the deadline?");
            String deadline = scanner.nextLine();
            System.out.println("On a scale of 0 to 10, how important is this task?");
            int howImportant = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter a description for your new task.");
            String description = scanner.nextLine();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            if(status.equalsIgnoreCase("In progress") || status.equalsIgnoreCase("Not done")){
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TASKS (USERNAME, TITLE, STATUS, DEADLINE, HOWIMPORTANT, DESCRIPTION) VALUES (?,?,?,?,?,?)");
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,title);
                preparedStatement.setString(3,status);
                preparedStatement.setString(4,deadline);
                preparedStatement.setInt(5,howImportant);
                preparedStatement.setString(6,description);
                preparedStatement.executeUpdate();
            }else if(status.equalsIgnoreCase("Done")){
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DONETASKS (USERNAME, TITLE, STATUS, DEADLINE, HOWIMPORTANT, DESCRIPTION) VALUES (?, ?,'Done',?,?,?)");
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,title);
                preparedStatement.setString(3,deadline);
                preparedStatement.setInt(4,howImportant);
                preparedStatement.setString(5,description);
                preparedStatement.executeUpdate();
            }
            PreparedStatement preparedStatement1 = connection.prepareStatement("SET @counter1 = 0");
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE TASKS SET ID = @counter1:=@counter1+1 WHERE USERNAME = ? ORDER BY ID");
            preparedStatement2.setString(1,username);
            preparedStatement2.executeUpdate();

            PreparedStatement preparedStatement3 = connection.prepareStatement("SET @counter2 = 0");
            preparedStatement3.executeUpdate();

            PreparedStatement preparedStatement4 = connection.prepareStatement("UPDATE DONETASKS SET ID = @counter2:=@counter2+1 WHERE USERNAME = ? ORDER BY ID");
            preparedStatement4.setString(1,username);
            preparedStatement4.executeUpdate();

            connection.close();
            System.out.println("The task has been added.");
        }catch (Exception e){
            System.out.println("Something went wrong. Check whether you have followed the recommendations correctly.");
        }
    }
    static void showTasks(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, TITLE FROM TASKS WHERE USERNAME=?");
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("List of tasks:");
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1)+". " + resultSet.getString(2));
            }
            connection.close();
        }catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }
    static void showDoneTasks(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, TITLE FROM DONETASKS WHERE USERNAME=?");
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("List of completed tasks:");
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1)+". " + resultSet.getString(2));
            }
            connection.close();
        }catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }
    static void showTask(int id){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT TITLE, STATUS, DEADLINE, HOWIMPORTANT, DESCRIPTION FROM TASKS WHERE USERNAME=? AND ID = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Title: " + resultSet.getString(1));
                System.out.println("Status: " + resultSet.getString(2));
                System.out.println("Deadline: " + resultSet.getString(3));
                System.out.println("Importance scale: " + resultSet.getInt(4) + "/10");
                System.out.println("Description: " + resultSet.getString(5));
                System.out.println("If you want to go back to the list, type \"back\". If you want to delete this task, type \"delete\". If you want to make changes, type \"update\".");
            } else {
                System.out.println("There is no task with the given number. Type \"back\" to return to the list.");
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Something went wrong. Please try entering again.");
        }
    }
    static void showDoneTask(int id){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT TITLE, STATUS, DEADLINE, HOWIMPORTANT, DESCRIPTION FROM DONETASKS WHERE USERNAME=? AND ID = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Title: " + resultSet.getString(1));
                System.out.println("Status: " + resultSet.getString(2));
                System.out.println("Deadline: " + resultSet.getString(3));
                System.out.println("Importance scale: " + resultSet.getInt(4) + "/10");
                System.out.println("Description: " + resultSet.getString(5));
                System.out.println("If you want to go back to the list, type \"back\". If you want to delete this task, type \"delete\". If you want to make changes, type \"update\".");
            } else {
                System.out.println("There is no task with the given number. Type \"back\" to return to the list.");
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Something went wrong. Please try entering again.");
        }
    }
    static void showImportantTasks(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, TITLE, DEADLINE, HOWIMPORTANT FROM TASKS WHERE USERNAME=? AND HOWIMPORTANT>=7 ORDER BY HOWIMPORTANT DESC");
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("List of the most important tasks:");
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1)+". " + resultSet.getString(2) + "   Deadline: " + resultSet.getString(3) + "   Importance scale: " + resultSet.getInt(4) + "/10");
            }
            connection.close();
        }catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }
    static void deleteTask(int id){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM TASKS WHERE ID = ? AND USERNAME = ?");
            preparedStatement1.setInt(1,id);
            preparedStatement1.setString(2,username);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2= connection.prepareStatement("SET @counter = 0");
            preparedStatement2.executeUpdate();

            PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE TASKS SET ID = @counter:=@counter+1 WHERE USERNAME = ? ORDER BY ID");
            preparedStatement3.setString(1,username);
            preparedStatement3.executeUpdate();

            connection.close();
            System.out.println("The task has been deleted.");
        }catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }
    static void deleteDoneTask(int id){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM DONETASKS WHERE ID = ? AND USERNAME = ?");
            preparedStatement1.setInt(1,id);
            preparedStatement1.setString(2,username);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2= connection.prepareStatement("SET @counter = 0");
            preparedStatement2.executeUpdate();

            PreparedStatement preparedStatement3 = connection.prepareStatement(" UPDATE DONETASKS SET ID = @counter:=@counter+1 WHERE USERNAME = ? ORDER BY ID");
            preparedStatement3.setString(1,username);
            preparedStatement3.executeUpdate();

            connection.close();
            System.out.println("The task has been deleted.");
        }catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }
    static void updateTask(int id){
        System.out.println("Enter the name of the category you want to make changes to. \n• Title\n• Status\n• Deadline\n• HowImportant\n• Description");
        Scanner scanner = new Scanner(System.in);
        String category = scanner.nextLine();
        System.out.println("Enter new content for the " + category +".");
        String changes = scanner.nextLine();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            if(category.equalsIgnoreCase("Status") && changes.equalsIgnoreCase("Done")){
                PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT TITLE, DEADLINE, HOWIMPORTANT, DESCRIPTION FROM TASKS WHERE USERNAME = ? AND ID = ?");
                preparedStatement1.setString(1, username);
                preparedStatement1.setInt(2, id);
                ResultSet resultSet = preparedStatement1.executeQuery();
                if(resultSet.next()) {
                    String title = resultSet.getString(1);
                    String status = "Done";
                    String deadline = resultSet.getString(2);
                    int howImportant = resultSet.getInt(3);
                    String description = resultSet.getString(4);

                    PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO DONETASKS (USERNAME, TITLE, STATUS, DEADLINE, HOWIMPORTANT, DESCRIPTION) VALUES (?, ?,'Done',?,?,?)");
                    preparedStatement2.setString(1,username);
                    preparedStatement2.setString(2,title);
                    preparedStatement2.setString(3,deadline);
                    preparedStatement2.setInt(4,howImportant);
                    preparedStatement2.setString(5,description);
                    preparedStatement2.executeUpdate();
                }

                PreparedStatement preparedStatement3= connection.prepareStatement("SET @counter1 = 0");
                preparedStatement3.executeUpdate();

                PreparedStatement preparedStatement4 = connection.prepareStatement("UPDATE DONETASKS SET ID = @counter1:=@counter1+1 WHERE USERNAME = ? ORDER BY ID");
                preparedStatement4.setString(1,username);
                preparedStatement4.executeUpdate();

                PreparedStatement preparedStatement5 = connection.prepareStatement("DELETE FROM TASKS WHERE ID = ? AND USERNAME = ?");
                preparedStatement5.setInt(1,id);
                preparedStatement5.setString(2,username);
                preparedStatement5.executeUpdate();

                PreparedStatement preparedStatement6= connection.prepareStatement("SET @counter2 = 0");
                preparedStatement6.executeUpdate();

                PreparedStatement preparedStatement7 = connection.prepareStatement("UPDATE TASKS SET ID = @counter2:=@counter2+1 WHERE USERNAME = ? ORDER BY ID");
                preparedStatement7.setString(1,username);
                preparedStatement7.executeUpdate();

            }else {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TASKS SET " + category + " = ? WHERE USERNAME = ? AND ID = ?");
                preparedStatement.setString(1, changes);
                preparedStatement.setString(2, username);
                preparedStatement.setInt(3, id);
                preparedStatement.executeUpdate();
            }
            connection.close();
            System.out.println("Changes have been saved.");
        }catch (Exception e){
            System.out.println("Something went wrong. Check if you have entered the category correctly.");
        }
    }
    static void updateDoneTask(int id){
        System.out.println("Enter the name of the category you want to make changes to. \n• Title\n• Status\n• Deadline\n• HowImportant\n• Description");
        Scanner scanner = new Scanner(System.in);
        String category = scanner.nextLine();
        System.out.println("Enter new content for the " + category +".");
        String changes = scanner.nextLine();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            if(category.equalsIgnoreCase("Status") && (changes.equalsIgnoreCase("Not done") || changes.equalsIgnoreCase("In progress"))){
                PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT TITLE, DEADLINE, HOWIMPORTANT, DESCRIPTION FROM DONETASKS WHERE USERNAME = ? AND ID = ?");
                preparedStatement1.setString(1, username);
                preparedStatement1.setInt(2, id);
                ResultSet resultSet = preparedStatement1.executeQuery();
                if(resultSet.next()) {
                    String title = resultSet.getString(1);
                    String status = changes;
                    String deadline = resultSet.getString(2);
                    int howImportant = resultSet.getInt(3);
                    String description = resultSet.getString(4);

                    PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO TASKS (USERNAME, TITLE, STATUS, DEADLINE, HOWIMPORTANT, DESCRIPTION) VALUES (?,?,?,?,?,?)");
                    preparedStatement2.setString(1,username);
                    preparedStatement2.setString(2,title);
                    preparedStatement2.setString(3,changes);
                    preparedStatement2.setString(4,deadline);
                    preparedStatement2.setInt(5,howImportant);
                    preparedStatement2.setString(6,description);
                    preparedStatement2.executeUpdate();
                }

                PreparedStatement preparedStatement3= connection.prepareStatement("SET @counter1 = 0");
                preparedStatement3.executeUpdate();

                PreparedStatement preparedStatement4 = connection.prepareStatement("UPDATE TASKS SET ID = @counter1:=@counter1+1 WHERE USERNAME = ? ORDER BY ID");
                preparedStatement4.setString(1,username);
                preparedStatement4.executeUpdate();

                PreparedStatement preparedStatement5 = connection.prepareStatement("DELETE FROM DONETASKS WHERE ID = ? AND USERNAME = ?");
                preparedStatement5.setInt(1,id);
                preparedStatement5.setString(2,username);
                preparedStatement5.executeUpdate();

                PreparedStatement preparedStatement6= connection.prepareStatement("SET @counter2 = 0");
                preparedStatement6.executeUpdate();

                PreparedStatement preparedStatement7 = connection.prepareStatement("UPDATE DONETASKS SET ID = @counter2:=@counter2+1 WHERE USERNAME = ? ORDER BY ID");
                preparedStatement7.setString(1,username);
                preparedStatement7.executeUpdate();

            }else {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DONETASKS SET " + category + " = ? WHERE USERNAME = ? AND ID = ?");
                preparedStatement.setString(1, changes);
                preparedStatement.setString(2, username);
                preparedStatement.setInt(3, id);
                preparedStatement.executeUpdate();
            }
            connection.close();
            System.out.println("Changes have been saved.");
        }catch (Exception e){
            System.out.println("Something went wrong. Check if you have entered the category correctly.");
        }
    }
}
