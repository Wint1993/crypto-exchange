package crypto.exchange.general.exception;

import crypto.exchange.general.dto.ValidationErrorDTO;
import crypto.exchange.general.dto.BaseApiContractDTO;
import crypto.exchange.general.exception.exceptions.EmptyListException;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.Collections;

@Log4j2
@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<BaseApiContractDTO<ValidationErrorDTO>> handleAllExceptions(Exception ex) {
        log.error("Exception: ", ex);
        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(null, MessageFormat.format("Server Error: {0}", ex.getLocalizedMessage()) , null);
        return ResponseEntity.internalServerError().body(new BaseApiContractDTO<>(Collections.singletonList(validationErrorDTO)));
    }

    @ExceptionHandler(FeignException.class)
    public final ResponseEntity<BaseApiContractDTO<ValidationErrorDTO>> handleFeignException(FeignException ex) {
        log.error("Feign Client Exception: ", ex);
        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(null, "Server Error, an error occurred, please try again later", null);
        return ResponseEntity.internalServerError().body(new BaseApiContractDTO<>(Collections.singletonList(validationErrorDTO)));
    }

    @ExceptionHandler(EmptyListException.class)
    public final ResponseEntity<BaseApiContractDTO<ValidationErrorDTO>> handleEmptyListException(EmptyListException ex) {
        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(null, ex.getLocalizedMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiContractDTO<>(Collections.singletonList(validationErrorDTO)));
    }
}
