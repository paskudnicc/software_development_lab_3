package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ServletTestingBase {
//    why is product db named test? don't want to mess with it so "test_test" it is then
    private final String dbURL = "jdbc:sqlite:test_test.db";

    Server server;
    final int PORT = 8081;

    abstract void addServlet(ServletContextHandler context);

    @BeforeAll
    void create() {
        try (Connection c = DriverManager.getConnection(dbURL)) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        addServlet(context);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void flush() {
        try (Connection c = DriverManager.getConnection(dbURL)) {
            String sql = "DELETE FROM PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
