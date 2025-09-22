package lk.ijse.skillworker_backend.controller;


import com.cloudinary.Cloudinary;
import lk.ijse.skillworker_backend.dto.request.WorkerRequestDTO;
import lk.ijse.skillworker_backend.dto.request.WorkerUpdateDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.WorkerProfileResponseDTO;
import lk.ijse.skillworker_backend.dto.response.WorkerResponseDTO;
import lk.ijse.skillworker_backend.service.CloudinaryService;
import lk.ijse.skillworker_backend.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/worker")
@CrossOrigin
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> registerWorker(@RequestBody WorkerRequestDTO workerRequestDTO) {
       return new ResponseEntity<>(new APIResponse<>(
               201 ,
               "Worker Successfully Registered !" ,
               workerService.registerWorker(workerRequestDTO)),
               HttpStatus.CREATED
       );
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @GetMapping("/getall")
    public ResponseEntity<APIResponse<List<WorkerResponseDTO>>> getAllWorkers(){
        List<WorkerResponseDTO> allWorkers = workerService.getAllWorkers();
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Success",
                allWorkers)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @PatchMapping("/status/{id}")
    public ResponseEntity<APIResponse<String>> changeStatus(@PathVariable Long id){
        workerService.changeStatus(id);
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Status Changed Successfully",
                null)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @GetMapping("/getworker/{id}")
    public ResponseEntity<APIResponse<WorkerResponseDTO>> getWorkerById(@PathVariable Long id){
        WorkerResponseDTO worker = workerService.getWorkerById(id);
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Success",
                worker)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @PutMapping("/update/{workerId}")
    public ResponseEntity<APIResponse<WorkerResponseDTO>> updateWorker(@PathVariable Long workerId , @RequestBody WorkerUpdateDTO dto){
        System.out.println("UPdate DTO :  "+ dto.toString());
        WorkerResponseDTO updated = workerService.updateWorker(workerId, dto);
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Worker updated successfully",
                updated
        ));
    }



    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @GetMapping("/profile/{id}")
    public ResponseEntity<APIResponse<WorkerProfileResponseDTO>> getWorkerProfileById(@PathVariable Long id){
        WorkerProfileResponseDTO worker = workerService.getWorkerProfileById(id);
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Success",
                worker)
        );
    }

    @GetMapping("/top-rated")
    public ResponseEntity<APIResponse<List<WorkerResponseDTO>>> getTop3WorkersByRating(){
        List<WorkerResponseDTO> topWorkers = workerService.getTop3WorkersByRating();
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Top 3 workers retrieved successfully",
                topWorkers)
        );
    }

}
