package practice.application.models.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import practice.application.models.DTO.ExResponseDTO;
import practice.application.models.exception.DuplicateEmailException;
import practice.application.models.exception.NoStockException;
import practice.application.models.exception.NotFoundException;
import practice.application.models.exception.OrderAlreadyCancelledException;

@RestControllerAdvice
@Slf4j
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundException.class)
    public ExResponseDTO ExceptionHandler(NotFoundException e) {
        return new ExResponseDTO("Bad", e.getMessage());
    }

    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    @ExceptionHandler(NoStockException.class)
    public ExResponseDTO ExceptionHandler(NoStockException e) {
        return new ExResponseDTO("Bad", e.getMessage());
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderAlreadyCancelledException.class)
    public ExResponseDTO ExceptionHandler(OrderAlreadyCancelledException e) {
        return new ExResponseDTO("Bad", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public ExResponseDTO ExceptionHandler(DuplicateEmailException e) {
        return new ExResponseDTO("Bad", e.getMessage());
    }





}
