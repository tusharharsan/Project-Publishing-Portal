package Freelancing_Project_Portal.Project_Portal.Controllers;

import Freelancing_Project_Portal.Project_Portal.Config.JwtProvider;
import Freelancing_Project_Portal.Project_Portal.Entity.Application;
import Freelancing_Project_Portal.Project_Portal.Entity.FeedBack;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtProvider jwtProvider;


    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getStudentProfile(@RequestHeader ("Authorization") String tokne){
        String email = jwtProvider.getEmailfromToken(tokne);
        UserEntity student = studentService.getStudetnProfile(email);
        return new ResponseEntity<>(student , HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserEntity> updateStudentProfile(@RequestHeader ("Authorization") String token , @RequestBody  UserEntity updates){
        String email = jwtProvider.getEmailfromToken(token);
        UserEntity updatedStudent = studentService.updateStudentProfile(email , Map.of(
                "name" , updates.getName(),
                "about" , updates.getAbout()
        ));
        return new ResponseEntity<>(updatedStudent , HttpStatus.OK);
    }

    @PostMapping("/resume")
    public ResponseEntity<String> uploadeResume(@RequestHeader ("Authorization") String token , @RequestParam("file")MultipartFile file){
        try {
            String email = jwtProvider.getEmailfromToken(token);
            studentService.uploadResume(email , file);
            return new ResponseEntity<>("Resume uploaded successfully" , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/resume")
    public ResponseEntity<byte[]> getStudentPorfile(@RequestHeader("Authorization") String token){
        String email = jwtProvider.getEmailfromToken(token);
        byte[] resume = studentService.getResume(email);
        String fileName = studentService.getResumeFileName(email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(resume , headers , HttpStatus.OK);
    }



    @PostMapping("apply/{projectId}")
    public ResponseEntity<String> applyforPorject(@RequestHeader ("Authorization") String token , @PathVariable Long ProjectId){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            studentService.applyforProject(email , ProjectId);
            return new ResponseEntity<>("Applied for project successfully" , HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getStudentPplication(@RequestHeader("Authorization") String token){
        String email = jwtProvider.getEmailfromToken(token);
        List<Application> applications = studentService.getStudentApplications(email);
        return ResponseEntity.ok(applications);
    }

    @PostMapping("feedback/project/{projectId}")
    public ResponseEntity<String> provideFeedbacckonProject(@RequestHeader("Authorization") String token , @PathVariable Long ProjectId
    ,@RequestParam String comments , @RequestParam int rating){
        try{
            String mail = jwtProvider.getEmailfromToken(token);
            studentService.provideFeedbackonProject(mail , ProjectId , comments , rating);
            return new ResponseEntity<>("Feedback submitted successfully" , HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("feedback/faculty/{facultyId}")
    public ResponseEntity<String> provideFeedbackonFaculty(@RequestHeader("Authorization") String token , @PathVariable Long facultyId
            ,@RequestParam String comments , @RequestParam int rating){
        try{
            String mail = jwtProvider.getEmailfromToken(token);
            studentService.provideFeedbackonFaculty(mail , facultyId , comments , rating);
            return new ResponseEntity<>("Feedback submitted successfully" , HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedBack>> getStudentFeedbacks(@RequestHeader("Authorization") String token){
        String email = jwtProvider.getEmailfromToken(token);
        List<FeedBack> feedBackList = studentService.getStudentFeedbacks(email);
        return ResponseEntity.ok(feedBackList);
    }



}
