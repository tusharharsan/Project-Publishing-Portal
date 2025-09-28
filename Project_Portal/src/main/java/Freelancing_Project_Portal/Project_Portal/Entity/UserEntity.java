package Freelancing_Project_Portal.Project_Portal.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String password;
    private USER_ROLES role = USER_ROLES.ROLE_STUDENT;// STUDENT / BUSINESS / FACULTY

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] resume;

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    @Column(columnDefinition = "TEXT")
    private String about;

    private String resumeFileName;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<FavWork> favWorks;

    @OneToMany(mappedBy = "student")
    private List<Application> applications;

    @OneToMany(mappedBy= "faculty")
    private List<Project> projectsPosted;

    @OneToMany(mappedBy="student")
    private List<FeedBack> feedBackList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public USER_ROLES getRole() {
        return role;
    }

    public void setRole(USER_ROLES role) {
        this.role = role;
    }

    public List<FavWork> getFavWorks() {
        return favWorks;
    }

    public void setFavWorks(List<FavWork> favWorks) {
        this.favWorks = favWorks;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Project> getProjectsPosted() {
        return projectsPosted;
    }

    public void setProjectsPosted(List<Project> projectsPosted) {
        this.projectsPosted = projectsPosted;
    }

    public List<FeedBack> getFeedBackList() {
        return feedBackList;
    }

    public void setFeedBackList(List<FeedBack> feedBackList) {
        this.feedBackList = feedBackList;
    }
}
