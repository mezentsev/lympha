package pro.mezentsev.lympha.aspects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import pro.mezentsev.lympha.events.Event;
import pro.mezentsev.lympha.informer.Informer;

@Aspect
public class TimingProfiler {

    @Around("@annotation(pro.mezentsev.lympha.annotation.LymphaProfiler) && execution(@pro.mezentsev.lympha.annotation.LymphaProfiler * *.*(..))")
    @Nullable
    public Object getRealNumber(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue = null;
        Throwable throwable = null;

        processEnterMethod(joinPoint);

        long startTime = System.nanoTime();
        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable t) {
            throwable = t;
            throw t;
        } finally {
            long endTime = System.nanoTime();
            long totalMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            processExitMethod(joinPoint, totalMs, returnValue, throwable);
        }

        return returnValue;
    }

    private static void processEnterMethod(@NonNull JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Class[] parameterTypes = codeSignature.getParameterTypes();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("⇢ ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterTypes[i]).append(" ").append(parameterNames[i]).append(" = ");
            builder.append("\"").append(parameterValues[i]).append("\"");
        }
        builder.append(')');

        Informer.inform(new ProfilerEvent(builder.toString(), 0L));
    }

    private static void processExitMethod(@NonNull JoinPoint joinPoint, long time, @Nullable Object returnValue, @Nullable Throwable t) {
        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("⇠ ").append(methodName);

        if (hasReturnType) {
            builder.append(" = ");
            if (returnValue != null) {
                builder.append(returnValue.getClass().getSimpleName()).append(" ");
            }
            builder.append("\"").append(returnValue).append("\"");
        }

        Informer.inform(new ProfilerEvent(builder.toString(), time));
    }

    private static class ProfilerEvent extends Event.Simple {

        private final long timeTaken;

        ProfilerEvent(@NotNull String message,
                      long timeTaken) {
            super(message);
            this.timeTaken = timeTaken;
        }

        @Override
        public long getTimeTaken() {
            return timeTaken;
        }
    }
}
