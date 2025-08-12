package lk.ijse.skillworker_backend.entity;

import jakarta.persistence.*;
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

    private String city; // optional, for more specific location

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<WorkerLocation> workerLocations = new ArrayList<>();

}
