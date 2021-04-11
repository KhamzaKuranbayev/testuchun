package module1.lesson_joins.task8;

import module1.lesson_joins.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String SQL_SELECT = "select p.maker from product p" +
                " join pc on pc.model = p.model and pc.speed >= 750" +
                " intersect" +
                " select p.maker from product p" +
                " join laptop l on l.model = p.model and l.speed >= 750;";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("maker");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("maker"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
