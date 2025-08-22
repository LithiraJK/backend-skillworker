package lk.ijse.skillworker_backend.entity.location;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.worker.WorkerLocation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    @Enumerated(EnumType.STRING)
    private District district;

    @Column(nullable = false)
    private boolean isActive = true;

    //Relationships
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkerLocation> workerLocations = new ArrayList<>();

}
