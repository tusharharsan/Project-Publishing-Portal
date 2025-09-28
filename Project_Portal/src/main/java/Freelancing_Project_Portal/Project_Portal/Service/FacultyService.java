package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.Application;
import Freelancing_Project_Portal.Project_Portal.Entity.FeedBack;
import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;

import java.util.List;

public interface FacultyService {
    UserEntity getFacultyById(long id);

    UserEntity getFacultyByEmail(String email);

    UserEntity updateFaculty(long id, UserEntity facultyDetails);

    Project createPorject(long FacultyId , Project projectDetails);

    Project UpdateProject(long projectId , Project projectDetails);

    Void deleteProject(long projectId);

    Project getprojectbyId(long projectId);

    List<Project> getProjectsByFaculty(long facultyId);

    List<FeedBack> getAllProjectFeebacks();

    List<FeedBack> getFeedbackforPorjectId(long projectId);

    List<FeedBack> getFeedbackforFacultyId(long facultyId);

    List<Application> getApplicationsforProjectId(long projectId);

    List<Application> getAllApplications(long facultyId);

    Application updateApplicationStatus(long applicationId,String status);



}
