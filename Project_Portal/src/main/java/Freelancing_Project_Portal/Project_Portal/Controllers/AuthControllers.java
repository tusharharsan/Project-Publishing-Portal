package Freelancing_Project_Portal.Project_Portal.Controllers;

import Freelancing_Project_Portal.Project_Portal.Config.JwtProvider;
import Freelancing_Project_Portal.Project_Portal.Entity.USER_ROLES;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Repository.UserRepository;
import Freelancing_Project_Portal.Project_Portal.Request.LoginRequest;
import Freelancing_Project_Portal.Project_Portal.Response.AuthResponse;
import Freelancing_Project_Portal.Project_Portal.Service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.LabelUI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthControllers {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService CustomUserDetailService;

    @PostMapping("/signup")
    public ResponseEntity<?> sigup(@RequestBody UserEntity user){
       try {
            UserEntity isEmailExist =userRepository.findByEmail(user.getEmail());
            if(isEmailExist != null){
                return new ResponseEntity<>("Email is already used with another account" , HttpStatus.BAD_REQUEST);
            }

            UserEntity newuser =new UserEntity();
           newuser.setPassword(passwordEncoder.encode(user.getPassword()));
           newuser.setEmail(user.getEmail());
            newuser.setApplications(new ArrayList<>());
            newuser.setName(user.getName());
            newuser.setRole(user.getRole());
            newuser.setProjectsPosted(new ArrayList<>());
            newuser.setFeedBackList(new ArrayList<>());
            newuser.setFavWorks(new ArrayList<>());

            UserEntity saveduser = userRepository.save(newuser);

            Authentication authentication =new UsernamePasswordAuthenticationToken(user.getEmail() , user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

           AuthResponse authResponse = new AuthResponse();
           authResponse.setToken(token);
           authResponse.setMessage("Regitration Successful");
           authResponse.setRole(user.getRole());

            return new ResponseEntity<>(authResponse , HttpStatus.CREATED);


       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>("Something went wrong" , HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> singin(@RequestBody LoginRequest user){
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            Authentication authentication = authenticate(email,password);
            String jwt = jwtProvider.generateToken(authentication);
            String token = jwtProvider.generateToken(authentication);

            Collection<? extends GrantedAuthority > authorities = authentication.getAuthorities();

            String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();


            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setMessage("Login Successful");
            authResponse.setRole(USER_ROLES.valueOf(role));

            return new ResponseEntity<>(authResponse, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails =  CustomUserDetailService.loadUserByUsername(email);

        if(userDetails == null){
            throw new RuntimeException("User not found with email: "+email);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new RuntimeException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities());
    }


}
