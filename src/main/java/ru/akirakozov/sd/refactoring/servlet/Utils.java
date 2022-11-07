package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.function.Function;

public class Utils {
    private static final String dbUrl = "jdbc:sqlite:test.db";
    public static void collectProductsToResponse(HttpServletResponse response, Statement stmt, ResultSet rs) throws SQLException, IOException {
        while (rs.next()) {
            String name = rs.getString("name");
            long price = rs.getInt("price");
            response.getWriter().println(name + "\t" + price + "</br>");
        }
        response.getWriter().println("</body></html>");

        rs.close();
        stmt.close();
    }

    public static void executeQuery(HttpServletResponse response, String sqlCommand, Function<ResponseWriter, Void> callback) {
        try (Connection c = DriverManager.getConnection(dbUrl)) {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlCommand);
            callback.apply(new ResponseWriter(response, stmt, resultSet));
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeUpdate(String sqlCommand) {
        try (Connection c = DriverManager.getConnection(dbUrl)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sqlCommand);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    I ABSOLUTELY HATE JAVA
    public static class ResponseWriter {
        HttpServletResponse response;
        Statement stmt;
        ResultSet resultSet;

        public ResponseWriter(HttpServletResponse response, Statement stmt, ResultSet resultSet) {
            this.response = response;
            this.stmt = stmt;
            this.resultSet = resultSet;
        }
    }
}
