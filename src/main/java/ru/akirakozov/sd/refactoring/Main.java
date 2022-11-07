package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.servlet.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    private static final int serverPort = 8081;
    public static void main(String[] args) throws Exception {
        Utils.executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");

        Server server = new Server(serverPort);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()),"/query");

        server.start();
        server.join();
    }
}
