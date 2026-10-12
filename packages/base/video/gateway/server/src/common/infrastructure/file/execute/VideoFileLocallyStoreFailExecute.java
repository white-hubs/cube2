package com.guhecloud.cube.base.videogw.server.common.infrastructure.file.execute;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.task.VideoFileLocallyStoreTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 视频文件本次存储错误重试的线程池
 *
 * @author lanchun
 * @date 2024/12/2 15:07
 */
@Slf4j
public class VideoFileLocallyStoreFailExecute {

    private static final BlockingQueue<VideoFilePO> blockingQueue = new LinkedBlockingQueue<>(100);

    /**
     * 内置线程池，后续用户可以指定自己的线程池
     */
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            2,
            //maximumPoolSize: 池中允许的最大的线程数
            10,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingQueue<>(101),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("videoGwFileTransferFailRetry-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    public static BlockingQueue<VideoFilePO> getQueue() {
        return blockingQueue;
    }

    public static Boolean queueIsEmpty() {
        return blockingQueue.isEmpty();
    }

    public static Boolean addQueueTask(VideoFilePO videoFilePO) {
        boolean offerSuccess = blockingQueue.offer(videoFilePO);
        if (!offerSuccess) {
            log.warn("缓存区对列已满");
            return false;
        }
        return true;
    }

    public static void executeTask(VideoFileLocallyStoreTask videoFileLocallyStoreTask) {
        executor.submit(videoFileLocallyStoreTask);
    }
}
