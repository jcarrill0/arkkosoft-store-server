package com.arkksoft.store.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.arkksoft.store.dto.AuthDTO;
import com.arkksoft.store.dto.TokenDTO;
import com.arkksoft.store.models.dao.UserDao;
import com.arkksoft.store.models.entity.Role;
import com.arkksoft.store.models.entity.User;
import com.arkksoft.store.security.jwt.JwtUtils;

@Service
public class AuthService {
    @Autowired
    BCryptPasswordEncoder passEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtils jwtUtils;

    public TokenDTO signin(AuthDTO request) throws Exception {
        Authentication authentication = this.authenticate(request.getEmail(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UsuarioDetails userDetails = (UsuarioDetails) usuarioDetailsService.loadUserByUsername(request.getEmail());

        final String jwt = jwtUtils.generateToken(userDetails);

       List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return new TokenDTO(jwt,userDetails.getEmail(), roles);
    }

    public Map<String, Object> signup(AuthDTO request) {
        Map<String, Object> message = new HashMap<>();
        
        Optional<User> existsUser = userDao.findUserByEmail(request.getEmail());
        
        if(existsUser.isPresent()){
            throw new BadCredentialsException("Email ya existe!!!");
        }

        // Create new user's account
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_ADMIN);
        String password = passEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), password);
        user.setRoles(roles);
        userDao.save(user);

        message.put("message", "El usuario se creo correctamente!");

        return message;
    }

    @Transactional
    public Authentication authenticate(String email, String password) throws Exception {
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
           throw new DisabledException("USER_DISABLED: ", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS: ", e);
        }
        return authentication;
    }

    public void initUserSuperAdmin(){
        if(userDao.findUserByEmail("superadmin@superadmin.com").isEmpty()){
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ROLE_ADMIN);
            String passEncrypt = passEncoder.encode("abc123");

            User superAdminUser = new User("superadmin@superadmin.com", passEncrypt);
            superAdminUser.setRoles(roles);
            userDao.save(superAdminUser);
        } else {
            System.out.println("EXISTE EN LA BASE DE DATOS");
        }
    }
}
