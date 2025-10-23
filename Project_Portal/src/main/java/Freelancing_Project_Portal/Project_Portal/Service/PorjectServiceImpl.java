package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Repository.ProjectRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PorjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRespository projectRespository;



    @Override
    public Page<Project> getAllProjects(int page , int size) {
        Pageable pageable = PageRequest.of(page,size , Sort.by("id").descending());
        return projectRespository.findAll(pageable);

    }

    @Override
    public Project getProjectById(Long ProjectId) {
        return projectRespository.findById(ProjectId).orElse(null);
    }

    @Override
    public Page<Project> getProjectByStatus(String status , int page , int size) {
        Pageable pageable = PageRequest.of(page,size , Sort.by("id").descending());
        return projectRespository.findByStatus(status,pageable);
    }

    @Override
    public Page<Project> getProjectsByFacultyId(Long FacultyId , int page , int size) {
        Pageable pageable = PageRequest.of(page,size ,Sort.by("id").descending());
        return projectRespository.findByFacultyId(FacultyId , pageable);
    }
}
