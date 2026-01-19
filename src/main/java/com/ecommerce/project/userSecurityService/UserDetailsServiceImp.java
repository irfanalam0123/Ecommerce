package com.ecommerce.project.userSecurityService;

import com.ecommerce.project.CategoryModel.User;
import com.ecommerce.project.categoryRepository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user =userRepository.findByUserName(username)
               .orElseThrow(()->new UsernameNotFoundException("user does not exit" +username));

       return UserDetailsImp.build(user);

    }
}
