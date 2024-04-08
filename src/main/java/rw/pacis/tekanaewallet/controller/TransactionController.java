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
import rw.pacis.tekanaewallet.model.Transaction;
import rw.pacis.tekanaewallet.model.dtos.request.CreateTransactionDTO;
import rw.pacis.tekanaewallet.model.dtos.response.GetMyTransactionDTO;
import rw.pacis.tekanaewallet.services.ITransactionService;
import rw.pacis.tekanaewallet.utils.ApiResponse;
import rw.pacis.tekanaewallet.utils.Constants;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController extends BaseController{
    private final ITransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/search")
    public ResponseEntity<ApiResponse<Page<Transaction>>> searchAll(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "fromAmount", required = false) BigDecimal fromAmount,
            @RequestParam(value = "toAmount", required = false) BigDecimal toAmount,
            @RequestParam(value = "customerId", required = false) UUID customerId,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit) throws BadRequestException {

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Transaction> transactions = this.transactionService.getAllTransactions(startDate, endDate, fromAmount, toAmount, customerId, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(transactions, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @GetMapping(value = "/myTransactions")
    public ResponseEntity<ApiResponse<Page<GetMyTransactionDTO>>> getMyTransactions(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "fromAmount", required = false) BigDecimal fromAmount,
            @RequestParam(value = "toAmount", required = false) BigDecimal toAmount,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit) throws BadRequestException, ResourceNotFoundException {

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<GetMyTransactionDTO> transactions = this.transactionService.getMyTransactions(startDate, endDate, fromAmount, toAmount, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(transactions, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> create(@Valid @RequestBody CreateTransactionDTO dto) throws BadRequestException, ResourceNotFoundException {
        this.transactionService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(localize("responses.saveEntitySuccess"), HttpStatus.CREATED));
    }

    @Override
    protected String getEntityName() {
        return "Transaction";
    }
}
