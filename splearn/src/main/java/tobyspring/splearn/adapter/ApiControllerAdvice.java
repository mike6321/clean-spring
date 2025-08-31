package tobyspring.splearn.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tobyspring.splearn.domain.member.DuplicationEmailException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail hendleException(Exception e) {
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(DuplicationEmailException.class)
    public ProblemDetail handleDuplicateEmailException(DuplicationEmailException e) {
        return getProblemDetail(HttpStatus.CONFLICT, e);
    }

    private static ProblemDetail getProblemDetail(HttpStatus httpStatus, Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, e.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", e.getClass().getSimpleName());
        return problemDetail;
    }

}
