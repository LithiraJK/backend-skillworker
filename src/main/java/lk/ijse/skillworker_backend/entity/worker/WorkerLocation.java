package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.*;
import lk.ijse.skillworker_backend.entity.location.Location;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "worker_location")
public class WorkerLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "location_id" , nullable = false)
    private Location location;

}
