package Freelancing_Project_Portal.Project_Portal.Repository;

import Freelancing_Project_Portal.Project_Portal.Entity.ProjectEmneddings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectEmbeddingRepository extends JpaRepository<ProjectEmneddings , Long> {
    ProjectEmneddings findByProjectId(Long projectId);
}
