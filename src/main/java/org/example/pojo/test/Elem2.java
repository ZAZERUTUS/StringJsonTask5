package org.example.pojo.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Elem2 {
    String customName;
    LocalDate fieldTimestamp;
    List<Elem3> elem3s;
}
