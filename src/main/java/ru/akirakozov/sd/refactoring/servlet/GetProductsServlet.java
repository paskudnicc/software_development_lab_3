package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM PRODUCT");
            response.getWriter().println("<html><body>");
            Utils.collectProductsToResponse(response, stmt, resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
