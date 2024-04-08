package rw.pacis.tekanaewallet.audits;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rw.pacis.tekanaewallet.model.enums.EAuditType;

import java.time.LocalDateTime;
import java.util.UUID;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
public abstract class AuditDetail<T> {

    private String observation;

    @Embedded
    private T snapshot;

    @Enumerated(EnumType.STRING)
    private EAuditType auditType;

    private UUID operatorId;

    private String operatorNames;

    private String operatorEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime doneAt;
}
