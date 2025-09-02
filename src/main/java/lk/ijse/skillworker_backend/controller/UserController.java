package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.UserRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.LocationResponseDTO;
import lk.ijse.skillworker_backend.dto.response.UserResponseDTO;
import lk.ijse.skillworker_backend.service.AuthService;
import lk.ijse.skillworker_backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private  final AuthService authService;

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @GetMapping("/getuser/{id}")
    public ResponseEntity<APIResponse<UserResponseDTO>> getUser(@PathVariable Long id){
        return ResponseEntity.ok(new APIResponse<>(200,"User fetch Sucessfully !" ,authService.getUser(id)));
    }

    @PutMapping("/update/{id}")
    public  ResponseEntity<APIResponse<UserResponseDTO>> updateUser(@PathVariable Long id , @RequestBody UserRequestDTO userDTO){
        return ResponseEntity.ok(new APIResponse<>(200,"User Updated Sucessfully !" ,authService.updateUser(id , userDTO)));
    }

}
