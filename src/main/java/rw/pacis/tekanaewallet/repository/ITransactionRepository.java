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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE (CAST(:startDate AS DATE) IS NULL OR t.createdAt >= CAST(:startDate AS DATE)) " +
            "AND (CAST(:endDate AS DATE) IS NULL OR t.createdAt <= CAST(:endDate AS DATE)) " +
            "AND (:fromAmount IS NULL OR t.amount >= :fromAmount) " +
            "AND (:toAmount IS NULL OR t.amount <= :toAmount) " +
            "AND ((CAST(:customerId as uuid) IS NULL) OR " +
            "     (t.senderWallet.customer.id = CAST(:customerId as uuid)) OR " +
            "     (t.receiverWallet.customer.id = CAST(:customerId as uuid)))")
    Page<Transaction> searchAll(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                @Param("fromAmount") BigDecimal fromAmount,
                                @Param("toAmount") BigDecimal toAmount,
                                @Param("customerId") UUID customerId,
                                Pageable pageable);

    @Query("SELECT NEW rw.pacis.tekanaewallet.model.dtos.response.GetMyTransactionDTO(" +
            "t.id, " +
            "CASE WHEN t.senderWallet.id = :walletId THEN 'TRANSFER' " +
            "     ELSE 'RECEIVE' END," +
            "CONCAT(t.receiverWallet.customer.firstName, ' ', t.receiverWallet.customer.lastName), " +
            "t.receiverWallet.id, " +
            "CONCAT(t.senderWallet.customer.firstName, ' ', t.senderWallet.customer.lastName), " +
            "t.senderWallet.id, " +
            "t.amount, " +
            "t.createdAt) " +
            "FROM Transaction t " +
            "WHERE (:walletId IS NULL OR t.senderWallet.id = :walletId OR t.receiverWallet.id = :walletId) " +
            "AND (:fromAmount IS NULL OR t.amount >= :fromAmount) " +
            "AND (:toAmount IS NULL OR t.amount <= :toAmount)" +
            "AND (CAST(:startDate AS DATE) IS NULL OR (t.createdAt >= CAST(:startDate AS DATE))) " +
            "AND (CAST(:endDate AS DATE) IS NULL OR (t.createdAt <= CAST(:endDate AS DATE))) "
    ) Page<GetMyTransactionDTO> getMyTransactions(
            @Param("walletId") String walletId,
            @Param("fromAmount") BigDecimal fromAmount,
            @Param("toAmount") BigDecimal toAmount,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

}
