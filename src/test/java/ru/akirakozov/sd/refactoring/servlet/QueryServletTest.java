package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryServletTest extends ServletTestingBase {

    @Override
    void addServlet(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");
    }

    String generateQueryRequest(String query) {
        return String.format("http://localhost:8081/query?command=%s", query);
    }

    @Test
    void max() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            Product apple = new Product("ringo", 300);
            Product uranium = new Product("uranium", 9000);
            addProduct(tomato);
            addProduct(apple);
            addProduct(uranium);
            response = executeURL(new URL(generateQueryRequest("max")));
            assertEquals("<html><body><h1>Product with max price: </h1>uranium\t9000</br></body></html>",
                    response.replaceAll("(?![\t ])\\s++", ""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void min() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            Product apple = new Product("ringo", 300);
            Product uranium = new Product("uranium", 9000);
            addProduct(tomato);
            addProduct(apple);
            addProduct(uranium);
            response = executeURL(new URL(generateQueryRequest("min")));
            assertEquals("<html><body><h1>Product with min price: </h1>tomato\t150</br></body></html>",
                    response.replaceAll("(?![\t ])\\s++", ""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sum() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            Product apple = new Product("ringo", 300);
            Product uranium = new Product("uranium", 9000);
            addProduct(tomato);
            addProduct(apple);
            addProduct(uranium);
            response = executeURL(new URL(generateQueryRequest("sum")));
            assertEquals("<html><body>Summary price: 9450</body></html>",
                    response.replaceAll("(?![\t ])\\s++", ""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void count() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            Product apple = new Product("ringo", 300);
            Product uranium = new Product("uranium", 9000);
            addProduct(tomato);
            addProduct(apple);
            addProduct(uranium);
            response = executeURL(new URL(generateQueryRequest("count")));
            assertEquals("<html><body>Number of products: 3</body></html>",
                    response.replaceAll("(?![\t ])\\s++", ""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

