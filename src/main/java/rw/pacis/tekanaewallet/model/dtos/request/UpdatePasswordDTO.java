package rw.pacis.tekanaewallet.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UpdatePasswordDTO {
    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
