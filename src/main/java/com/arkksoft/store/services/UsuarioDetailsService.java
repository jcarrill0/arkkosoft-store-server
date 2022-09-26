package com.arkksoft.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

import com.arkksoft.store.models.dao.UserDao;
import com.arkksoft.store.models.entity.User;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException  {
        User user = userDao.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found by email!"));

        return UsuarioDetails.build(user);
    }

    public User getById(Long id){
        return userDao.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Username with ID: " + id + " not found"));
    }
}
