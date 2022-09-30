package com.database.database.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection connection = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        Connection connection2 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        log.info("connection= {}, class={}", connection, connection.getClass());
        log.info("connection= {}, class={}", connection2, connection2.getClass());
    }

    //DriverManagerDataSource 또한 새로운 커낵션을 매번 획득한다.

    @Test
    void driverManagerDataSource() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        userDatasource(dataSource);
    }

    private void userDatasource(DataSource dataSource) throws SQLException{
        Connection connection = dataSource.getConnection();
        Connection connection1 = dataSource.getConnection();

        log.info("connection= {}, class={}", connection, connection.getClass());
        log.info("connection= {}, class={}", connection1, connection1.getClass());
    }

    @Test
    void HikariDatasourceConnectionPool() throws SQLException{
        HikariDataSource hikari = new HikariDataSource();

        hikari.setJdbcUrl(ConnectionConst.URL);
        hikari.setUsername(ConnectionConst.USERNAME);
        hikari.setPassword(ConnectionConst.PASSWORD);
        hikari.setMaximumPoolSize(10);
        hikari.setPoolName("marcoPool");

        userDatasource(hikari);



    }


}
