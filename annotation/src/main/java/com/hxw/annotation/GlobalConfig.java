package com.hxw.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hxw on 2018/5/8
 * 参考: https://juejin.im/entry/577142c3a633bd006435eea4
 * https://juejin.im/post/5a9698a8f265da4e7d6077bc
 * Retention 保留的范围，默认值为CLASS. 可选值有三种
 * <p>
 * RetentionPolicy.SOURCE(只保留在源码中，会被编译器丢弃)
 * RetentionPolicy.CLASS(注解会被编译器记录在class文件中，但不需要被VM保留到运行时，这也是默认的行为)
 * RetentionPolicy.RUNTIME(注解会被编译器记录在class文件中并被VM保留到运行时，所以可以通过反射获取)
 * </p>
 * Target 表明该注解类型可以注解哪些程序元素
 * <p>
 * ElementType.TYPE(类、接口(包括注解类型)、枚举的声明)
 * ElementType.FIELD(字段(包括枚举常量)的声明)
 * ElementType.METHOD(方法的声明)
 * ElementType.PARAMETER(形参的声明)
 * ElementType.CONSTRUCTOR(构造器的声明)
 * ElementType.LOCAL_VARIABLE(本地变量的声明)
 * ElementType.ANNOTATION_TYPE(注解类型的声明)
 * ElementType.PACKAGE(包的声明)
 * ElementType.TYPE_PARAMETER(泛型参数的声明)
 * ElementType.TYPE_USE(泛型的使用)
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GlobalConfig {
}
