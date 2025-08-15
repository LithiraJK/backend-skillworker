package lk.ijse.skillworker_backend.entity.worker;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class WorkerCategoryID {
    private Long worker_id;
    private Long category_id;


}
