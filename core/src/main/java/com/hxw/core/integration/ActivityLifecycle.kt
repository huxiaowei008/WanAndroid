package com.hxw.core.integration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.hxw.core.base.IActivity
import timber.log.Timber


/**
 * [Application.ActivityLifecycleCallbacks] 默认实现类
 * 实现内部逻辑
 *
 * @author hxw
 * @date 2018/5/5
 */
class ActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private val mFragmentLifecycle: FragmentLifecycle by lazy { FragmentLifecycle() }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.i("%s - onActivityCreated", activity.toString())
        AppManager.addActivity(activity)

        val useFragment = activity is IActivity && (activity as IActivity).useFragment()
        if (activity is FragmentActivity && useFragment) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(mFragmentLifecycle, true)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("%s - onActivityStarted", activity.toString())
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.i("%s - onActivityResumed", activity.toString())
        AppManager.setCurrentActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("%s - onActivityPaused", activity.toString())
        if (AppManager.getCurrentActivity() === activity) {
            AppManager.setCurrentActivity(null)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.i("%s - onActivityStopped", activity.toString())
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        Timber.i("%s - onActivitySaveInstanceState", activity.toString())
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("%s - onActivityDestroyed", activity.toString())
        AppManager.removeActivity(activity)
    }
}
