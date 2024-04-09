package rw.pacis.tekanaewallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.Customer;
import rw.pacis.tekanaewallet.model.CustomerWallet;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerDTO;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerWalletDTO;
import rw.pacis.tekanaewallet.model.enums.EGender;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;
import rw.pacis.tekanaewallet.services.ICustomerService;
import rw.pacis.tekanaewallet.utils.ApiResponse;
import rw.pacis.tekanaewallet.utils.Constants;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController extends BaseController{
    private final ICustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Customer>> register(@Valid @RequestBody RegisterCustomerDTO dto) throws BadRequestException {
        Customer customer = this.customerService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(customer, localize("responses.saveEntitySuccess"), HttpStatus.CREATED));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{id}/wallets/register")
    public ResponseEntity<ApiResponse<CustomerWallet>> registerCustomerWallet(
            @PathVariable(value = "id") UUID id,
            @Valid @RequestBody RegisterCustomerWalletDTO dto) throws ResourceNotFoundException, BadRequestException {
        CustomerWallet wallet = this.customerService.createWallet(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(wallet, localize("responses.saveEntitySuccess"), HttpStatus.CREATED));
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping("/myWallets")
    public ResponseEntity<ApiResponse<List<CustomerWallet>>> retrieveMyWallets() throws ResourceNotFoundException {
        List<CustomerWallet> wallets = this.customerService.getMyWallets();
        return ResponseEntity.ok(new ApiResponse<>( wallets,localize("responses.saveEntitySuccess"), HttpStatus.CREATED));
    }

    @Override
    protected String getEntityName() {
        return "Customer";
    }
}
