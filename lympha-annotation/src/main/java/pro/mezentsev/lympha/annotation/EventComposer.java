package pro.mezentsev.lympha.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EventComposer {

    Class<?> classType();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.CLASS)
    public @interface Factory {

    }
}
