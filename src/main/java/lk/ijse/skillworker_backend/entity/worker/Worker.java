package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.*;
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
    private Long worker_id;  // same as user_id for 1-to-1 relation

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private User user;

    private Integer experience_years;

    private String phone;

    private String bio;

    @ElementCollection
    @CollectionTable(name = "worker_skills", joinColumns = @JoinColumn(name = "worker_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private boolean isProfileComplete;  // optional, can be calculated instead

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkerLocation> workerLocations = new ArrayList<>();
}
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private Long worker_id;  // same as user_id for 1-to-1 relation
//
//@OneToOne(cascade = CascadeType.ALL)
//@MapsId
//private User user;
//
//private String experience;
//
//private String phone;
//
//private String bio;
//
//private String skills;
//
//private List<Category> categories ;
//
//private List<WorkerLocation> working_area;
//
//private String profilePictureUrl;
//
//@Enumerated(EnumType.STRING)
//private SubscriptionPlan subscriptionPlan;
//
//private boolean isProfileComplete;