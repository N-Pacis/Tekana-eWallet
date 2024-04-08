package rw.pacis.tekanaewallet.exceptions;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import rw.pacis.tekanaewallet.utils.ApiResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebMvc
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException ex, Locale locale) {
        return new ApiResponse<>(messageSource.getMessage("exceptions.invalidCredentials", null, locale), (Object) "", HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, Locale locale) {
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        } catch (NoSuchMessageException e) {
            errorMessage = ex.getMessage();
        }

        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.NOT_FOUND).toResponseEntity();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> badRequest(BadRequestException ex, Locale locale) {
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        } catch (NoSuchMessageException e) {
            errorMessage = ex.getMessage();
        }

        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> unAuthorizedException(UnAuthorizedException ex, Locale locale) {
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        } catch (NoSuchMessageException e) {
            errorMessage = ex.getMessage();
        }

        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> invalidCredentialsException(InvalidCredentialsException ex, Locale locale) {
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        } catch (NoSuchMessageException e) {
            errorMessage = ex.getMessage();
        }

        return new ApiResponse<>(errorMessage, (Object) errorMessage, HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public  ResponseEntity<?> handlerAccessDeniedException(final Exception ex,
                                                           final HttpServletRequest request, final HttpServletResponse response) {

        return new ApiResponse<>(ex.getMessage(), (Object) "", HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?>  handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = messageSource.getMessage("exceptions.validation.message", null, locale);
        return new ApiResponse<>(errorMessage, errors, HttpStatus.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, Locale locale) throws JsonProcessingException {
        String message = ex.getMessage();
        Object error = ex.getMessage();
       // ex.printStackTrace();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex.getClass().getSimpleName().equals("InternalAuthenticationServiceException"))
            status = HttpStatus.UNAUTHORIZED;

        if (ex.getClass().getSimpleName().equals("HttpMessageNotReadableException")) {
            status = HttpStatus.BAD_REQUEST;
            message = "Malformed JSON request format";
        }

        String errorMessage = messageSource.getMessage("exceptions.validation.server", null, locale);
        log.error(ex.getMessage());
        return new ApiResponse<>(errorMessage, error, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
    }
}

