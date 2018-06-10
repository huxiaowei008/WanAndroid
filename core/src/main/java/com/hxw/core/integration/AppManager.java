package com.hxw.core.integration;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * 用于管理所有 {@link Activity},和在前台的 {@link Activity}
 *
 * @author hxw on 2018/5/4.
 */
@Singleton
public final class AppManager {

    private Activity mCurrentActivity;
    private LinkedList<Activity> mActivityStack = new LinkedList<>();

    @Inject
    AppManager() {
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }


    /**
     * 添加 {@link Activity} 到集合
     */
    public void addActivity(@NonNull Activity activity) {
        synchronized (AppManager.class) {
            mActivityStack.add(activity);
        }
    }

    /**
     * 获取最近启动的一个 {@link Activity}, 此方法不保证获取到的 {@link Activity} 正处于前台可见状态
     * 即使 App 进入后台或在这个 {@link Activity} 中打开一个之前已经存在的 {@link Activity}, 这时调用此方法
     * 还是会返回这个最近启动的 {@link Activity}, 因此基本不会出现 {@code null} 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     * <p>
     * Tips: mActivityStack 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     */
    public Activity getTopActivity() {
        if (mActivityStack.isEmpty()) {
            Timber.w("mActivityStack size is 0 when getTopActivity()");
            return null;
        }
        return mActivityStack.getLast();
    }

    /**
     * 删除集合里的指定的 {@link Activity} 实例
     *
     * @param activity {@link Activity}
     */
    public void removeActivity(@NonNull Activity activity) {
        synchronized (AppManager.class) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
            }
        }
    }

    public void killActivity(Class<?> activityClass) {
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (next.getClass().equals(activityClass)) {
                    iterator.remove();
                    next.finish();
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            killAll();
            mActivityStack.clear();
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 关闭所有 activity
     */
    private void killAll() {
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = mActivityStack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                iterator.remove();
                next.finish();
            }
        }
    }


}
