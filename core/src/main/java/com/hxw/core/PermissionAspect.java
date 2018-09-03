package com.modoutech.aopdemo;


import android.content.Context;
import android.support.v4.app.Fragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author hxw on 2018/9/3.
 */
@Aspect
public class PermissionAspect {


    /**
     * Pointcut注解，就是设置一个切入点。类中的意思就是被CheckPermission的注解所修饰的方法作为一个切点。
     *
     * @param checkPermission
     */
    @Pointcut("execution(@com.modoutech.aopdemo.CheckPermission * *(..)) && @annotation(checkPermission)")
    public void requestPermissionMethod(CheckPermission checkPermission) {
    }

    /**
     * Around注解，就是在执行被CheckPermission注解修饰的方法时，进入切点，并执行check方法。
     *
     * @param joinPoint
     * @param checkPermission
     * @return
     * @throws Throwable
     */
//    @Around("requestPermissionMethod(checkPermission)")
    @Around("execution(@com.modoutech.aopdemo.CheckPermission * *(..)) && @annotation(checkPermission)")
    public void check(ProceedingJoinPoint joinPoint, CheckPermission checkPermission) throws Throwable {
        Context context = null;
        final Object object = joinPoint.getThis();
        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        }
        if (context == null || checkPermission == null) {
            return;
        }

    }
}
