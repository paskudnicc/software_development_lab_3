package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Utils.executeQuery(response, "SELECT * FROM PRODUCT", (responseWriter -> {
            try {
                responseWriter.response.getWriter().println("<html><body>");
                Utils.collectProductsToResponse(responseWriter.response, responseWriter.stmt, responseWriter.resultSet);
                return null;
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }));

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
