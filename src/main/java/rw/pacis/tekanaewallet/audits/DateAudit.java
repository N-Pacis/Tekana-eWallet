package rw.pacis.tekanaewallet.audits;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class DateAudit implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
	@CreationTimestamp
    private LocalDateTime doneAt;
}
