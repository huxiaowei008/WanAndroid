package com.hxw.core

import android.app.Activity
import androidx.fragment.app.Fragment
import com.hxw.core.annotation.CheckPermission
import com.hxw.core.utils.PermissionUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

/**
 * 权限切面声明
 *
 * @author hxw on 2018/9/3.
 * @ Aspect：声明切面，标记类
 */
@Aspect
class PermissionAspect {
    companion object {
        const val REQUEST_CODE = 10086
    }

    /**
     * Around注解，就是在执行被CheckPermission注解修饰的方法时，进入切点，并执行check方法。
     * 切点表达式结构：
     * execution(<@注解类型模式>? <修饰符模式>? <返回类型模式> <方法名模式>(<参数模式>) <异常模式>?)
     * 带?是可以不填写的,用*号代表任意
     *
     * @param joinPoint       连接点
     * @param checkPermission 检查权限的注解
    </异常模式></参数模式></方法名模式></返回类型模式></修饰符模式> */
    @Around("execution(@com.hxw.core.annotation.CheckPermission * *(..)) && @annotation(checkPermission)")
    fun check(joinPoint: ProceedingJoinPoint, checkPermission: CheckPermission?) {
        val host = joinPoint.getThis()
        if (checkPermission == null) {
            return
        }
        when (host) {
            is Fragment -> PermissionUtils.checkPermissions(host, checkPermission.permissions, REQUEST_CODE, object : PermissionUtils.PermissionAction {
                override fun doAction() {
                    try {
                        joinPoint.proceed()
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                }
            })
            is Activity -> PermissionUtils.checkPermissions(host, checkPermission.permissions, REQUEST_CODE, object : PermissionUtils.PermissionAction {
                override fun doAction() {
                    try {
                        joinPoint.proceed()
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                }
            })
            else -> throw AssertionError("目标不是Activity或Fragment")
        }
    }


}
