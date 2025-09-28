package Freelancing_Project_Portal.Project_Portal.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface SearchService {
    List<Map<String , Object>> searchProject(String query) throws JsonProcessingException;

}
