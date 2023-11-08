package org.example;

import org.example.parser.CustomParser;
import org.example.pojo.Customer;
import org.example.pojo.Order;
import org.example.pojo.Product;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Product pr1 = new Product(UUID.randomUUID(), "Prod1", 1.22);
        Product pr2 = new Product(UUID.randomUUID(), "Prod2", 2.33);
        Product pr3 = new Product(UUID.randomUUID(), "Prod3", 3.44);

        Order order1 = new Order(UUID.randomUUID(), Arrays.asList(pr1, pr2, pr3), OffsetDateTime.now());
        Order order2 = new Order(UUID.randomUUID(), Arrays.asList(pr3, pr2, pr1), OffsetDateTime.now());

        Customer customer = new Customer(UUID.randomUUID(), "FirstName", "LastName", LocalDate.now(), Arrays.asList(order1, order2));

        System.out.println(CustomParser.serialize(customer));
    }
}