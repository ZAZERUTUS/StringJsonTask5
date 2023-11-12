package org.example.pojo.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Elem1 {
    UUID id;
    String fieldElem1;
    String email;
    Double fieldNum;
    LocalDate dateCreate;
    List<Elem2> elems2;
}
