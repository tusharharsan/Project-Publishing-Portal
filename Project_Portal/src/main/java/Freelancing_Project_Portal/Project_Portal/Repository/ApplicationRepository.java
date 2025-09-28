package Freelancing_Project_Portal.Project_Portal.Repository;

import Freelancing_Project_Portal.Project_Portal.Entity.Application;
import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application , Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByProjectId(Long projectId);
    Application findByStudentAndProject(UserEntity student, Project project);

    List<Application> findByStudent(UserEntity student);
}
