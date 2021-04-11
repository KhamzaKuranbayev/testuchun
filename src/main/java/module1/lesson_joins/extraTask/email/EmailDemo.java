package module1.lesson_joins.extraTask.email;

import module1.lesson_joins.ConnectionDB;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmailDemo {

    public static User current_user = null;
    public static Connection connection = ConnectionDB.getConnection("mail_db");
    public static Scanner scanner = new Scanner(System.in);

    public static List<Integer> files = new ArrayList<Integer>();

    public static void main(String[] args) {
        while (true) {
            mainMenu();
            int main_choice = scanner.nextInt();
            if (main_choice == 1) {
                signIn();
            } else if (main_choice == 2) {
                signUp();
            } else {
                return;
            }
        }
    }

    private static void mainMenu() {
        System.out.println("Main Menu");
        System.out.println("1. Sign in");
        System.out.println("2. Sign up");
        System.out.println("0. Exit");
    }

    private static void signUp() {
        System.out.print("Email: ");
        String email = scanner.next();
        System.out.print("Firstname: ");
        String firstname = scanner.next();
        System.out.print("Lastname: ");
        String lastname = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        insertUser(email, password, firstname, lastname);
    }

    private static void signIn() {
        System.out.print("Email: ");
        String email = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();

        if (!"".equals(getUserPassword(email))) {
            if (getUserPassword(email).equals(password)) {
                loadCurrentUserData(email);

                boolean b = true;
                while (b) {
                    System.out.println("\n" + current_user.getFirstname() + " " + current_user.getLastname() +
                            " | " + current_user.getEmail_address());
                    if (getCountUnreadMessages() > 0) {
                        System.out.println("Unread messages: " + getCountUnreadMessages());
                    }
                    printOperations();

                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            viewEmailList(email);
                            sentEmail();
                            break;
                        case 2:
                            viewUnreadEmails();
                            break;
                        case 3:
                            viewInbox();
                            break;
                        case 4:
                            viewOutbox();
                            break;
                        case 5:
                            viewTrash();
                            break;
                        case 6:
                            changePassword();
                            break;
                        case 7:
                            System.out.println("");
                            b = false;
                            break;
                    }
                }
            } else {
                System.out.println("The password is not correct!\n");
            }
        } else {
            System.out.println("Bunday email topilmadi!\n");
        }
    }

    private static void viewTrash() {
        if (getCountDeletedMessages() != 0) {
            String SENT_MESSAGES = "SELECT * FROM \"Mail\" WHERE is_deleted = true and receiver_id =?";
            getMessageFromDB(SENT_MESSAGES, "Trash");

            System.out.println("X - DELETE FOREVER R - RECOVER C - CANCEL");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("x")) {
                System.out.print("Mail id: ");
                int mail_id = scanner.nextInt();
                deleteForeverMailById(mail_id);
            } else if (choice.equalsIgnoreCase("r")) {
                System.out.print("Mail id: ");
                int mail_id = scanner.nextInt();
                recoverMailById(mail_id);
            }
        } else {
            System.out.println("You don't have deleted messages!");
        }
    }

    private static void recoverMailById(int mail_id) {
        String DELETE_MAIL = "UPDATE \"Mail\" SET is_deleted = false WHERE mail_id = " + mail_id;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MAIL);
            preparedStatement.executeUpdate();

            System.out.println("Mail recovered!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void deleteForeverMailById(int mail_id) {
        String DELETE_MAIL = "DELETE FROM \"Mail\" WHERE mail_id = " + mail_id;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MAIL);
            preparedStatement.executeUpdate();

            System.out.println("Mail deleted forever!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static int getCountDeletedMessages() {
        String DELETED_MESSAGES = "SELECT COUNT(*) FROM \"Mail\" WHERE is_deleted = true and receiver_id = " + current_user.getUser_id();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETED_MESSAGES);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return 0;
    }

    private static void changePassword() {

    }

    private static void viewInbox() {
        if (getCountReceiveMessages() != 0) {
            String SENT_MESSAGES = "SELECT * FROM \"Mail\" WHERE is_deleted = false AND receiver_id =?";
            getMessageFromDB(SENT_MESSAGES, "Inbox");
            System.out.println("X - DELETE MESSAGE C - CANCEL");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("x")) {
                System.out.print("Mail id: ");
                int mail_id = scanner.nextInt();
                deleteMailById(mail_id);
            }
        } else {
            System.out.println("You don't have receive messages!");
        }
    }

    private static void deleteMailById(int mail_id) {
        String DELETE_MAIL = "UPDATE \"Mail\" SET is_deleted = true WHERE mail_id = " + mail_id;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MAIL);
            preparedStatement.executeUpdate();

            System.out.println("Mail deleted!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void viewOutbox() {
        if (getCountSentMessages() != 0) {
            String SENT_MESSAGES = "SELECT * FROM \"Mail\" WHERE is_deleted = false AND sender_id =?";
            getMessageFromDB(SENT_MESSAGES, "Outbox");
        } else {
            System.out.println("You don't have sent messages!");
        }
    }

    private static void viewUnreadEmails() {
        if (getCountUnreadMessages() != 0) {
            String UNREAD_MESSAGES = "SELECT * FROM \"Mail\" WHERE is_deleted = false AND status = false AND receiver_id =?";
            getMessageFromDB(UNREAD_MESSAGES, "UNREAD");
        } else {
            System.out.println("You don't have unread messages!");
        }
    }

    private static void getMessageFromDB(String SQL_QUERY, String action) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY);
            preparedStatement.setInt(1, current_user.getUser_id());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Email> emailList = new ArrayList<Email>();
            while (resultSet.next()) {
                int email_id = resultSet.getInt("mail_id");
                String subject = resultSet.getString("subject");
                String message = resultSet.getString("message");
                Integer senderId = resultSet.getInt("sender_id");
                User sender = getUser(senderId);
                Integer receiverId = resultSet.getInt("receiver_id");
                User receiver = getUser(receiverId);
                String date = resultSet.getString("created_at");
                boolean status = resultSet.getBoolean("status");
                boolean isDeleted = resultSet.getBoolean("is_deleted");

                Email email = new Email(email_id, subject, message, sender, receiver, date, status, isDeleted);
                emailList.add(email);
            }
            printEmails(emailList, action);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void printEmails(List<Email> emailList, String action) {

        System.out.println("-----------------" + action + "----------------------\n");
        for (Email email : emailList) {
            if (email != null) {
                System.out.println(email.toString());
                if (action.equals("UNREAD") || action.equals("Inbox") || action.equals("Trash")) {
                    System.out.println("Sender: " + email.getSender().toString());
                } else {
                    System.out.println("Receiver: " + email.getReceiver().toString());
                }
                email.setStatus(connection);
                System.out.print("\n");
            }
        }
        System.out.println("---------------------------------------------");
        emailList.clear();
    }

    public static User getUser(Integer userId) {
        String selectQuery = "SELECT * FROM \"User\" WHERE user_id='" + userId + "'";

        User currentUser = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {

                int id = resultSet.getInt("user_id");
                String emailR = resultSet.getString("email_address");
                String resultPassword = resultSet.getString("password");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");

                currentUser = new User(id, emailR, resultPassword, firstName, lastName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currentUser;

    }

    private static void sentEmail() {
        boolean b = true;
        while (b) {
            System.out.println("New Message");
            System.out.print("To: ");
            String toEmail = scanner.next();
            String receiverId = getReceiverEmail(toEmail);

            if (receiverId != null) {
                scanner = new Scanner(System.in);

                System.out.print("Subject: ");
                String subject = scanner.nextLine();
                System.out.println("Message:");
                String message = scanner.nextLine();

                boolean b1 = false;
                while (true) {
                    System.out.println("+ UPLOAD FILE  - CANCEL");
                    String choice = scanner.next();
                    if (choice.equals("+")) {
                        System.out.println("Choose file");
                        System.out.println("--------------------");
                        File file = new File("src/main/resources/files/");
                        printDataInFolder(file);
                        System.out.print("Choose index: ");
                        int choiceIndex = scanner.nextInt();
                        File file1 = getFileByIndex(file, choiceIndex);
                        insertFile(file1);
                        System.out.println("Done!");
                        int file_id = getLastFileId();
                        files.add(file_id);
                        b1 = true;
                    } else {
                        break;
                    }
                }

                sendNewMessage(subject, message, current_user.getUser_id(), receiverId);
                if (b1) {
                    int mail_id = getLastMailId();
                    for (Integer file_id : files) {
                        insertMailFile(mail_id, file_id);
                    }
                }

                System.out.println("The message sent successfully!\n");
                b = false;
            } else {
                System.out.println("The receiver email was not found!\n");
            }
        }
    }

    private static void insertMailFile(int mail_id, int file_id) {
        String INSERT_FILE = "INSERT INTO mail_files (mail_id, file_id) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FILE);
            preparedStatement.setInt(1, mail_id);
            preparedStatement.setInt(2, file_id);

            preparedStatement.executeUpdate();
            System.out.println();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static int getLastFileId() {
        String SELECT_LAST_FILE_ID = "SELECT file_id FROM \"File\" ORDER BY date DESC LIMIT 1";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LAST_FILE_ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("file_id");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    private static int getLastMailId() {
        String SELECT_LAST_MAIl_ID = "SELECT mail_id FROM \"Mail\" ORDER BY created_at DESC LIMIT 1";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_LAST_MAIl_ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("mail_id");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    private static void insertFile(File file1) {
        String INSERT_FILE = "INSERT INTO \"File\" (path, name, length, date) VALUES (?, ?, ?, now())";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FILE);
            preparedStatement.setString(1, file1.getPath());
            preparedStatement.setString(2, file1.getName());
            preparedStatement.setInt(3, (int) file1.length());

            preparedStatement.executeUpdate();
            System.out.println();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static File getFileByIndex(File file, int index) {
        File[] files = file.listFiles();
        int index2 = 1;
        if (files.length > 0) {
            for (File file1 : files) {
                if (index == index2) {
                    return file1;
                }
                index2++;
            }
        }
        return null;
    }

    private static void printDataInFolder(File file) {
        File[] files = file.listFiles();
        int index = 1;
        if (files.length > 0) {
            for (File file1 : files) {
                System.out.println(index + ". " + file1.getName());
                index++;
            }
        }
        System.out.println("--------------------");
    }

    private static void sendNewMessage(String subject, String message, int sender_id, String receiverId) {
        String INSERT_NEW_MESSAGE = "INSERT INTO \"Mail\" (subject, message, sender_id, receiver_id, created_at, status, is_deleted)" +
                " VALUES(?, ?, ?, ?, now(), false, false)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_MESSAGE);
            preparedStatement.setString(1, subject);
            preparedStatement.setString(2, message);
            preparedStatement.setInt(3, sender_id);
            preparedStatement.setInt(4, Integer.parseInt(receiverId));

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static String getReceiverEmail(String toEmail) {

        String RECEIVER_ID = "SELECT user_id FROM \"User\" WHERE email_address = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(RECEIVER_ID);
            preparedStatement.setString(1, toEmail);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    private static void viewEmailList(String email) {
        String all_email_address = "SELECT email_address FROM \"User\"";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(all_email_address);

            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("\nList of emails:");
            System.out.println("---------------------------------");
            while (resultSet.next()) {
                if (!resultSet.getString("email_address").equals(email))
                    System.out.println(resultSet.getString("email_address"));
            }
            System.out.println("---------------------------------\n");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static int getCountReceiveMessages() {
        String SENT_MESSAGES = "SELECT COUNT(*) FROM \"Mail\" WHERE is_deleted = false AND receiver_id = " + current_user.getUser_id();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SENT_MESSAGES);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return 0;
    }

    private static int getCountSentMessages() {
        String SENT_MESSAGES = "SELECT COUNT(*) FROM \"Mail\" WHERE is_deleted = false AND sender_id = " + current_user.getUser_id();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SENT_MESSAGES);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return 0;
    }

    private static int getCountUnreadMessages() {

        String UNREAD_MESSAGES = "SELECT COUNT(*) FROM \"Mail\" WHERE is_deleted = false AND status = false AND receiver_id = " + current_user.getUser_id();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UNREAD_MESSAGES);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return resultSet.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return 0;
    }

    private static void loadCurrentUserData(String email) {

        String CURRENT_USER = "SELECT user_id, email_address, password, firstname, lastname FROM \"User\" " +
                "WHERE email_address = '" + email + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CURRENT_USER);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                Integer userId = resultSet.getInt("user_id");
                String email_address = resultSet.getString("email_address");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");

                current_user = new User(userId, email_address, password, firstName, lastName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insertUser(String email, String firstname, String lastname, String password) {
        String USER_INSERT = "INSERT INTO \"User\" (email_address, password, firstname, lastname) VALUES (?, ?, ?, ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(USER_INSERT);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, firstname);
            preparedStatement.setString(3, lastname);
            preparedStatement.setString(4, password);

            preparedStatement.executeUpdate();
            System.out.println("The new user was successfully registered!\n");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getUserPassword(String email) {
        String USER_PASSWORD = "SELECT password FROM \"User\" WHERE email_address = '" + email + "'";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(USER_PASSWORD);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return "";
    }

    public static void printOperations() {

        System.out.println("\nChoose the operation:");
        System.out.println("1-Send email");
        System.out.println("2-View unread");
        System.out.println("3-Inbox");
        System.out.println("4-Outbox");
        System.out.println("5-Trash");
        System.out.println("6-Change Password");
        System.out.println("7-Log Out");
    }


}
