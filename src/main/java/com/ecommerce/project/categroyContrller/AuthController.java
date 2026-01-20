package com.ecommerce.project.categroyContrller;

import com.ecommerce.project.CategoryModel.AppRole;
import com.ecommerce.project.CategoryModel.Role;
import com.ecommerce.project.CategoryModel.User;
import com.ecommerce.project.categoryRepository.RoleRepository;
import com.ecommerce.project.categoryRepository.UserRepository;
import com.ecommerce.project.security.jwt.JWUtils;
import com.ecommerce.project.securityRequest.LoginRequest;
import com.ecommerce.project.securityRequest.SignupRequest;
import com.ecommerce.project.securityResponsedto.LoginResponse;
import com.ecommerce.project.securityResponsedto.ResponseMessage;
import com.ecommerce.project.userSecurityService.UserDetailsImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWUtils jwUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;


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
        ResponseCookie jwtCookie=jwUtils.generateCookieFromjwt(userDetails);
       // generateuser name v use kar sakteh hai fec change
        List<String> roles=userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        LoginResponse response=new
                LoginResponse(userDetails.getId(),userDetails.getUsername(),
                jwtCookie.toString(),roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                .body(response);

    }
    @PostMapping("/signup")
    public ResponseEntity<?>registerUser(@Valid @RequestBody SignupRequest signupRequest){

        if(userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new ResponseMessage("name is already exit"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new ResponseMessage("email is already exit"));
        }


        User user=new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())
        );

        Set<String>strRole=signupRequest.getRole();
          Set< Role >roles=new HashSet<>();

          if(strRole == null){
              Role userRole=roleRepository.findByRoleName(AppRole.ROLE_USER)
                              .orElseThrow(()->new RuntimeException("role is not provide"));

              roles.add(userRole);

          }else {

              strRole.forEach(role->{
                  switch (role){
                      case "admin":
                          Role userAdmin=roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                  .orElseThrow(()->new RuntimeException("role is not provide admin"));
                          roles.add(userAdmin);
                          break;

                      case "seller":
                          Role userselller=roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                  .orElseThrow(()->new RuntimeException("role is not provide selller"));
                          roles.add(userselller);
                          break;
                      default:
                          Role userrole=roleRepository.findByRoleName(AppRole.ROLE_USER)
                                  .orElseThrow(()->new RuntimeException("role is not provide userRole"));
                          roles.add(userrole);
                          break;


                  }


              });


          }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseMessage("resgister is seccussufull"));

    }



    @GetMapping("/username")
    public String getUsername(Authentication authentication){

        if(authentication != null){
            return authentication.getName();
        }else {
            return "null";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication authentication){

        UserDetailsImp userDetails= (UserDetailsImp) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        LoginResponse response=new
                LoginResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().body(response);


    }


    @PostMapping("/logout")
    public ResponseEntity<?>logout(){
       ResponseCookie cookie= jwUtils.cleanCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new ResponseMessage("you are logout"));

    }

}

