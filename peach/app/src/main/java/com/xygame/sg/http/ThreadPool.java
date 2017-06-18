package com.xygame.sg.http;

import android.content.Context;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tony on 2016/3/10.
 */
public class ThreadPool {
    private static ExecutorService FULL_TASK_EXECUTOR;
    private static ThreadPool mInstance;

    public static ThreadPool getInstance() {
        if (mInstance == null) {
            synchronized (ThreadPool.class) {
                if (mInstance == null) {
                    mInstance = new ThreadPool();
                }
            }
        }
        return mInstance;
    }

    public ThreadPool(){
        FULL_TASK_EXECUTOR= Executors.newCachedThreadPool();
    }

    public void excuseAction(Handler.Callback context,RequestBean requestBean,int constData){
        if (requestBean.getData()!=null){
            HttpService task=new HttpService(context,requestBean,constData);
            task.executeOnExecutor(FULL_TASK_EXECUTOR);
        }
    }

    public void excuseThread(Runnable runnable){
        FULL_TASK_EXECUTOR.execute(runnable);
    }
}
