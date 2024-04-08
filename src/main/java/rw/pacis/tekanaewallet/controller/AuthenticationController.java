package rw.pacis.tekanaewallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.dtos.request.UpdatePasswordDTO;
import rw.pacis.tekanaewallet.security.dtos.LoginRequest;
import rw.pacis.tekanaewallet.security.dtos.LoginResponseDTO;
import rw.pacis.tekanaewallet.services.IAuthenticationService;
import rw.pacis.tekanaewallet.services.IUserService;
import rw.pacis.tekanaewallet.utils.ApiResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {
    private final IAuthenticationService authenticationService;
    private final IUserService userService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> signin(
            @RequestBody LoginRequest request,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = "Device-Type", required = false) String deviceType) throws ResourceNotFoundException {
        return ResponseEntity.ok(new ApiResponse<>(authenticationService.signIn(request, userAgent, deviceType), localize("responses.getEntitySuccess"), HttpStatus.OK));
    }

    @PutMapping(path="/updatePassword")
    public ResponseEntity<ApiResponse<UserAccount>> changePassword(@Valid @RequestBody UpdatePasswordDTO dto) throws ResourceNotFoundException, BadRequestException {
        UserAccount userAccount = this.authenticationService.updatePassword(dto);
        return ResponseEntity.ok(new ApiResponse<>(userAccount, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PostMapping("/signOut")
    public ResponseEntity<ApiResponse<Object>> signOut() throws ResourceNotFoundException {
        authenticationService.signOut();
        return ResponseEntity.ok(new ApiResponse<>(localize("responses.success"), HttpStatus.OK));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<ApiResponse<UserAccount>> authUser() throws ResourceNotFoundException {
        return ResponseEntity.ok(new ApiResponse<>(userService.getLoggedInUser(), localize("responses.getEntitySuccess"), HttpStatus.OK));
    }

    @Override
    protected String getEntityName() {
        return "Auth";
    }
}
