package module1.lesson_joins.task1;

import module1.lesson_joins.ConnectionDB;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        String SQL_SELECT = "SELECT model, speed, ram, hd, price FROM laptop WHERE price >= 900" +
                "UNION " +
                "SELECT model, speed, ram, hd, price FROM pc " +
                "WHERE price >= 900";

        try {
            Connection connection = ConnectionDB.getConnection("computerdb");
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("model    speed    ram    hd    price");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("model") + "      " + resultSet.getString("speed"));
                System.out.print("     " + resultSet.getString("ram") + "      " + resultSet.getString("hd"));
                System.out.println("    " + resultSet.getString("price"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
