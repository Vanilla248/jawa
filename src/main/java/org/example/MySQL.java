package org.example;

import org.apache.logging.log4j.LogManager;

import java.sql.*;

public class MySQL {

    private static Connection connection;
    public MySQL() {
        String url = "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "#Mhr2843296627";
        try {
            connection = DriverManager.getConnection(url, user, password);
            // 连接成功
            System.out.println("连接成功");
        }catch (SQLException e){
            LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
        }
    }

    public int checkType0And1(String username, String password,int type) {
        String selectSQL;
        if(type==0){
            selectSQL = "SELECT * FROM user_information WHERE email =?";
        }else {
           selectSQL = "SELECT * FROM user_information WHERE name =?";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)){
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    String storedPassword = resultSet.getString("password");
                    if (storedPassword.equals(password)) {
                        return 0; // 密码匹配返回 0
                    } else {
                        return 2; // 密码不匹配返回 2
                    }
                }
            }
        } catch (SQLException e) {
            LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
        }
        return 1; // 用户名不存在返回 1
    }

    public int checkType2(String email,int type) {
        String selectSQL="SELECT * FROM user_information WHERE email =?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)){
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    if(type==0){
                        SendEmail.sendEmail(email,this);
                        return 0;
                    } else {
                        return 1;
                    }
                }
            }
        } catch (SQLException e) {
            LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
        }
        if(type==0){
            return 1;
        } else {
            SendEmail.sendEmail(email,this);
            return 0;
        }
    }

    public void insertEmailNumber(String email,String number) {
        String selectSQL = "SELECT * FROM email_number WHERE email = ?";
        String updateSQL = "UPDATE email_number SET number = ? WHERE email = ?";
        String insertSQL = "INSERT INTO email_number (email, number) VALUES (?, ?)";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
            selectStatement.setString(1, email);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 如果找到email，则更新number
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                        updateStatement.setString(1, number);
                        updateStatement.setString(2, email);
                        updateStatement.executeUpdate();
                        System.out.println("数据更新成功");
                    }
                } else {
                    // 如果未找到email，则插入新记录
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                        insertStatement.setString(1, email);
                        insertStatement.setString(2, number);
                        insertStatement.executeUpdate();
                        System.out.println("数据插入成功");
                    }
                }
            }
        } catch (SQLException e) {
            LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
        }
    }

    public int checkType3(String email,String number) {
        String deleteSQL = "DELETE FROM email_number WHERE email = ? AND number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, email); // 绑定 email
            preparedStatement.setString(2, number); // 绑定 number

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("删除成功: 已删除的行数为 " + rowsAffected);
                return 0;
            } else {
                System.out.println("未找到匹配的记录，无法删除");
                return 1;
            }
        } catch (SQLException e) {
            LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
        }
        return -1; // 未找到邮箱，未定义行为
    }

    public int checkType4(String email,String password) {
        String updateSQL = "UPDATE user_information SET password = ? WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, password); // 绑定新的密码
            preparedStatement.setString(2, email); // 绑定需要查找的 email

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("密码更新成功: 已更新的行数为 " + rowsAffected);
                return 0;
            } else {
                System.out.println("未找到指定的 email，无法更新密码");
                return -1;
            }
        } catch (SQLException e) {
            LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
        }
        return -1; // 未找到邮箱，未定义行为
    }

    public int checkType5(String name,String email,String number) {
        int result=checkType3(email,number);
        if(result==0){
            String selectSQL = "SELECT * FROM user_information WHERE name = ?";
            String insertSQL = "INSERT INTO user_information (name,email, password) VALUES (?, ?,?)";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setString(1, name);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        // 如果未找到用户名，则插入新记录
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                            insertStatement.setString(1, name);
                            insertStatement.setString(2, email);
                            insertStatement.setString(3, "$aA123456$");
                            insertStatement.executeUpdate();
                            System.out.println("用户插入成功");
                        }
                    } else {
                        System.out.println("用户已存在，无法插入");
                        result=2;
                    }
                }
            } catch (SQLException e) {
                LogManager.getLogger(MySQL.class).error("发生 SQL Exception", e);
            }
        }
        return result;
    }
}
