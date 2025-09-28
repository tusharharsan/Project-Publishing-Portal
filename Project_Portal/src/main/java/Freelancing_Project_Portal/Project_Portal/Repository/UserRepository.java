package Freelancing_Project_Portal.Project_Portal.Repository;


import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findById(long id);
}
