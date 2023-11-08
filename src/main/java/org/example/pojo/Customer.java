package org.example.pojo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Customer {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dateBirth;
    private List<Order> orders;
}
