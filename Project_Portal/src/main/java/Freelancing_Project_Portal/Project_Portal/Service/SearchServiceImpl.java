package Freelancing_Project_Portal.Project_Portal.Service;

import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import Freelancing_Project_Portal.Project_Portal.Repository.ProjectRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class SearchServiceImpl implements SearchService{

    @Autowired
    private ProjectRespository projectRespository;



    @Override
    public List<Map<String, Object>> searchProject(String query) throws JsonProcessingException {
        List<Project> allPorject = projectRespository.findAll();

        List<String> documents  = new ArrayList<>();
        List<Project> projectsforRanking = new ArrayList<>();

        for(Project project : allPorject){
            StringBuilder sb = new StringBuilder();
            sb.append("Title: ").append(project.getTitle()).append(". ");

            if(project.getSkillsRequired() != null){
                sb.append("Skills Required: ").append(String.join(", ", project.getSkillsRequired())).append(". ");
            }

            if(project.getStatus() != null){
                sb.append("Status: ").append(project.getStatus()).append(". ");
            }
            UserEntity faculty = project.getFaculty();
            if(faculty != null){
                sb.append("Faculty: ").append(faculty.getName()).append(". ");
            }
            documents.add(sb.toString());
            projectsforRanking.add(project);
        }

        Map<String , Object> requestBody = new HashMap<>();

        requestBody.put("query",query);
        requestBody.put("documents" , documents);

        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(requestBody);

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/rank"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode()>=200 && response.statusCode()<300){
                Map<String , Object> apiresponse = mapper.readValue(response.body(), Map.class);

                return processSearchResult(apiresponse,projectsforRanking ,documents);
            }else{
                System.err.println("API call failed with status: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private List<Map<String, Object>> processSearchResult(Map<String, Object> apiresponse, List<Project> projects, List<String> documents) {
        List<Map<String,Object>> results = new ArrayList<>();

        List<Map<String  , Object>> apiresult = (List<Map<String,Object>>) apiresponse.get("results");

        if(apiresult != null){
            for(Map<String , Object> result : apiresult){
                String documentText = (String) result.get("document");
                Double score = (Double) result.get("score");

                int documentindex = documents.indexOf(documentText);

                if(documentindex != -1 && documentindex<projects.size()){
                    Project project = projects.get(documentindex);
                    Map<String , Object> projectData = new HashMap<>();
                    projectData.put("id", project.getId());
                    projectData.put("title", project.getTitle());
                    projectData.put("description", project.getDescription());
                    projectData.put("skillsRequired", project.getSkillsRequired());
                    projectData.put("status", project.getStatus());
                    projectData.put("matchedDocument" , documentText);
                    if(project.getFaculty() != null){
                        Map<String,Object> facultyData = new HashMap<>();
                        facultyData.put("id" , project.getFaculty().getId());
                        facultyData.put("name" , project.getFaculty().getName());
                        projectData.put("faculty",facultyData);
                    }else{
                        projectData.put("facultyName", null);
                    }

                    projectData.put("score", score);

                    results.add(projectData);
                }
            }
        }

        return results;
    }
}
