package rw.pacis.tekanaewallet.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.Transaction;
import rw.pacis.tekanaewallet.model.dtos.request.CreateTransactionDTO;
import rw.pacis.tekanaewallet.model.dtos.response.GetMyTransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface ITransactionService {

    void create(CreateTransactionDTO dto) throws ResourceNotFoundException, BadRequestException;

    Page<Transaction> getAllTransactions(LocalDate startDate, LocalDate endDate, BigDecimal fromAmount, BigDecimal toAmount, UUID customerId, Pageable pageable) throws BadRequestException;


    Page<GetMyTransactionDTO> getMyTransactions(String walletId, LocalDate startDate, LocalDate endDate, BigDecimal fromAmount, BigDecimal toAmount, Pageable pageable) throws ResourceNotFoundException, BadRequestException;
}
