package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetProductsServletTest extends ServletTestingBase {
    @Override
    void addServlet(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
    }

    final String getUrl = "http://localhost:8081/get-products";

    @Test
    void empty() {
        String response;
        try {
            response = executeURL(new URL(getUrl));
            assertEquals("<html><body></body></html>", response.replaceAll("(?!\t)\\s++", ""));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void basic() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            Product apple = new Product("ringo", 300);
            addProduct(tomato);
            addProduct(apple);
            response = executeURL(new URL(getUrl));
            boolean simpleOrder = format("<html><body>%s\t%d</br>%s\t%d</br></body></html>",
                    tomato.name, tomato.price, apple.name, apple.price)
                    .equals(response.replaceAll("(?!\t)\\s++", ""));
            boolean reverseOrder = format("<html><body>%s\t%d</br>%s\t%d</br></body></html>",
                    apple.name, apple.price, tomato.name, tomato.price)
                    .equals(response.replaceAll("(?!\t)\\s++", ""));
            assertTrue(simpleOrder || reverseOrder);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}