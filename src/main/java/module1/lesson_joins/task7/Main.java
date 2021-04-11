package module1.lesson_joins.task7;

import module1.lesson_joins.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String SQL_SELECT = "select maker, price from pc a " +
                "left join product b on a.model = b.model WHERE price = (SELECT MAX(price) FROM pc)";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("maker     price");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("maker") + "     ");
                System.out.print(resultSet.getString("price"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
