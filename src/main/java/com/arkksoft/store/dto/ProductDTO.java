package com.arkksoft.store.dto;

import lombok.*;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private Float price;
    private String brand;
    private Integer model;
    private String description;
    private String image;
    private Long categoryId;
}
