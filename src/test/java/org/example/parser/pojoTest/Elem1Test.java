package org.example.parser.pojoTest;

import org.example.pojo.test.Elem1;
import org.example.pojo.test.Elem2;
import org.example.pojo.test.Elem3;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class Elem1Test {
    public static Elem1 getElem1() {
        Elem3 elem31 = new Elem3(UUID.randomUUID(), "wdqwdqwd");
        Elem3 elem32 = new Elem3(UUID.randomUUID(), "baervgsafsv");
        Elem3 elem33 = new Elem3(UUID.randomUUID(), "wdqwdfervbsgnnhbqwd");
        Elem3 elem34 = new Elem3(UUID.randomUUID(), "wdqwdqrtyhvf5654wd");

        Elem2 elem21 = new Elem2("dwqdqwd", LocalDate.now(), Arrays.asList(elem34, elem31, elem32, elem31, elem34));
        Elem2 elem22 = new Elem2("dwqdqwd", LocalDate.now(), Arrays.asList(elem34, elem33, elem32, elem32, elem34));
        Elem2 elem23 = new Elem2("dwqdqwd", LocalDate.now(), Arrays.asList(elem34, elem31, elem31, elem32, elem34));
        Elem2 elem24 = new Elem2("dwqdqwd", LocalDate.now(), Arrays.asList(elem34, elem33, elem32, elem32, elem34));
        Elem2 elem25 = new Elem2("dwqdqwd", LocalDate.now(), Arrays.asList(elem34, elem33, elem32, elem31, elem34));

        return new Elem1(UUID.randomUUID(), "adwefwe", "dfweff",23.323, LocalDate.now(), Arrays.asList(elem21, elem22, elem23, elem24, elem25));
    }
}
