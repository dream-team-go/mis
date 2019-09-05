package com.dream.mis.core.plugin.shiro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来清除所有的Shiro访问控制注解，适合于Controller绝大部分方法都需要做访问控制，个别不需要做访问控制的场合。
 * 仅能用在方法上。
 * @author dafei
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)//注解保留位置：注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.METHOD})//注解的作用目标:方法
public @interface ClearShiro {
}
