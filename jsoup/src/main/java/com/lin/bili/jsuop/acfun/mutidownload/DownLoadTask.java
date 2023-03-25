package com.lin.bili.jsuop.acfun.mutidownload;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class DownLoadTask<T> implements Runnable {
    protected int start;
    protected int end;
    protected List<T> data;
    protected List<String> aids;

    public DownLoadTask(int start, int end, List<T> data, List<String> aids) {
        this.start = start;
        this.end = end;
        this.data = data;
        this.aids = aids;
    }

    @Override
    public void run() {
        try {
            for (int i=start; i<end; i++) {
                doDownload(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void doDownload(int i);
}