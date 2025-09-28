package Freelancing_Project_Portal.Project_Portal.Repository;

import Freelancing_Project_Portal.Project_Portal.Entity.FeedBack;
import Freelancing_Project_Portal.Project_Portal.Entity.FEEDBACK_TYPE;
import Freelancing_Project_Portal.Project_Portal.Entity.Project;
import Freelancing_Project_Portal.Project_Portal.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
    List<FeedBack> findByStudent(UserEntity student);
    List<FeedBack> findByStudentAndFeedbackType(UserEntity student, FEEDBACK_TYPE feedbackType);
    List<FeedBack> findByProject(Project project);
    List<FeedBack> findByFaculty(UserEntity faculty);
}
