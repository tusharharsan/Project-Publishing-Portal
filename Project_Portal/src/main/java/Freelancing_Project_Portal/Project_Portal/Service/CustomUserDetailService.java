package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.USER_ROLES;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user =userRepository.findByEmail(username);
        if(user ==null){
            throw new UsernameNotFoundException("User not found with email"+username);

        }
        USER_ROLES userRoles = user.getRole();
        List< GrantedAuthority> authorities =new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(userRoles.toString()));
        return new org.springframework.security.core.userdetails.User(user.getEmail() , user.getPassword() ,authorities);


    }
}
