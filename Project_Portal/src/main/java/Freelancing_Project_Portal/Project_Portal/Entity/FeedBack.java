package Freelancing_Project_Portal.Project_Portal.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "feedback")

public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String comments;
    private int rating; // 1–5
    private LocalDate givenAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getGivenAt() {
        return givenAt;
    }

    public void setGivenAt(LocalDate givenAt) {
        this.givenAt = givenAt;
    }

    public FEEDBACK_TYPE getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(FEEDBACK_TYPE feedbackType) {
        this.feedbackType = feedbackType;
    }

    public UserEntity getStudent() {
        return student;
    }

    public void setStudent(UserEntity student) {
        this.student = student;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public UserEntity getFaculty() {
        return faculty;
    }

    public void setFaculty(UserEntity faculty) {
        this.faculty = faculty;
    }

    private FEEDBACK_TYPE feedbackType;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private UserEntity student;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private UserEntity faculty;
}
