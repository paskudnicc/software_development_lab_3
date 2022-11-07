package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private static Function<Utils.ResponseWriter, Void> generateCallbackForCollector(String header) {
        return responseWriter -> {
            try {
                responseWriter.response.getWriter().println("<html><body>");
                responseWriter.response.getWriter().println(format("<h1>%s</h1>", header));
                Utils.collectProductsToResponse(responseWriter.response, responseWriter.stmt, responseWriter.resultSet);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        };
    }

    private static Function<Utils.ResponseWriter, Void> generateCallbackForIntQuery(String header) {
        return responseWriter -> {
            try {
                responseWriter.response.getWriter().println("<html><body>");
                responseWriter.response.getWriter().println(header);

                if (responseWriter.resultSet.next()) {
                    responseWriter.response.getWriter().println(responseWriter.resultSet.getInt(1));
                }
                responseWriter.response.getWriter().println("</body></html>");
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        };
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            Utils.executeQuery(response, "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                    generateCallbackForCollector("Product with max price: "));
        } else if ("min".equals(command)) {
            Utils.executeQuery(response, "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                    generateCallbackForCollector("Product with min price: "));
        } else if ("sum".equals(command)) {
            Utils.executeQuery(response, "SELECT SUM(price) FROM PRODUCT",
                    generateCallbackForIntQuery("Summary price: "));
        } else if ("count".equals(command)) {
            Utils.executeQuery(response, "SELECT COUNT(*) FROM PRODUCT",
                    generateCallbackForIntQuery("Number of products: "));
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
