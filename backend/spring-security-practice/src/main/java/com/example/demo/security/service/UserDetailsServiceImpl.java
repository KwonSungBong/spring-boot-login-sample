package com.example.demo.security.service;

import com.example.demo.model.User;
import com.example.demo.security.model.SpringSecurityUser;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.loadUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No User found with username '%s'.", username));
        } else {
            return new SpringSecurityUser(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    null,
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
            );
        }
    }

}
