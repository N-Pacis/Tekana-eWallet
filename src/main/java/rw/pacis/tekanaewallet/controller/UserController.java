package rw.pacis.tekanaewallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.UserAccountAudit;
import rw.pacis.tekanaewallet.model.UserAccountLoginHistory;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterUserDTO;
import rw.pacis.tekanaewallet.model.dtos.request.SetPasswordDTO;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;
import rw.pacis.tekanaewallet.services.ICustomerService;
import rw.pacis.tekanaewallet.services.IUserService;
import rw.pacis.tekanaewallet.utils.ApiResponse;
import rw.pacis.tekanaewallet.utils.Constants;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController{
    private final IUserService userService;

    private final ICustomerService customerService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/search")
    public ResponseEntity<ApiResponse<Page<?>>> searchAll(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "q",required = false,defaultValue = "") String query,
            @RequestParam(value = "role", required = false) ERole role,
            @RequestParam(value = "status", required = false) EUserStatus status,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit) throws ResourceNotFoundException {

        Pageable pageable = PageRequest.of(page - 1, limit);

        if (role.equals(ERole.CUSTOMER))
            return ResponseEntity.ok(
                    new ApiResponse<>(customerService.searchAll(query, status, pageable), localize("responses.getListSuccess"), HttpStatus.OK)
            );

        return ResponseEntity.ok(
                new ApiResponse<>(userService.searchAll(query, status, pageable), localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterUserDTO dto) throws BadRequestException {
        this.userService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(localize("responses.saveEntitySuccess"), HttpStatus.CREATED));

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(path="/{id}/activate")
    public ResponseEntity<ApiResponse<UserAccount>> approveById(@PathVariable(value = "id") UUID id) throws ResourceNotFoundException, BadRequestException {
        UserAccount user = this.userService.activate(id);
        return ResponseEntity.ok(new ApiResponse<>(user, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(path="/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserAccount>> deactivateById(@PathVariable(value = "id") UUID id) throws ResourceNotFoundException, BadRequestException, BadRequestException {
        UserAccount user = this.userService.deactivate(id);
        return ResponseEntity.ok(new ApiResponse<>(user, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(path="/{id}/resetPassword")
    public ResponseEntity<ApiResponse<UserAccount>> resetPassword(@PathVariable(value = "id") UUID id, @Valid @RequestBody SetPasswordDTO dto) throws ResourceNotFoundException, BadRequestException {
        UserAccount userAccount = this.userService.resetPassword(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(userAccount, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/{id}/audits")
    public ResponseEntity<ApiResponse<Page<UserAccountAudit>>> getUserAudit(
            @PathVariable(value = "id") UUID id,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
            ) throws ResourceNotFoundException {

        Sort sort = Sort.by(Sort.Direction.DESC, "doneAt");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<UserAccountAudit> userAudits = this.userService.getAuditByUser(id, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(userAudits, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/{id}/loginHistory")
    public ResponseEntity<ApiResponse<Page<UserAccountLoginHistory>>> getUserLoginHistory(
            @PathVariable(value = "id") UUID id,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) throws ResourceNotFoundException {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<UserAccountLoginHistory> loginHistory = this.userService.getUserLoginHistory(id, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(loginHistory, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @Override
    protected String getEntityName() {
        return "User";
    }
}
