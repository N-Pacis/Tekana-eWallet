package rw.pacis.tekanaewallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import rw.pacis.tekanaewallet.model.enums.ErrorCode;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@AllArgsConstructor
public class ResourceNotFoundException extends Exception {

    private String message;
    private final ErrorCode code;


    private Object[] args;

    public ResourceNotFoundException(String message, Object... args) {
        super(message);
        this.message = message;
        this.code = ErrorCode.ENTITY_NOT_FOUND;
        this.args = args;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
        this.code = ErrorCode.ENTITY_NOT_FOUND;
    }
}

