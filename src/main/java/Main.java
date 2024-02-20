import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean userIsLoggedin = false;
        System.out.println("Hi! This is your to-do list. If you want to log in, enter \"login\", but if you want to create a new account, enter \"registration\".");
        while (!userIsLoggedin) {
            Scanner scanner = new Scanner(System.in);
            String answer1 = scanner.next();
            if (answer1.equalsIgnoreCase("login")) {
                userIsLoggedin = ToDoList.Login();
            }
            else if(answer1.equalsIgnoreCase("registration")) {
                ToDoList.Registration();
                System.out.println("Your account has been created. You can now log in, enter \"login\".");
            } else System.out.println("Please try entering again.");
        }
        while (userIsLoggedin){
            System.out.println("What do you want to do now? Enter the command number please.\n1. Add new task\n2. Show all my tasks to do\n3. Show the most important tasks to do\n4. Show completed tasks");
            Scanner scanner = new Scanner(System.in);
            int answer1 = scanner.nextInt();
            if(answer1==1) {
                ToDoList.addTask();
            }
            else if(answer1==2) {
                while(answer1==2) {
                    ToDoList.showTasks();
                    System.out.println("If you want to see the details of a specific task, enter the appropriate number. If you want to go back to the menu, type \"back\".");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        ToDoList.showTask(id);
                        while (answer1==2) {
                            String answer2 = scanner.next();
                            if (answer2.equalsIgnoreCase("Back")) break;
                            else if (answer2.equalsIgnoreCase("Delete")) {
                                ToDoList.deleteTask(id);
                                break;
                            } else if (answer2.equalsIgnoreCase("Update")) {
                                ToDoList.updateTask(id);
                                break;
                            } else System.out.println("Please try entering again.");
                        }
                    }else {
                        while (answer1==2) {
                            String answer2 = scanner.next();
                            if (answer2.equalsIgnoreCase("Back")) break;
                            else System.out.println("Please try entering again.");
                        }
                        break;
                    }
                }
            }
            else if(answer1==3) {
                while(answer1==3) {
                    ToDoList.showImportantTasks();
                    System.out.println("If you want to see the details of a specific task, enter the appropriate number. If you want to go back to the menu, type \"back\".");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        ToDoList.showTask(id);
                        while (answer1==3) {
                            String answer2 = scanner.next();
                            if (answer2.equalsIgnoreCase("Back")) break;
                            else if (answer2.equalsIgnoreCase("Delete")) {
                                ToDoList.deleteTask(id);
                                break;
                            } else if (answer2.equalsIgnoreCase("Update")) {
                                ToDoList.updateTask(id);
                                break;
                            } else System.out.println("Please try entering again.");
                        }
                    }else {
                        while (answer1==3) {
                            String answer2 = scanner.next();
                            if (answer2.equalsIgnoreCase("Back")) break;
                            else System.out.println("Please try entering again.");
                        }
                        break;
                    }
                }
            }
            else if(answer1==4) {
                while(answer1==4) {
                    ToDoList.showDoneTasks();
                    System.out.println("If you want to see the details of a specific task, enter the appropriate number. If you want to go back to the menu, type \"back\".");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        ToDoList.showDoneTask(id);
                        while (answer1==4) {
                            String answer2 = scanner.next();
                            if (answer2.equalsIgnoreCase("Back")) break;
                            else if (answer2.equalsIgnoreCase("Delete")) {
                                ToDoList.deleteDoneTask(id);
                                break;
                            } else if (answer2.equalsIgnoreCase("Update")) {
                                ToDoList.updateDoneTask(id);
                                break;
                            } else System.out.println("Please try entering again.");
                        }
                    }else {
                        while (answer1==4) {
                            String answer2 = scanner.next();
                            if (answer2.equalsIgnoreCase("Back")) break;
                            else System.out.println("Please try entering again.");
                        }
                        break;
                    }
                }
            }
            else System.out.println("Please try entering again.");
        }
    }
}
