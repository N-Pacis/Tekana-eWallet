package rw.pacis.tekanaewallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.pacis.tekanaewallet.model.Transaction;
import rw.pacis.tekanaewallet.model.dtos.response.GetMyTransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE (:startDate IS NULL OR t.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR t.createdAt <= :endDate) " +
            "AND (:fromAmount IS NULL OR t.amount >= :fromAmount) " +
            "AND (:toAmount IS NULL OR t.amount <= :toAmount) " +
            "AND ((:customerId IS NULL) OR " +
            "     (t.senderWallet.customer.id = :customerId) OR " +
            "     (t.receiverWallet.customer.id = :customerId)) ")
    Page<Transaction> searchAll(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("fromAmount") BigDecimal fromAmount,
                                @Param("toAmount") BigDecimal toAmount,
                                @Param("customerId") UUID customerId,
                                Pageable pageable);

    @Query("SELECT NEW rw.pacis.tekanaewallet.model.dtos.response.GetMyTransactionDTO(" +
            "t.id, " +
            "CASE WHEN t.senderWallet.customer.id = :customerId THEN 'TRANSFER' " +
            "     ELSE 'RECEIVE' END," +
            "CONCAT(t.receiverWallet.customer.firstName, ' ', t.receiverWallet.customer.lastName) , " +
            "t.receiverWallet.id, " +
            "CONCAT(t.senderWallet.customer.firstName, ' ', t.senderWallet.customer.lastName) , " +
            "t.senderWallet.id, " +
            "t.amount, " +
            "t.createdAt)" +
            "FROM Transaction t " +
            "WHERE (:startDate IS NULL OR t.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR t.createdAt <= :endDate) " +
            "AND (:fromAmount IS NULL OR t.amount >= :fromAmount) " +
            "AND (:toAmount IS NULL OR t.amount <= :toAmount)")
    Page<GetMyTransactionDTO> getMyTransactions(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("fromAmount") BigDecimal fromAmount,
                                                @Param("toAmount") BigDecimal toAmount,
                                                @Param("customerId") UUID customerId,
                                                Pageable pageable);
}
