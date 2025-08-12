package lk.ijse.skillworker_backend.entity;

import jakarta.persistence.*;
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

    @OneToOne
    @MapsId
    private User user;

    private String fullName;

    private String phone;

    private String bio;

    private String skills;  // could be CSV or a separate skills table

    private String working_area;  // could be a separate Location entity

    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private boolean isProfileComplete;  // optional, can be calculated instead

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkerLocation> workerLocations = new ArrayList<>();
}
