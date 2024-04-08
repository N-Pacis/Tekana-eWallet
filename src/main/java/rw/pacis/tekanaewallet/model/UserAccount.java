package rw.pacis.tekanaewallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import rw.pacis.tekanaewallet.audits.TimestampAudit;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterCustomerDTO;
import rw.pacis.tekanaewallet.model.dtos.request.RegisterUserDTO;
import rw.pacis.tekanaewallet.model.enums.ELoginStatus;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {
        @Index(name = "idx_user_account_email", columnList = "email", unique = true)})
@OnDelete(action = OnDeleteAction.CASCADE)
public class UserAccount extends TimestampAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Transient
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ERole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EUserStatus status = EUserStatus.PENDING;

    @JsonIgnore
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(unique = true)
    private UUID sessionId;

    @Column
    @Enumerated(EnumType.STRING)
    private ELoginStatus loginStatus = ELoginStatus.INACTIVE;

    @Column(name = "last_login")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

    @JsonIgnore
    @Transient
    private Collection<GrantedAuthority> authorities;

    public UserAccount(String firstName, String lastName, String email, ERole role, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public UserAccount(RegisterUserDTO dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
    }

    public UserAccount(RegisterCustomerDTO dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.role = ERole.CUSTOMER;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}