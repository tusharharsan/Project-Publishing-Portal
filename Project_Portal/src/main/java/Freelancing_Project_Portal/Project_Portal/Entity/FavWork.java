package Freelancing_Project_Portal.Project_Portal.Entity;

import jakarta.persistence.*;

import javax.crypto.SecretKey;

@Entity
@Table(name = "fav_work")
public class FavWork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
