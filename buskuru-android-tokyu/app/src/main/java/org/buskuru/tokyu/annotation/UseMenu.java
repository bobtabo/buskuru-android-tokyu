package org.buskuru.tokyu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メニュー使用アノテーションです。
 *
 * @author <a href="mailto:bobtabo.buhibuhi@gmail.com">Satoshi Nagashiba</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseMenu {

}
