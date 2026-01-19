package com.ecommerce.project.categroyContrller;

import com.ecommerce.project.security.jwt.JWUtils;
import com.ecommerce.project.securityRequest.LoginRequest;
import com.ecommerce.project.securityResponsedto.LoginResponse;
import com.ecommerce.project.userSecurityService.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWUtils jwUtils;


    @PostMapping("/loginin")
    public ResponseEntity<?> logindata(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

        } catch (AuthenticationException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "bad credential");
            body.put("status", false);

            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails= (UserDetailsImp) authentication.getPrincipal();
        String jwtToken=jwUtils.generateFromUserName(userDetails);

        List<String> roles=userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        LoginResponse response=new LoginResponse(userDetails.getUsername(), jwtToken,roles);

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

//@PostMapping("/signup")


}
