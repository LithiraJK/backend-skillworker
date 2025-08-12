package lk.ijse.skillworker_backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/worker")
@RequiredArgsConstructor
@CrossOrigin
public class WorkerController {

    @GetMapping("/get")
    public String getWorkerDetails() {
        // This method will return worker details
        return "Worker details will be implemented here.";
    }


}
