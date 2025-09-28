package Freelancing_Project_Portal.Project_Portal.Controllers;

import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;





}
