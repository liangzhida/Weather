package com.example.weather.custom;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    //activity集合，用来同意管理activity，方便一键退出
    private List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 添加activity
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除某个activity
     * @param activity
     */
    public void finishSingle(Activity activity){
        activityList.remove(activity);
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 移除所有的activity(通常会在一键退出的时候使用)
     */
    public void finishAll() {
        for (Activity activity : activityList
        ) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 清楚掉除了自己之外的activity
     * 这个方法一般会用在，如果activity任务栈中已经存在了几个activty了，但是你想回到最底层的activty并且销毁掉之前所有的activty
     * 比如说 现在有4个activity A B C D,A是第一个activty，然后进行activity的跳转 A ->B ->C ->D,然后你想从D->跳到A，并且销毁掉B和C，那么可以在进入到A后，在A中调用此方法
     * 举个实际点的例子：你想做一个切换账号的功能，一般此功能都是在 个人中心 ->设置 ->退出当前登录，那么如果你直接进入到登录界面进行登录不做任何处理的话，之前的activty没有finish掉
     * ，gc（垃圾回收机）也不会回收这些内存，这样很容易引起内存泄漏（亲身经历）
     * @param activity
     */
    public void finishAllButNotMe(Activity activity){
        for (Activity ac : activityList
        ) {
            if (ac == activity){
                //如果是当前的activty，不做任何处理
            }else if (!ac.isFinishing()) {
                //如果不是，finish掉
                ac.finish();
            }
        }
    }
}
