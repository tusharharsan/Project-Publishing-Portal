package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.*;
import Freelancing_Project_Portal.Project_Portal.Repository.ApplicationRepository;
import Freelancing_Project_Portal.Project_Portal.Repository.FeedBackRepository;
import Freelancing_Project_Portal.Project_Portal.Repository.ProjectRespository;
import Freelancing_Project_Portal.Project_Portal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImplemetation implements StudentService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRespository projectRespository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Override
    public UserEntity getStudetnProfile(String mail) {
        UserEntity student = userRepository.findByEmail(mail);
        if(student==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Student not found with email: "+mail);
        }

        if(student.getRole() != USER_ROLES.ROLE_STUDENT){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "User with email: "+mail+" is not a student");
        }

        return student;
    }

    @Override
    public UserEntity updateStudentProfile(String mail, Map<String, String> updates) {
        UserEntity student  = getStudetnProfile(mail);
        if(updates.containsKey("about")){
            student.setAbout(updates.get("about"));
        }

        if(updates.containsKey("name")){
            student.setName(updates.get("name"));
        }

        return userRepository.save(student);
    }

    @Override
    public void uploadResume(String mail, MultipartFile file) throws IOException {
        UserEntity student = getStudetnProfile(mail);

        student.setResume(file.getBytes());
        student.setResumeFileName(file.getOriginalFilename());

        userRepository.save(student);
    }

    @Override
    public byte[] getResume(String mail) {
        UserEntity student = getStudetnProfile(mail);
        if(student.getResume()==null || student.getResume().length==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Resume not found for student with email: "+mail);
        }
        return student.getResume();
    }

    @Override
    public String getResumeFileName(String email) {
        UserEntity student = getStudetnProfile(email);
        if(student.getResumeFileName()==null || student.getResumeFileName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Resume file name not found for student with email: "+email);
        }
        return student.getResumeFileName();
    }

    @Override
    public void applyforProject(String email, Long ProjectId) {
        UserEntity student = getStudetnProfile(email);

        Optional<Project> projectOpt = projectRespository.findById(ProjectId);
        if(!projectOpt.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Project not found with id: "+ProjectId);
        }
        Project project = projectOpt.get();
        Application exustingApplication = applicationRepository.findByStudentAndProject(student,project);
        if(exustingApplication !=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "You have already applied for this project");
        }
        Application application = new Application();
        application.setStudent(student);
        application.setProject(project);
        application.setStatus("APPLIED");
        application.setAppliedAt(LocalDate.now());

        applicationRepository.save(application);
    }

    @Override
    public List<Application> getStudentApplications(String email) {
        UserEntity student = getStudetnProfile(email);

        return applicationRepository.findByStudent(student);
    }

    @Override
    public void provideFeedbackonProject(String mail, Long projectId, String comments, int rating) {
        UserEntity student = getStudetnProfile(mail);
        Optional<Project> projectopt = projectRespository.findById(projectId);
        if(!projectopt.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Project not found with id: "+projectId);
        }
        Project project = projectopt.get();
        if(rating <1 || rating>5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Rating must be between 1 and 5");
        }
        FeedBack feedBack = new FeedBack();
        feedBack.setProject(project);
        feedBack.setStudent(student);
        feedBack.setComments(comments);
        feedBack.setRating(rating);
        feedBack.setGivenAt(LocalDate.now());
        feedBack.setFeedbackType(FEEDBACK_TYPE.PROJECT);

        feedBackRepository.save(feedBack);
    }

    @Override
    public void provideFeedbackonFaculty(String mail, Long facultyId, String comments, int rating) {
        UserEntity student = getStudetnProfile(mail);

        Optional<UserEntity> facultyopt = userRepository.findById(facultyId);
        if(!facultyopt.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Faculty not found with id: "+facultyId);
        }
        UserEntity faculty = facultyopt.get();

        if(faculty.getRole() != USER_ROLES.ROLE_FACULTY){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "User with id: "+facultyId+" is not a faculty");
        }

        if(rating <1 || rating>5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Rating must be between 1 and 5");
        }

        FeedBack feedBack = new FeedBack();
        feedBack.setStudent(student);
        feedBack.setComments(comments);
        feedBack.setRating(rating);
        feedBack.setGivenAt(LocalDate.now());
        feedBack.setFeedbackType(FEEDBACK_TYPE.FACULTY);

        feedBackRepository.save(feedBack);
    }

    @Override
    public List<FeedBack> getStudentFeedbacks(String mail) {
        UserEntity student = getStudetnProfile(mail);
        return feedBackRepository.findByStudent(student);
    }
}
