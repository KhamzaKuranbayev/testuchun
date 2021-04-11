package module1.lesson_joins.task4;

import module1.lesson_joins.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String SQL_SELECT = "SELECT b.maker, a.model, a.price FROM printer a" +
                " JOIN product b ON a.model = b.model WHERE color = 'n'";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("maker   model   price");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("maker") + "       ");
                System.out.print(resultSet.getString("model") + "   ");
                System.out.println(resultSet.getString("price"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
