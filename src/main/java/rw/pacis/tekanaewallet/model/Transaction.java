package rw.pacis.tekanaewallet.model;

import jakarta.persistence.*;
import lombok.*;
import rw.pacis.tekanaewallet.audits.TimestampAudit;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_transaction_sender_wallet", columnList = "sender_wallet_id"),
        @Index(name = "idx_transaction_receiver_wallet", columnList = "receiver_wallet_id")
})
public class Transaction extends TimestampAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", nullable = false)
    private CustomerWallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id", nullable = true)
    private CustomerWallet receiverWallet;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public Transaction(CustomerWallet senderWallet, CustomerWallet receiverWallet, BigDecimal amount) {
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
    }
}