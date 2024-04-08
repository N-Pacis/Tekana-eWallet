package rw.pacis.tekanaewallet.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import rw.pacis.tekanaewallet.model.enums.ErrorCode;

import java.io.Serial;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {
    @Serial
    private static final long serialVersionUID = 2L;

    private String message;
    private final ErrorCode code;


    private Object[] args;

    public BadRequestException(ErrorCode code, String message, Object... args) {
        super(message);
        this.message = message;
        this.code = code;
        this.args = args;
    }
    public BadRequestException(String message) {
        super(message);
        this.message = message;
        this.code = ErrorCode.BAD_REQUEST;
    }
}
