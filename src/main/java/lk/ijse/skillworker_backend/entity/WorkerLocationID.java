package lk.ijse.skillworker_backend.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class WorkerLocationID {
    private Long worker_id;
    private Long location_id;

}
