package org.example;

import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionManager.connect();
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("CREATE TABLE timetable (id INTEGER PRIMARY KEY AUTOINCREMENT, task STRING, priority INTEGER, status STRING)\n");
        } catch (SQLiteException e) {
            ;
            if (e.getErrorCode() != 1) {
                throw e;
            }
        }

        Scanner scanner = new Scanner(System.in);
        boolean isCorrectAnswer=false;
        while (!isCorrectAnswer) {
            System.out.println("Do you want to start a new day?");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("YES")) {
                statement.executeUpdate("DELETE FROM timetable; UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='timetable';");

                isCorrectAnswer=true;
            } else if (answer.equalsIgnoreCase("NO")) {
                isCorrectAnswer=true;
            } else {
                System.out.println("Answer must be 'YES' or 'NO'. Please try again.");
            }
        }

        boolean isTimetableActive = true;
        while (isTimetableActive) {
            System.out.println("Please choose the task: 1 = Add a new task, 2 = Amend status of existing task, 3 = Show task list in descending order, 4 = Close the timetable");
            try {
                int inputtedChoice = scanner.nextInt();
                if (inputtedChoice < 1 || inputtedChoice > 4) {
                    System.out.println("Incorrect selection. Please try again");
                } else if (inputtedChoice == 4) {
                    isTimetableActive = false;
                } else {
                    switch (inputtedChoice) {
                        case 1 -> {
                            System.out.println("Please clarify the name of the task");
                            Scanner scanner2 = new Scanner(System.in);
                            String task = scanner2.nextLine();
                            System.out.println("Please clarify the priority");
                            int priority = scanner2.nextInt();
                            String newTask = String.format("INSERT INTO timetable (task, priority, status) values('%s', %s, 'to Do')", task, priority);
                            statement.executeUpdate(newTask);
                        }
                        case 2 -> {
                            System.out.println("Please clarify the task ID");
                            Scanner scanner3 = new Scanner(System.in);
                            int id = scanner3.nextInt();
                            scanner3.nextLine();
                            System.out.println("Please select the new status of the task(inProgress/done/canceled)");
                            String status = scanner3.nextLine();
                            String updateStatus = String.format("UPDATE timetable SET status = '%s' WHERE id = %s", status, id);
                            statement.executeUpdate(updateStatus);
                        }
                        case 3 -> {
                            ResultSet rs = statement.executeQuery("SELECT * FROM timetable ORDER BY priority DESC");
                            while (rs.next()) {
                                System.out.println("id = " + rs.getString("id") + "; task = " + rs.getString("task") + "; priority = " + rs.getString("priority") + "; status = " + rs.getString("status"));
                            }
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserted wrong number. Please try again");
                scanner.next();
            }
        }
        ConnectionManager.close(connection);
    }

}
