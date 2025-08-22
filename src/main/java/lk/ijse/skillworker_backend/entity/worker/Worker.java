package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.entity.category.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "worker")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long worker_id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private User user;

    private Integer experience_years;

    @ElementCollection
    @CollectionTable(name = "worker_phone_numbers", joinColumns = @JoinColumn(name = "worker_id"))
    @Column(name = "phone_numbers")
    private List<String> phone_numbers = new ArrayList<>();

    @Lob
    private String bio;

    @ElementCollection
    @CollectionTable(name = "worker_skills", joinColumns = @JoinColumn(name = "worker_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Lob
    private String profilePictureUrl;

    @Column(name = "profile_complete", nullable = false)
    private boolean profileComplete = false;

    //Relationships

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkerLocation> workerLocations = new ArrayList<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkerCategory> workerCategories = new ArrayList<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Ad> ads = new ArrayList<>();
}