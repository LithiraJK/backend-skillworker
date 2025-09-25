package lk.ijse.skillworker_backend.aspect;

import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This aspect is used to log the method name and arguments of all controller methods.
 * The aspect is divided into two parts: pointcuts and advices.
 * Pointcuts are used to define the methods that need to be logged.
 * Advices are used to log the method name and arguments before and after the method is executed.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    /**
     * Pointcuts for all controller methods.
     */
    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.AdController.*(..))")
    public void adControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.AuthController.*(..))")
    public void authControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.CategoryController.*(..))")
    public void categoryControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.ChatController.*(..))")
    public void chatControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.ChatRestController.*(..))")
    public void chatRestControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.CloudinaryController.*(..))")
    public void cloudinaryControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.LocationController.*(..))")
    public void locationControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.OAuth2Controller.*(..))")
    public void oAuth2ControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.ReviewController.*(..))")
    public void reviewControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.SubscriptionController.*(..))")
    public void subscriptionControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.UserController.*(..))")
    public void userControllerMethods() {}

    @Pointcut("execution(* lk.ijse.skillworker_backend.controller.WorkerController.*(..))")
    public void workerControllerMethods() {}

    /**
     * Advice for logging before the method is executed.
     * @param joinPoint the method that is being executed.
     */
    @Before("adControllerMethods() || authControllerMethods() || categoryControllerMethods() || " +
            "chatControllerMethods() || chatRestControllerMethods() || cloudinaryControllerMethods() || " +
            "locationControllerMethods() || oAuth2ControllerMethods() || reviewControllerMethods() || " +
            "subscriptionControllerMethods() || userControllerMethods() || workerControllerMethods()")
    private void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    /**
     * Advice for logging after the method is executed.
     * @param joinPoint the method that is being executed.
     * @param result the return value of the method.
     */
    @AfterReturning(pointcut = "adControllerMethods() || authControllerMethods() || categoryControllerMethods() || " +
            "chatControllerMethods() || chatRestControllerMethods() || cloudinaryControllerMethods() || " +
            "locationControllerMethods() || oAuth2ControllerMethods() || reviewControllerMethods() || " +
            "subscriptionControllerMethods() || userControllerMethods() || workerControllerMethods()", returning = "result")
    private void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            logResponseEntity(joinPoint, responseEntity);
        } else if (result instanceof Collection<?> collection) {
            logCollection(joinPoint, collection);
        } else {
            logResult(joinPoint, result);
        }
    }
    /**
     * Logs the response entity.
     * @param joinPoint the method that is being executed.
     * @param responseEntity the response entity.
     */
    private void logResponseEntity(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        Object body = responseEntity.getBody();
        if (body instanceof AuthResponseDTO) {
            log.info("Exiting method: {} with result: <{} {} {}>",
                    joinPoint.getSignature().toShortString(),
                    responseEntity.getStatusCode(),
                    ((HttpStatus) responseEntity.getStatusCode()).getReasonPhrase(),
                    "AuthResponseDTO(token=****)");
        } else if (body instanceof Collection<?> collection) {
            logCollection(joinPoint, collection);
        } else {
            logResult(joinPoint, body);
        }
    }
    /**
     * Logs the collection.
     * @param joinPoint the method that is being executed.
     * @param collection the collection.
     */
    private void logCollection(JoinPoint joinPoint, Collection<?> collection) {
        log.info("Exiting method: {} with result size: {}", joinPoint.getSignature().toShortString(), collection.size());
    }
    /**
     * Logs the result.
     * @param joinPoint the method that is being executed.
     * @param result the result.
     */
    private void logResult(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }
}
