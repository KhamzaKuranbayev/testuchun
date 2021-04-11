package module1.lesson_joins.task3;

import module1.lesson_joins.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        String SQL_SELECT = "SELECT maker FROM product WHERE type = 'pc'" +
                "EXCEPT " +
                "SELECT maker FROM product WHERE type = 'laptop'";

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
