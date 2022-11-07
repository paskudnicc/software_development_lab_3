package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddProductServletTest extends ServletTestingBase {
    @Override
    void addServlet(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
    }

    String generateAddRequest(Product product) {
        return String.format("http://localhost:8081/add-product?name=%s&price=%d", product.name, product.price);
    }

    String generateBadAddRequest(String name, String price) {
        return String.format("http://localhost:8081/add-product?name=%s&price=%s", name, price);
    }


    @Test
    void basic() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            response = executeURL(new URL(generateAddRequest(tomato)));
            assertEquals("OK", response);
            ArrayList<Product> allProducts = getAllProducts();
            assertEquals(1, allProducts.size());
            Product actualTomato = allProducts.get(0);
            assertEquals(tomato.name, actualTomato.name);
            assertEquals(tomato.price, actualTomato.price);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void nonsense() {
        assertThrows(java.lang.RuntimeException.class, () -> {
            executeURL(new URL(generateBadAddRequest("makes no sense", "but really")));
        });
    }

    @Test
    void twoProducts() {
        String response;
        try {
            Product tomato = new Product("tomato", 150);
            Product apple = new Product("ringo", 300);
            response = executeURL(new URL(generateAddRequest(tomato)));
            assertEquals("OK", response);
            response = executeURL(new URL(generateAddRequest(apple)));
            assertEquals("OK", response);
            ArrayList<Product> allProducts = getAllProducts();
            assertEquals(2, allProducts.size());
            Product actualTomato;
            Product actualApple;
            if (Objects.equals(allProducts.get(0).name, tomato.name)) {
                actualTomato = allProducts.get(0);
                actualApple = allProducts.get(1);
            } else {
                actualApple = allProducts.get(0);
                actualTomato = allProducts.get(1);
            }
            assertEquals(tomato.name, actualTomato.name);
            assertEquals(tomato.price, actualTomato.price);
            assertEquals(apple.name, actualApple.name);
            assertEquals(apple.price, actualApple.price);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}