package module1.lesson_joins.extraTask.email;

import java.io.File;
import java.sql.*;
import java.util.List;

public class Email {

    private int email_id;
    private String subject;
    private String message;
    private User sender;
    private User receiver;
    private String created_at;
    private boolean status; // true - read false - unread
    private boolean isDeleted; // true - deleted false - not deleted
    private List<File> files;

    public Email(int email_id, String subject, String message, User sender, User receiver, String created_at, boolean status, boolean isDeleted) {
        this.email_id = email_id;
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.created_at = created_at;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Email(int email_id, String subject, String message, User sender, User receiver, String created_at, boolean status, boolean isDeleted, List<File> files) {
        this.email_id = email_id;
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.created_at = created_at;
        this.status = status;
        this.isDeleted = isDeleted;
        this.files = files;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(Connection connection) {

        String UPDATE_STATUS = "UPDATE \"Mail\" SET status = true WHERE mail_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS);
            preparedStatement.setInt(1, getEmail_id());

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "ID: " + email_id +
                "\nSubject: " + subject +
                "\nMessage: " + message +
                "\nCreated at: " + getCreated_at().substring(0, getCreated_at().lastIndexOf(':') + 3);
    }
}
