package com.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Product {
    @Id
//    @SequenceGenerator(name = "products_id_seq", allocationSize = 1)
//    @GeneratedValue(generator = "products_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;

}

