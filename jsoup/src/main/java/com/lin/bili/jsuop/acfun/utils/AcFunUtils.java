package com.lin.bili.jsuop.acfun.utils;

import com.lin.bili.jsuop.acfun.mapper.AnimeMapper;
import com.lin.bili.jsuop.acfun.mutidownload.DownLoadTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AcFunUtils {
    public static int parseState(String stateDetail) {
        if (stateDetail.startsWith("连载中")) {
            return 0;
        }
        if (stateDetail.startsWith("全")) {
            return 1;
        }
        if (stateDetail.startsWith("即将上映")) {
            return 2;
        }
        return -1;
    }

    public static int parseTotal(String stateDetail) {
        int state = parseState(stateDetail);
        if (state==1) {
            String s = stateDetail.substring(1, stateDetail.length()-1);
            return Integer.parseInt(s);
        } else {
            return 0;
        }
    }

    public static String parseJs(String s) {
        int n = s.length();
        int l = -1;
        int r = -1;
        for (int i=0; i<s.length(); i++) {
            if (l==-1 && s.charAt(i)=='{') {
                l = i;
            }
            if (s.charAt(i)==';') {
                r = i;
                break;
            }
        }
        return s.substring(l, r);
    }

    public static int parseEpisodeName(String episodeName) {
        int ans = 0;
        for (int i=0; i<episodeName.length(); i++) {
            if (episodeName.charAt(i)<='9'&&episodeName.charAt(i)>='0') {
                while (i<episodeName.length() && episodeName.charAt(i)<='9'&&episodeName.charAt(i)>='0') {
                    ans *= 10;
                    ans+=episodeName.charAt(i)-'0';
                    i++;
                }
                break;
            }
        }
        return ans;
    }

    /**
     * 多线程下载
     */
    public static <T> void multithreadDownload(int threadCnt, List<T> data, List<String> aids, Class<? extends DownLoadTask> taskClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCnt);
        int n = aids.size();
        int bucketSize = n/threadCnt;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i=0; i<n; i+=bucketSize) {
            Constructor<? extends DownLoadTask> constructor = taskClass.getConstructor(int.class, int.class, List.class, List.class);
            DownLoadTask downLoadTask = constructor.newInstance(i, Math.min(i + bucketSize, n), data, aids);
            CompletableFuture<Void> future = CompletableFuture.runAsync(downLoadTask, threadPool);
            futureList.add(future);
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        all.join();
    }
}
