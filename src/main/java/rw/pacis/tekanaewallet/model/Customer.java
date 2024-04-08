package rw.pacis.tekanaewallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerDTO;
import rw.pacis.tekanaewallet.model.enums.EGender;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers", indexes = {
        @Index(name = "idx_customer_phone_number", columnList = "phone_number", unique = true)
})
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class Customer extends UserAccount{

    @Column(name = "phone_number", unique = true ,nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private EGender gender;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<CustomerWallet> wallets;

    public Customer(RegisterCustomerDTO dto){
        super(dto);
        this.phoneNumber = dto.getPhoneNumber();
        this.gender = dto.getGender();
    }
}
