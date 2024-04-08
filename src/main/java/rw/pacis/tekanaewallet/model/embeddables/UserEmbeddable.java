package rw.pacis.tekanaewallet.model.embeddables;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.pacis.tekanaewallet.model.UserAccount;
import rw.pacis.tekanaewallet.model.enums.ERole;
import rw.pacis.tekanaewallet.model.enums.EUserStatus;


@Embeddable
@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public class UserEmbeddable {
    @Column(name = "_first_name")
    private String firstName;

    @Column(name = "_last_name")
    private String lastName;

    @Column(name="_email")
    private String email;

    @Column(name = "_role")
    private ERole role;

    @Column(name = "_status")
    private EUserStatus status;

    public UserEmbeddable(@NotNull UserAccount userAccount) {
        this.firstName = userAccount.getFirstName();
        this.lastName = userAccount.getLastName();
        this.email = userAccount.getEmail();
        this.role = userAccount.getRole();
        this.status = userAccount.getStatus();
    }
}

