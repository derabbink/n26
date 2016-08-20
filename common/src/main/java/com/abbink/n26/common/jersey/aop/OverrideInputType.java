package com.abbink.n26.common.jersey.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jersey resource annotation used to declare how to override
 * any Content-Type request headers
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OverrideInputType {
	/**
	 * What the Content-Type request header value should be replaced by
	 */
	String value();
	
	/**
	 * which Content-Type request header values should not be replaced
	 * (case-insentitive)
	 */
	String[] except() default {};
}
