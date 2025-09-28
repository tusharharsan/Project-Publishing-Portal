package Freelancing_Project_Portal.Project_Portal.Entity;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String skillsRequired;
    private String status;
    private LocalDate deadline;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private UserEntity faculty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public UserEntity getFaculty() {
        return faculty;
    }

    public void setFaculty(UserEntity faculty) {
        this.faculty = faculty;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }

    public ProjectEmneddings getProjectEmneddings() {
        return projectEmneddings;
    }

    public void setProjectEmneddings(ProjectEmneddings projectEmneddings) {
        this.projectEmneddings = projectEmneddings;
    }

    public List<FeedBack> getFeedBackList() {
        return feedBackList;
    }

    public void setFeedBackList(List<FeedBack> feedBackList) {
        this.feedBackList = feedBackList;
    }

    @OneToMany(mappedBy = "project")
    private List<Application> applications;

    @OneToMany(mappedBy="project")
    private List<Task> task;

    @OneToOne(mappedBy = "project" , cascade = CascadeType.ALL)
    private ProjectEmneddings projectEmneddings;

    @OneToMany(mappedBy = "project")
    private List<FeedBack> feedBackList;



}
