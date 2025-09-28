package Freelancing_Project_Portal.Project_Portal.Repository;

import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProjectRespository extends JpaRepository<Project , Long> {
    List<Project> findByFaculty(UserEntity faculty);
}
