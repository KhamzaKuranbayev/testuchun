package module1.lesson_joins.task5;

import module1.lesson_joins.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String SQL_SELECT = "SELECT AVG(a.price) as price FROM pc a" +
                " JOIN product b ON a.model = b.model WHERE b.maker = 'A'";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("price");
            while (resultSet.next()) {
                System.out.printf("%.2f", Float.parseFloat(resultSet.getString("price")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
