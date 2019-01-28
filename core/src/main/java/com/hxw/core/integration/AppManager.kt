package com.hxw.core.integration

import android.app.Activity
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*


/**
 * 用于管理所有 [Activity],和在前台的 [Activity]
 *
 * @author hxw
 * @date 2018/5/4
 */
object AppManager {
    private var currentActivity: WeakReference<Activity>? = null
    private val mActivityStack = LinkedList<Activity>()

    /**
     * 设置处于活动中的activity
     *
     * @param activity [Activity]
     */
    internal fun setCurrentActivity(activity: Activity?) {
        currentActivity = if (activity == null) {
            null
        } else {
            WeakReference(activity)
        }
    }

    /**
     * 获取处于活动中的activity
     *
     * @return [Activity]
     */
    fun getCurrentActivity(): Activity? {
        return currentActivity?.get()
    }

    /**
     * 获取最近启动的一个 [Activity], 此方法不保证获取到的 [Activity] 正处于前台可见状态
     * 即使 App 进入后台或在这个 [Activity] 中打开一个之前已经存在的 [Activity], 这时调用此方法
     * 还是会返回这个最近启动的 [Activity], 因此基本不会出现 `null` 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     *
     * Tips: mActivityStack 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     * @return [Activity]
     */
    fun getTopActivity(): Activity? {
        if (mActivityStack.isEmpty()) {
            Timber.w("mActivityStack size is 0 when getTopActivity()")
            return null
        }
        return mActivityStack.last
    }


    /**
     * 添加 [Activity] 到集合
     *
     * @param activity [Activity]
     */
    internal fun addActivity(activity: Activity) {
        synchronized(AppManager::class.java) {
            mActivityStack.add(activity)
        }
    }

    /**
     * 删除集合里的指定的 [Activity] 实例
     *
     * @param activity [Activity]
     */
    internal fun removeActivity(activity: Activity) {
        synchronized(AppManager::class.java) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity)
            }
        }
    }

    /**
     * 关闭指定的activity,存在多个也会一起关闭
     *
     * @param activityClass 指定的[Activity]类名
     */
    fun killActivity(vararg activityClass: Class<*>) {
        synchronized(AppManager::class.java) {
            val iterator = mActivityStack.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (activityClass.contains(next.javaClass)) {
                    iterator.remove()
                    next.finish()
                }
            }
        }
    }

    /**
     * 关闭所有[Activity]除了指定的[Activity]
     * @param activityClass 指定的[Activity]类名
     */
    fun killAllExclude(vararg activityClass: Class<*>) {
        synchronized(AppManager::class.java) {
            val iterator = mActivityStack.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (activityClass.contains(next.javaClass)) {
                    continue
                }
                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 退出应用程序
     */
    fun exitApp() {
        try {
            killAll()
            mActivityStack.clear()
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid())
            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
            System.exit(-1)
        }

    }

    /**
     * 关闭所有 activity
     */
    fun killAll() {
        synchronized(AppManager::class.java) {
            val iterator = mActivityStack.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                iterator.remove()
                next.finish()
            }
        }
    }
}
