package rw.pacis.tekanaewallet.model.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.pacis.tekanaewallet.model.enums.ETransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMyTransactionDTO {

    private UUID id;

    private ETransactionType type;

    private String receiverNames;

    private String receiverWalletId;

    private String senderNames;

    private String senderWalletId;

    private BigDecimal amount;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime doneAt;

    public GetMyTransactionDTO(UUID id, String type, String receiverNames, String receiverWalletId, String senderNames, String senderWalletId, BigDecimal amount, LocalDateTime doneAt){
        this.id = id;
        this.type = ETransactionType.fromValue(type);
        this.receiverNames = receiverNames;
        this.receiverWalletId = receiverWalletId;
        this.senderNames = senderNames;
        this.senderWalletId = senderWalletId;
        this.amount = amount;
        this.doneAt = doneAt;
    }
}
