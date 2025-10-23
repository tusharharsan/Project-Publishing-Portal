package Freelancing_Project_Portal.Project_Portal.Controllers;

import Freelancing_Project_Portal.Project_Portal.Config.JwtProvider;
import Freelancing_Project_Portal.Project_Portal.Entity.Application;
import Freelancing_Project_Portal.Project_Portal.Entity.FeedBack;
import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getFacultyProfile(@RequestHeader ("Authorization") String token){
        String email = jwtProvider.getEmailfromToken(token);
        UserEntity faculty = facultyService.getFacultyByEmail(email);
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateFacultyProfile(@RequestHeader ("Authorization") String token , @RequestBody UserEntity user){
        String email = jwtProvider.getEmailfromToken(token);

        UserEntity faculty = facultyService.getFacultyByEmail(email);
        if(faculty == null){
            return ResponseEntity.notFound().build();
        }
        UserEntity updateedFaculty = facultyService.updateFaculty(faculty.getId(), user);

        return ResponseEntity.ok(updateedFaculty);
    }

    @PostMapping("/createProject")
    public ResponseEntity<Project> createPorject(@RequestHeader("Authorization") String token , @RequestBody Project project){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity faculty = facultyService.getFacultyByEmail(email);
            Project createdproject = facultyService.createPorject(faculty.getId() , project);

            return new ResponseEntity<>(createdproject , HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<Project> UpdateProject(@RequestHeader("Authorization") String token , @PathVariable long projectId , @RequestBody Project updates){
        String email = jwtProvider.getEmailfromToken(token);
        UserEntity faculty = facultyService.getFacultyByEmail(email);
        if(faculty==null){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }

        Project newproject =facultyService.UpdateProject(projectId, updates);

        return new ResponseEntity<>(newproject , HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deletePorject(@RequestHeader("Authorization") String token , @PathVariable long projectId){
        try {
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity user = facultyService.getFacultyByEmail(email);
            if(user==null){
                return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
            }
            facultyService.deleteProject(projectId);
            return new ResponseEntity<>("Deleted the project" , HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long ProjectId){
        try{
            Project project = facultyService.getprojectbyId(ProjectId);
            if(project==null){
                return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(project , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getPorjectsByFaculty(@RequestHeader("Authoration") String token){
        String email = jwtProvider.getEmailfromToken(token);
        UserEntity faculty = facultyService.getFacultyByEmail(email);

        if(faculty==null){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }

        List<Project> projects = facultyService.getProjectsByFaculty(faculty.getId());

        return new ResponseEntity<>(projects , HttpStatus.OK);
    }

    @GetMapping("/feedbacks/project")
    public ResponseEntity<List<FeedBack>> getFeedbackforProjectId(@RequestHeader("Authorization") String token , @RequestParam Long ProjectId){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity faculty = facultyService.getFacultyByEmail(email);
            if(faculty==null){
                return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
            }
            List<FeedBack> feedBackList = facultyService.getFeedbackforPorjectId(ProjectId);
            return new ResponseEntity<>(feedBackList , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("feedbacks/faculty")
    public ResponseEntity<List<FeedBack>> getFeedbackforFacultyId(@RequestHeader("Authorization") String token){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity faculty = facultyService.getFacultyByEmail(email);
            if(faculty==null){
                return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
            }
            List<FeedBack> feedBackList = facultyService.getFeedbackforFacultyId(faculty.getId());
            return new ResponseEntity<>(feedBackList , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/applications/project/{projectId}")
    public ResponseEntity<List<Application>> getApplicationsForProject(
            @RequestHeader("Authorization") String token,
            @PathVariable long projectId) {
        try {
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity faculty = facultyService.getFacultyByEmail(email);
            if (faculty == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            List<Application> applications = facultyService.getApplicationsforProjectId(projectId);
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplications(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity faculty = facultyService.getFacultyByEmail(email);
            if (faculty == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            List<Application> applications = facultyService.getAllApplications(faculty.getId());
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable long applicationId,
            @RequestParam String status) {
        try {
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity faculty = facultyService.getFacultyByEmail(email);
            if (faculty == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            Application updatedApplication = facultyService.updateApplicationStatus(applicationId, status);
            return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    


}
