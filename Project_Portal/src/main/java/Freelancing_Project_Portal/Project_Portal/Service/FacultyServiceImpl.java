package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.*;
import Freelancing_Project_Portal.Project_Portal.Repository.ApplicationRepository;
import Freelancing_Project_Portal.Project_Portal.Repository.FeedBackRepository;
import Freelancing_Project_Portal.Project_Portal.Repository.ProjectRespository;
import Freelancing_Project_Portal.Project_Portal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.NoSuchElementException;

public class FacultyServiceImpl implements FacultyService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRespository projectRespository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public UserEntity getFacultyById(long id) {
        UserEntity faculty = userRepository.findById(id);
        if(faculty == null){
            return null;
        }
        return faculty;
    }

    @Override
    public UserEntity getFacultyByEmail(String email) {
        UserEntity faculty = userRepository.findByEmail(email);
        if(faculty == null){
            return null;
        }
        return faculty;
    }

    @Override
    public UserEntity updateFaculty(long id, UserEntity facultyDetails) {
        UserEntity faculty = getFacultyById(id);
        if(faculty == null){
            return null;
        }
        if (facultyDetails.getName() != null) {
            faculty.setName(facultyDetails.getName());
        }

        if (facultyDetails.getEmail() != null) {
            faculty.setEmail(facultyDetails.getEmail());
        }

        if (facultyDetails.getAbout() != null) {
            faculty.setAbout(facultyDetails.getAbout());
        }

        if (facultyDetails.getPassword() != null && !facultyDetails.getPassword().isEmpty()) {
            faculty.setPassword(facultyDetails.getPassword());
        }

        return userRepository.save(faculty);
    }

    @Override
    public Project createPorject(long FacultyId, Project projectDetails) {
        UserEntity faculty = getFacultyById(FacultyId);
        if(faculty==null){
            throw new RuntimeException("Faculty not found with id: "+FacultyId);
        }
        projectDetails.setFaculty(faculty);
        Project project = projectRespository.save(projectDetails);

        return project;
    }

    @Override
    public Project UpdateProject(long projectId, Project projectDetails) {
        Project existingProject = getprojectbyId(projectId);

        if (projectDetails.getTitle() != null) {
            existingProject.setTitle(projectDetails.getTitle());
        }

        if (projectDetails.getSkillsRequired() != null) {
            existingProject.setSkillsRequired(projectDetails.getSkillsRequired());
        }

        if (projectDetails.getStatus() != null) {
            existingProject.setStatus(projectDetails.getStatus());
        }

        if (projectDetails.getDeadline() != null) {
            existingProject.setDeadline(projectDetails.getDeadline());
        }

        return projectRespository.save(existingProject);
    }

    @Override
    public Void deleteProject(long projectId) {
        Project project = getprojectbyId(projectId);

        // Delete the project
        projectRespository.delete(project);

        return null;
    }

    @Override
    public Project getprojectbyId(long projectId) {
        return projectRespository.findById(projectId).orElseThrow(()->new NoSuchElementException("Project not found with id: "+projectId));
    }

    @Override
    public List<Project> getProjectsByFaculty(long facultyId) {
        UserEntity faculty = getFacultyById(facultyId);

        return projectRespository.findByFaculty(faculty);
    }

    @Override
    public List<FeedBack> getAllProjectFeebacks() {
        return feedBackRepository.findAll();
    }

    @Override
    public List<FeedBack> getFeedbackforPorjectId(long projectId) {
        Project project = getprojectbyId(projectId);

        // Return all feedback for this project
        return feedBackRepository.findByProject(project);
    }

    @Override
    public List<FeedBack> getFeedbackforFacultyId(long facultyId) {
        UserEntity faculty = getFacultyById(facultyId);

        // Return all feedback for this faculty
        return feedBackRepository.findByFaculty(faculty);
    }

    @Override
    public List<Application> getApplicationsforProjectId(long projectId) {
        return applicationRepository.findByProjectId(projectId);
    }

    @Override
    public List<Application> getAllApplications(long facultyId) {
      return null;
    }

    @Override
    public Application updateApplicationStatus(long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->new NoSuchElementException("Application not found with id: "+applicationId));

        application.setStatus(status);

        return applicationRepository.save(application);
    }
}
