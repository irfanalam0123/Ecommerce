package com.ecommerce.project.securityResponsedto;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @Id
    private Long id;

    private  String jwtToken;
    private  String username;
    private List<String>roles;

    public LoginResponse(String username, String jwtToken, List<String> roles) {
    }
}



