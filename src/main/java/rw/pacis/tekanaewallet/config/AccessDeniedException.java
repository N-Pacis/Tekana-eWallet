package rw.pacis.tekanaewallet.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
@Getter
@AllArgsConstructor
public class AccessDeniedException extends RuntimeException {

    private String message = "exceptions.accessDenied";
    private Object[] args;

    public AccessDeniedException(Object ...args){
        super("Message");
        this.args = args;

    }
}
