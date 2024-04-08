package rw.pacis.tekanaewallet.audits;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDateAudit extends DateAudit {

	private UUID operatorId;

	private String operatorNames;

	private UUID userRoleId;

	private String userRoleName;

	private String operatorPrivilege;
}
