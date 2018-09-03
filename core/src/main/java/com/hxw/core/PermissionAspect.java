package com.hxw.core;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.hxw.core.annotation.CheckPermission;
import com.hxw.core.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author hxw on 2018/9/3.
 */
@Aspect
public class PermissionAspect {
    public static final int REQUEST_CODE = 10086;

    /**
     * Around注解，就是在执行被CheckPermission注解修饰的方法时，进入切点，并执行check方法。
     *
     * @param joinPoint       切入点
     * @param checkPermission 检查权限的注解
     */
    @Around("execution(@com.hxw.core.annotation.CheckPermission * *(..)) && @annotation(checkPermission)")
    public void check(final ProceedingJoinPoint joinPoint, CheckPermission checkPermission) {
        final Object object = joinPoint.getThis();
        if (checkPermission == null) {
            return;
        }
        if (object instanceof Fragment) {
            PermissionUtils.checkPermissions((Fragment) object, new PermissionUtils.PermissionAction() {
                @Override
                public void doAction() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }, REQUEST_CODE, checkPermission.permissions());
        } else if (object instanceof Activity) {
            PermissionUtils.checkPermissions((Activity) object, new PermissionUtils.PermissionAction() {
                @Override
                public void doAction() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }, REQUEST_CODE, checkPermission.permissions());
        }


    }
}
