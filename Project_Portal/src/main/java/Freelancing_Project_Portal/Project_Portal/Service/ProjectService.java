package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProjectService {
    Page<Project> getAllProjects(int page , int size);

    Project getProjectById(Long ProjectId);

    Page<Project> getProjectByStatus(String status , int page , int size);

    Page<Project> getProjectsByFacultyId(Long FacultyId , int page , int size);


}
