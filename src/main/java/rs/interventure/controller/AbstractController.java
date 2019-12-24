package rs.interventure.controller;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.common.base.Preconditions;

@Slf4j
abstract class AbstractController {
    @ExceptionHandler
    public ResponseEntity<?> handleArgumentException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException caught while executing request", e);
        return newErrorResponse(HttpStatus.BAD_REQUEST, String
            .format("Invalid value for argument '%s': %s", e.getName(), e.getCause().getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBodyParsingException(HttpMessageConversionException e) {
        log.error("HttpMessageConversionException caught while executing request", e);
        String description = e.getMessage();
        Throwable cause = e.getCause();
        if (cause instanceof InvalidDefinitionException) {
            String className =
                ((InvalidDefinitionException) cause).getType().getRawClass().getSimpleName();
            description = String.format("Not possible to parse object '%s': %s", className,
                cause.getCause().getMessage());
        }
        return newErrorResponse(HttpStatus.BAD_REQUEST, description);
    }

    // if we use Preconditions.checkArgument(), it throws a IllegalArgumentException that is handled here
    @ExceptionHandler(value = {IllegalArgumentException.class,
        MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleGenericBadRequestException(Exception e) {
        log.error("Exception caught while executing request", e);
        return newErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // any other exception not handled before will go here
    @ExceptionHandler public ResponseEntity<?> handleGenericServerException(Exception e) {
        log.error("Exception caught while executing request", e);
        return newErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            e.getClass().getCanonicalName() + ": " + e.getMessage());
    }

    protected ResponseEntity<?> newErrorResponse(HttpStatus status, String description) {
        RestErrorResponse errorResponse =
            new RestErrorResponse(String.valueOf(status), status.getReasonPhrase(), description);
        return new ResponseEntity<>(errorResponse, status);
    }

    protected ResponseEntity<?> notFoundResponse(String description) {
        return newErrorResponse(HttpStatus.NOT_FOUND, description);
    }

    protected <ID> ResponseEntity<?> notFoundResponseForId(String entityName, ID id) {
        return notFoundResponse(String.format("No %s with id %s was found.", entityName, id));
    }

    // input assertions
    protected <ID> void assertRequestIdsAreEqual(ID pathId, ID bodyId) {
        Preconditions.checkArgument(bodyId == null || bodyId.equals(pathId),
            "The ids in the path (%s) and the body (%s) do not match.", pathId, bodyId);
    }

    @Value @AllArgsConstructor private class RestErrorResponse {
        private String code;
        private String message;
        private String description;
    }

}
