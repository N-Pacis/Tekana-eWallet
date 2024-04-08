package rw.pacis.tekanaewallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rw.pacis.tekanaewallet.audits.AuditDetail;
import rw.pacis.tekanaewallet.model.embeddables.UserEmbeddable;
import rw.pacis.tekanaewallet.model.enums.EAuditType;

import java.io.Serial;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class UserAccountAudit extends AuditDetail<UserEmbeddable> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "UserAuditUUID")
    @GenericGenerator(name="UserAuditUUID", strategy="org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount;


    public UserAccountAudit(
            UserAccount userAccount, EAuditType auditType,
            UUID operatorId, String operatorNames, String operatorEmail,
            String observation
    ) {
        this.userAccount = userAccount;
        this.setSnapshot(new UserEmbeddable(userAccount));
        this.setAuditType(auditType);
        this.setOperatorId(operatorId);
        this.setOperatorNames(operatorNames);
        this.setOperatorEmail(operatorEmail);
        this.setObservation(observation);
    }
}

