package lk.ijse.skillworker_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_location")
public class WorkerLocation {

    @EmbeddedId
    private WorkerLocationID workerLocationId;

    @ManyToOne
    @MapsId("worker_id")
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne
    @MapsId("location_id")
    @JoinColumn(name = "location_id")
    private Location location;

}
