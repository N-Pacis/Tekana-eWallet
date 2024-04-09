package rw.pacis.tekanaewallet.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.exceptions.ResourceNotFoundException;
import rw.pacis.tekanaewallet.model.Customer;
import rw.pacis.tekanaewallet.model.CustomerWallet;
import rw.pacis.tekanaewallet.model.Transaction;
import rw.pacis.tekanaewallet.model.dtos.request.CreateTransactionDTO;
import rw.pacis.tekanaewallet.model.dtos.response.GetMyTransactionDTO;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;
import rw.pacis.tekanaewallet.repository.ICustomerWalletRepository;
import rw.pacis.tekanaewallet.repository.ITransactionRepository;
import rw.pacis.tekanaewallet.services.ICustomerService;
import rw.pacis.tekanaewallet.services.ITransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;

    private final ICustomerService customerService;

    private final ICustomerWalletRepository customerWalletRepository;

    public TransactionServiceImpl(ITransactionRepository transactionRepository, ICustomerService customerService, ICustomerWalletRepository customerWalletRepository) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
        this.customerWalletRepository = customerWalletRepository;
    }

    @Override
    @Transactional
    public void create(CreateTransactionDTO dto) throws ResourceNotFoundException, BadRequestException {
        if(dto.getSenderWalletId().equals(dto.getReceiverWalletId())) throw new BadRequestException("exceptions.badRequest.invalidAction");

        Customer customer = customerService.getLoggedInCustomer();

        CustomerWallet senderWallet = customerService.getWalletById(dto.getSenderWalletId());
        if(!senderWallet.getCustomer().getId().equals(customer.getId())) throw new BadRequestException("exceptions.badRequest.invalidAction");
        if(senderWallet.getBalance().subtract(dto.getAmount()).compareTo(BigDecimal.ZERO) < 0) throw new BadRequestException("exceptions.badRequest.transaction.insufficientAmount");

        CustomerWallet receiverWallet = customerService.getWalletById(dto.getReceiverWalletId());
        if(!receiverWallet.getCustomer().getStatus().equals(EUserStatus.ACTIVE)) throw new BadRequestException("exceptions.badRequest.invalidAction");

        senderWallet.setBalance(senderWallet.getBalance().subtract(dto.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(dto.getAmount()));
        customerWalletRepository.saveAll(List.of(senderWallet, receiverWallet));

        Transaction transaction = new Transaction(senderWallet, receiverWallet, dto.getAmount());

        transactionRepository.save(transaction);
    }

    @Override
    public Page<Transaction> getAllTransactions(LocalDate startDate, LocalDate endDate, BigDecimal fromAmount, BigDecimal toAmount, UUID customerId, Pageable pageable) throws BadRequestException {

        if(startDate == null && endDate == null){
            endDate = LocalDate.now();
            startDate = endDate.minusWeeks(1);
        } else if(startDate == null) {
            startDate = endDate.minusWeeks(1);
        } else if(endDate == null) {
            endDate = startDate.plusWeeks(1);
        }

        if (startDate.plusWeeks(1).isBefore(endDate)) throw new BadRequestException("exceptions.badRequest.transaction.invalidDateRange");
        if (startDate.isAfter(endDate))  throw new BadRequestException("exceptions.badRequest.transaction.invalidStartDate");

        if(fromAmount != null && toAmount != null){
            if (fromAmount.compareTo(toAmount) > 0)  throw new BadRequestException("exceptions.badRequest.transaction.invalidAmount");
        }

        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(23, 59, 59, 999_999_999);

        return transactionRepository.searchAll(startTime, endTime, fromAmount, toAmount, customerId, pageable);
    }

    @Override
    public Page<GetMyTransactionDTO> getMyTransactions(String walletId, LocalDate startDate, LocalDate endDate, BigDecimal fromAmount, BigDecimal toAmount,  Pageable pageable) throws ResourceNotFoundException, BadRequestException {

        CustomerWallet wallet = customerService.getWalletById(walletId);
        Customer customer = customerService.getLoggedInCustomer();

        if(!wallet.getCustomer().getId().equals(customer.getId())) throw new BadRequestException("exceptions.badRequest.invalidAction");

        if(startDate == null && endDate == null){
            endDate = LocalDate.now();
            startDate = endDate.minusWeeks(1);
        } else if(startDate == null) {
            startDate = endDate.minusWeeks(1);
        } else if(endDate == null) {
            endDate = startDate.plusWeeks(1);
        }

        if (startDate.plusWeeks(1).isBefore(endDate)) throw new BadRequestException("exceptions.badRequest.transaction.invalidDateRange");
        if (startDate.isAfter(endDate))  throw new BadRequestException("exceptions.badRequest.transaction.invalidStartDate");

        if(fromAmount != null && toAmount != null){
            if (fromAmount.compareTo(toAmount) > 0)  throw new BadRequestException("exceptions.badRequest.transaction.invalidAmount");
        }

        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(23, 59, 59, 999_999_999);

        return transactionRepository.getMyTransactions(walletId,fromAmount, toAmount, startTime, endTime, pageable);
    }
}
