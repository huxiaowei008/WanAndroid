package com.hxw.core.integration

import android.app.Activity
import org.kodein.di.Kodein
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*


/**
 * 用于管理所有 [Activity],和在前台的 [Activity]
 *
 * @author hxw on 2018/5/4.
 */
object AppManager {
    val kodein = Kodein {  }
    private var currentActivity: WeakReference<Activity>? = null
    private val mActivityStack = LinkedList<Activity>()

    fun setCurrentActivity(activity: Activity?) {
        currentActivity = if (activity == null) {
            null
        } else {
            WeakReference(activity)
        }
    }

    fun getCurrentActivity(): Activity? {
        return currentActivity?.get()
    }

    /**
     * 获取最近启动的一个 [Activity], 此方法不保证获取到的 [Activity] 正处于前台可见状态
     * 即使 App 进入后台或在这个 [Activity] 中打开一个之前已经存在的 [Activity], 这时调用此方法
     * 还是会返回这个最近启动的 [Activity], 因此基本不会出现 `null` 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     *
     *
     * Tips: mActivityStack 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
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
     */
    fun addActivity(activity: Activity) {
        synchronized(AppManager::class.java) {
            mActivityStack.add(activity)
        }
    }

    /**
     * 删除集合里的指定的 [Activity] 实例
     *
     * @param activity [Activity]
     */
    fun removeActivity(activity: Activity) {
        synchronized(AppManager::class.java) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity)
            }
        }
    }

    fun killActivity(activityClass: Class<*>) {
        synchronized(AppManager::class.java) {
            val iterator = mActivityStack.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.javaClass == activityClass) {
                    iterator.remove()
                    next.finish()
                }
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
    private fun killAll() {
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
