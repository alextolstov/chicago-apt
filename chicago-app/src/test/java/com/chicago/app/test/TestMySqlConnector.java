package com.chicago.app.test;

import com.chicago.ext.dal.DbConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMySqlConnector implements DbConnector
{
    private Connection _connection = null;
    public TestMySqlConnector()
    {
        connect("jdbc:mysql://108.50.217.209:3306/realestate?serverTimezone=EST5EDT&user=conclavia&password=2vjJcJy9_23o");
    }

    private void connect(String connectionString)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            _connection = DriverManager.getConnection(connectionString);
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public Connection getSession()
    {
        return _connection;
    }

    @Override
    public void close()
    {
        try
        {
            _connection.close();
        }catch (SQLException ex)
        {
        }
    }
}
