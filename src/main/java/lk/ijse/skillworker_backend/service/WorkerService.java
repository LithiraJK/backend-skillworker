package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.WorkerRequestDTO;
import lk.ijse.skillworker_backend.dto.response.WorkerResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkerService {
    String registerWorker(WorkerRequestDTO workerRequestDTO);

    List<WorkerResponseDTO> getAllWorkers();

    void changeStatus(Long id);

}
