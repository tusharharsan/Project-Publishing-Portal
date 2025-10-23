package Freelancing_Project_Portal.Project_Portal.Controllers;

import Freelancing_Project_Portal.Project_Portal.Config.JwtProvider;
import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Service.ProjectService;
import Freelancing_Project_Portal.Project_Portal.Service.SearchService;
import Freelancing_Project_Portal.Project_Portal.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/all")
    public ResponseEntity<Map<String,Object>> getAllProjects(@RequestHeader ("Authorization") String token,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity student = studentService.getStudetnProfile(email);
            if(student == null){
                return ResponseEntity.status(403).build();
            }
            Page<Project> projectPage = projectService.getAllProjects( page,  size);
            Map<String, Object> response = new HashMap<>();
            response.put("projects", projectPage.getContent());
            response.put("currentPage", projectPage.getNumber());
            response.put("totalItems", projectPage.getTotalElements());
            response.put("totalPages", projectPage.getTotalPages());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    // Get a single project by id
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@RequestHeader ("Authorization") String token,
                                                   @PathVariable Long projectId){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity student = studentService.getStudetnProfile(email);
            if(student == null){
                return ResponseEntity.status(403).build();
            }
            Project project = projectService.getProjectById(projectId);
            return ResponseEntity.ok(project);
        }catch (NoSuchElementException nse){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    // Get projects filtered by status (e.g., OPEN, CLOSED)
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getProjectsByStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String email = jwtProvider.getEmailfromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<Project> projectPage = projectService.getProjectByStatus(status, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("projects", projectPage.getContent());
        response.put("currentPage", projectPage.getNumber());
        response.put("totalItems", projectPage.getTotalElements());
        response.put("totalPages", projectPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Get projects posted by a specific faculty
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<Map<String,Object>> getProjectsByFaculty(@RequestHeader ("Authorization") String token,
                                                              @PathVariable Long facultyId,
                                                                   @RequestParam (defaultValue = "0") int page,
                                                                   @RequestParam (defaultValue = "10") int size){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity student = studentService.getStudetnProfile(email);
            if(student == null){
                return ResponseEntity.status(403).build();
            }
            Page<Project> projectPage = projectService.getProjectsByFacultyId(facultyId , page , size);
            Map<String, Object> response = new HashMap<>();
            response.put("projects", projectPage.getContent());
            response.put("currentPage", projectPage.getNumber());
            response.put("totalItems", projectPage.getTotalElements());
            response.put("totalPages", projectPage.getTotalPages());

            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Map<String , Object>>> searchProjects(@RequestHeader ("Authorization") String token,
                                                                     @RequestParam String Query){
        try{
            String email = jwtProvider.getEmailfromToken(token);
            UserEntity student = studentService.getStudetnProfile(email);
            if(student == null){
                return ResponseEntity.status(403).build();
            }

            List<Map<String , Object>> results = searchService.searchProject(Query);
            if(results.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(results);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }


}
