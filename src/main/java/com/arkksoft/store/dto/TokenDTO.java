package com.arkksoft.store.dto;

import java.util.List;

import lombok.*;;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String email;
    private List<String> roles;
}
