package com.arkksoft.store.dto;

import lombok.*;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class CategoryDTO {
    private String name;
    private String description;
}
