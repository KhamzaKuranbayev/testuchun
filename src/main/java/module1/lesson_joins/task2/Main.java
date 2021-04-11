package module1.lesson_joins.task2;

import module1.lesson_joins.ConnectionDB;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        String SQL_SELECT = "SELECT maker FROM product WHERE type = 'laptop'" +
                "INTERSECT " +
                "SELECT maker FROM product WHERE type = 'printer'";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.print(resultSet.getString("maker"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
