package FrameworkSpringJava.dao;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({METHOD,CONSTRUCTOR,FIELD,PARAMETER,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
