package weather.app.exception

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import weather.app.exception.dto.ErrorResponse
import java.util.logging.Logger

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = Logger.getLogger(this.javaClass.name)

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleInternalException(e: Exception): ErrorResponse {
        log.info("An exception was caught! $e")
        return ErrorResponse(500, "Something went wrong")
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ErrorResponse {
        log.info("An exception was caught! $e")
        return ErrorResponse(404, e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyRegisteredException::class)
    fun handleUserAlreadyRegisteredException(e: UserAlreadyRegisteredException): ErrorResponse {
        log.info("An exception was caught! $e")
        return ErrorResponse(400, e.message)
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): ErrorResponse {
        log.info("An exception was caught! $e")
        return ErrorResponse(403, "Access denied")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ErrorResponse {
        log.info("An exception was caught! $e")
        return ErrorResponse(400, "Invalid input")
    }
}
