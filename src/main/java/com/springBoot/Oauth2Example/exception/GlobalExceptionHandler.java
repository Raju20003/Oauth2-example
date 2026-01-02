    package com.springBoot.Oauth2Example.exception;

    import com.fasterxml.jackson.databind.JsonMappingException;
    import com.fasterxml.jackson.databind.exc.InvalidFormatException;
    import com.springBoot.Oauth2Example.dto.MessageResponse;
    import io.jsonwebtoken.ExpiredJwtException;
    import io.jsonwebtoken.MalformedJwtException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.http.converter.HttpMessageNotReadableException;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.web.HttpMediaTypeNotSupportedException;
    import org.springframework.web.HttpRequestMethodNotSupportedException;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.MissingServletRequestParameterException;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
    import org.springframework.web.multipart.MaxUploadSizeExceededException;

    import java.util.Optional;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
            String message = Optional.ofNullable(ex.getBindingResult().getFieldError())
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).orElse("Validation error");
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message),HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<MessageResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
            String message = "Request body is missing or malformed.";

            Throwable cause = ex.getCause();
            if (cause instanceof InvalidFormatException ife) {
                String fieldName = ife.getPath().isEmpty() ? "unknown" : ife.getPath().get(0).getFieldName();
                String expectedType = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "unknown";
                message = String.format("Field '%s' must be of type %s.", fieldName, expectedType);
            } else if (cause instanceof JsonMappingException) {
                message = "Invalid input structure.";
            }
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message),HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
        public ResponseEntity<MessageResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
            String message = "Unsupported " + ex.getContentType() + ". Supported: " + ex.getSupportedMediaTypes();
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<MessageResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
            String message = "Method '" + ex.getMethod() + "' is not supported. Supported methods: " + String.join(", ", ex.getSupportedMethods());
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<MessageResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
            String requiredType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "unknown";
            String message = "Parameter '" + ex.getName() + "' must be of type " + requiredType;
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<MessageResponse> handleMissingParams(MissingServletRequestParameterException ex) {
            String message = "Required request parameter '" + ex.getParameterName() + "' is missing";
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<MessageResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
            String message = "File size exceeds limit! Maximum allowed size is 10MB.";
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MalformedJwtException.class)
        public ResponseEntity<MessageResponse> handleMalformedJwt(MalformedJwtException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, "Malformed JWT Token"), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ExpiredJwtException.class)
        public ResponseEntity<MessageResponse> handleExpiredJwt(ExpiredJwtException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.UNAUTHORIZED, "JWT Token is expired"), HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(UserUnauthorizedException.class)
        public ResponseEntity<MessageResponse> handleUserUnauthorized(UserUnauthorizedException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.FORBIDDEN, ex.getMessage()), HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<MessageResponse> handleAccessDeniedException(AccessDeniedException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.FORBIDDEN, "Access denied: " + ex.getMessage()), HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(TokenUnauthorizedException.class)
        public ResponseEntity<MessageResponse> handleTokenUnauthorized(TokenUnauthorizedException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(ResourceAlreadyExistsException.class)
        public ResponseEntity<MessageResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.ALREADY_REPORTED, ex.getMessage()), HttpStatus.ALREADY_REPORTED);
        }

        @ExceptionHandler(LoggedOutException.class)
        public ResponseEntity<MessageResponse> handleLoggedOut(LoggedOutException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(InactiveStatusException.class)
        public ResponseEntity<MessageResponse> handleInactiveStatus(InactiveStatusException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(InvalidArgumentException.class)
        public ResponseEntity<MessageResponse> handleInvalidArgument(InvalidArgumentException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(NullPointerException.class)
        public ResponseEntity<MessageResponse> handleNullPointer(NullPointerException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, "A required value was null: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<MessageResponse> handleOrderNotFound(ResourceNotFoundException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ResourceIncorrectException.class)
        public ResponseEntity<MessageResponse> handlePasswordIncorrect(ResourceIncorrectException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DuplicateEntryException.class)
        public ResponseEntity<MessageResponse> handleDuplicateEntry(DuplicateEntryException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ExpiredTokenException.class)
        public ResponseEntity<MessageResponse> handleExpiredToken(ExpiredTokenException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ConfirmTokenExpiredException.class)
        public ResponseEntity<MessageResponse> handleConfirmTokenExpired(ConfirmTokenExpiredException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(CustomNullPointerException.class)
        public ResponseEntity<MessageResponse> handleCustomNullPointer(CustomNullPointerException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AlreadyDeletedException.class)
        public ResponseEntity<MessageResponse> handleAlreadyDeleted(AlreadyDeletedException ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<MessageResponse> handleAllOtherExceptions(Exception ex) {
            return new ResponseEntity<>(new MessageResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
