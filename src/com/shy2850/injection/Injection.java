package com.shy2850.injection;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.*;
/**
 * @author shy2850
 * @version 1.0
 */
@Target({FIELD,METHOD})
@Retention(RUNTIME)
public @interface Injection{
	String name() default "";
	Class<?> type() default java.lang.Object.class;
	enum AuthenticationType {
	    CONTAINER,
	    APPLICATION
    }
	AuthenticationType authenticationType() default AuthenticationType.CONTAINER;
	boolean shareable() default true;
	String mappedName() default "";
	String description() default "";
}
