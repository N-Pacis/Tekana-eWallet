package rw.pacis.tekanaewallet.model.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransactionDTO {

    @NotNull
    private String senderWalletId;

    @NotNull
    private String receiverWalletId;

    @NotNull
    private BigDecimal amount;
}
