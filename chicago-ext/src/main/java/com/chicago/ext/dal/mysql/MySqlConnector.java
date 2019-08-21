package com.chicago.ext.dal.mysql;

import com.chicago.dto.Config;
import com.chicago.ext.dal.DbConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector implements DbConnector<Connection>
{
    private static final Logger LOG = LoggerFactory.getLogger(MySqlConnector.class);
    private Connection _connection = null;

    @Inject
    private Config.MySqlConfig mysqlConfig;

    @PostConstruct
    private void postConstruct()
    {
        connect(mysqlConfig.getConnectionString());
    }

    private void connect(String connectionString)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            _connection = DriverManager.getConnection(connectionString);
        }
        catch (SQLException ex)
        {
            LOG.error(ex.getMessage());
        } catch (ClassNotFoundException ex)
        {
            LOG.error(ex.getMessage());
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
            LOG.error(ex.getMessage());
        }
    }
}
