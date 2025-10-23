package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.Application;
import Freelancing_Project_Portal.Project_Portal.Entity.FeedBack;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface StudentService {
    UserEntity getStudetnProfile(String mail);
    UserEntity updateStudentProfile(String mail, Map<String,String> updates);

    void uploadResume(String mail , MultipartFile file) throws IOException;

    byte[] getResume(String mail);

    String getResumeFileName(String email);

    void applyforProject(String email , Long ProjectId);
    List<Application> getStudentApplications(String email);

    void provideFeedbackonProject(String mail , Long projectId , String comments , int rating);

    void provideFeedbackonFaculty(String mail , Long facultyId , String comments , int rating);

    List<FeedBack> getStudentFeedbacks(String mail);
}
