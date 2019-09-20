package exchange.velox.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Aspect
public class ExceptionTranslatorAspect {

    @Around("@annotation(ExceptionTranslator)")
    public Object translateExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ExceptionTranslator annotation = method.getAnnotation(ExceptionTranslator.class);
        Class<? extends Exception> wrappedBy = annotation.wrappedBy();
        String errorMessage = annotation.message();
        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            Constructor<? extends Exception> constructor = wrappedBy.getConstructor(String.class, Throwable.class);
            throw constructor.newInstance(errorMessage, t);
        }
    }
}