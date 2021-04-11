package module1.lesson_joins.task6;

import module1.lesson_joins.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String SQL_SELECT = "SELECT DISTINCT b.maker FROM pc a " +
                "JOIN product b ON a.model = b.model WHERE a.speed >= 450";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("MAKER(S):");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("maker"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
