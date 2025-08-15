package lk.ijse.skillworker_backend.entity.worker;

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
