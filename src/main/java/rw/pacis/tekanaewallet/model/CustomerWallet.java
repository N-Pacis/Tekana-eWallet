package rw.pacis.tekanaewallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rw.pacis.tekanaewallet.audits.TimestampAudit;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerWalletDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomerWallet extends TimestampAudit {

    @Id
    private String id;

    @ManyToOne
    private Customer customer;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public CustomerWallet(String id, Customer customer, RegisterCustomerWalletDTO dto){
        this.id = id;
        this.customer = customer;
        this.balance = dto.getBalance();
    }

}
