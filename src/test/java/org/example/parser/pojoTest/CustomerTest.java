package org.example.parser.pojoTest;


import lombok.Builder;
import lombok.Getter;
import org.example.pojo.Customer;
import org.example.pojo.Order;
import org.example.pojo.Product;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(setterPrefix = "with")
public class CustomerTest {

    @Builder.Default
    private UUID id = UUID.fromString("5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1");

    @Builder.Default
    private String firstName = "SimpleName";

    @Builder.Default
    private String lastName = "SimpleLastName";

    @Builder.Default
    private LocalDate dateBirth = LocalDate.parse("2023-11-08");

    @Builder.Default
    private List<Order> orders = getDefaultOrders();

    private static List<Order> getDefaultOrders() {
        Order order1 = new Order(UUID.fromString("a3b125bb-0889-4fcb-8e32-e2b3c42caebf"),
                getDefaultProducts(),
                Timestamp.valueOf(LocalDateTime.of(2023, 11, 9, 9, 8)));

        Order order2 = new Order(UUID.fromString("7572a3a6-e460-4629-a45e-e14150eebce7"),
                getDefaultProducts(),
                Timestamp.valueOf(LocalDateTime.of(2023, 11, 9, 9, 9)));
        return Arrays.asList(order1, order2);
    }

    private static List<Product> getDefaultProducts() {
        Product pr1 = new Product(UUID.fromString("ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c"), "Prod1", 1.22);
        Product pr2 = new Product(UUID.fromString("71934ae7-3cf3-48db-aeaa-8764ecae4c3b"), "Prod2", 2.33);
        Product pr3 = new Product(UUID.fromString("308db6bc-2a07-42ab-83a1-dc6f4c7ded52"), "Prod3", 3.44);
        return Arrays.asList(pr1, pr2, pr3);
    }

    public Customer buildCustomer() {
        return new Customer(id, firstName, lastName, dateBirth, orders);
    }
}
