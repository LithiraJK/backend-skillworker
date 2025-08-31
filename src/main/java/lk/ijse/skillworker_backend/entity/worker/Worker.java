package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.auth.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "worker")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer experienceYears;

    @ElementCollection
    @CollectionTable(name = "worker_phone_numbers", joinColumns = @JoinColumn(name = "worker_id"))
    private List<String> phoneNumbers = new ArrayList<>();

    @Lob
    private String bio;

    @ElementCollection
    @CollectionTable(name = "worker_skills", joinColumns = @JoinColumn(name = "worker_id") )
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Lob
    private String profilePictureUrl;

    @Column(name = "profile_complete", nullable = false)
    private boolean profileComplete = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    //Relationships

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private User user;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default // To initialize the list to avoid NullPointerException
    private List<WorkerLocation> workerLocations = new ArrayList<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<WorkerCategory> workerCategories = new ArrayList<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ad> ads = new ArrayList<>();
}