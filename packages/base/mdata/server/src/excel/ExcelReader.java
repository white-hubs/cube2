package com.guhecloud.cube.biz.mdata.server.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.guhecloud.cube.biz.mdata.server.excel.ExcelTools.importSheet;

@Slf4j
public class ExcelReader<T> implements AutoCloseable {

    /**
     * 读取并行度<br/>
     * 默认为1，即单线程消费
     */
    private int parallelism;

    /**
     * 是否自动关闭输入流
     */
    private boolean autClose;

    /**
     * 同步等待消费完毕
     */
    private boolean synWaitConsumerDown;

    /**
     * 读取分页数,仅作为提示
     */
    private int pageSize;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    /**
     * 读取的输入流
     */
    private InputStream inputStream;

    /**
     * Excel模型类,依次有序
     */
    private Class<T> modelClz;

    /**
     * Excel模型类消费者
     */
    private Consumer<List<T>> modelConsumers;

    /**
     * 下拉数据项提供者<br/>
     * key: {@link ExcelColumn#selectionType()}
     * val: ExcelColumnSelectionProvider
     */
    private Map<String, ExcelColumnSelectionProvider> selectionProviders;

    /**
     * 数据读取完成回调
     */
    private Callback readDownCallback;

    /**
     * 数据消费完毕后回调
     */
    private Callback consumerDownCallback;

    /// reader内部维护的属性
    private CountDownLatch completeDown;//用于支持Read-Wait方法
    private BlockingDeque<T> parsedModelQueue; //已经解析完毕的数据队列
    private AtomicBoolean parsedModelComplete;//解析完毕标志
    private Semaphore consumerPermitsThread;//数据消费并行度控制
    private AtomicBoolean exit; //退出标志
    private List<String> parseErrorMsg;//解析失败的消息
    private volatile long rowCount;//统计行数

    /// reader内部维护的属性

    public static <T> ExelReaderBuilder<T> builder() {
        return new ExelReaderBuilder<>();
    }

    public void doRead() {
        Thread dispatcherLoopThread = new Thread(this::dispatcherModelData);//派发消费任务线程
        Thread readLoopThread = new Thread(this::readModelData);//读取线程
        parsedModelQueue = new LinkedBlockingDeque<>(2000);
        parsedModelComplete = new AtomicBoolean(false);
        consumerPermitsThread = new Semaphore(parallelism);
        exit = new AtomicBoolean(false);
        completeDown = new CountDownLatch(1);
        rowCount = 0;

        dispatcherLoopThread.start();//启动任务分发线程
        readLoopThread.start();//启动excel读线程

        //支持read同步阻塞
        if (synWaitConsumerDown) {
            try {
                completeDown.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 获取已经解析完的错误消息<br/>
     *
     * @return 未完成解析返回empty-list 反之返回errMsg-list
     */
    public List<String> fetchParseErrorMsgIfParsed() {
        if (parsedModelComplete.get()) {
            return parseErrorMsg;
        }

        return Collections.emptyList();
    }

    /**
     * 整体任务是否完成
     *
     * @return 解析&消费是否完成
     */
    public boolean isComplete() {
        return parsedModelComplete.get()
               && parsedModelQueue.isEmpty()
               && consumerPermitsThread.availablePermits() == parallelism;
    }

    /**
     * 返回已读取的行数
     *
     * @return size
     */
    public long fetchCurtReadRowSize() {
        return rowCount;
    }

    private void readModelData() {
        try {
            Workbook wb = new XSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(0);

            parseErrorMsg = importSheet(wb, sheet, modelClz,
                                        parsedModelQueue, parsedModelComplete,
                                        readDownCallback, selectionProviders);
        } catch (Exception e) {
            log.error("read excel fail!", e);
        }
    }

    /**
     * 派发消费任务
     */
    private void dispatcherModelData() {
        while (!exit.get()) {
            try {
                List<T> objs = new LinkedList<>();

                //塞满每页
                while ((objs.size() < pageSize) && (!parsedModelQueue.isEmpty())) {
                    T e = parsedModelQueue.poll(50, TimeUnit.MILLISECONDS);
                    if (Objects.nonNull(e)) {
                        objs.add(e);
                    }
                }

                //获取许可并分发
                if (!objs.isEmpty()) {
                    consumerPermitsThread.acquire();
                    rowCount += objs.size();//总行数累加
                    executorService.submit(() -> doReadDownCallback(objs));
                }

                //解析并消费完毕
                if (this.isComplete()) {
                    exit.set(true);
                }
            } catch (InterruptedException interruptedException) {
                exit.set(true);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("派发消费任务异常", e);
            }
        }

        //等待所有任务执行完毕后退出并回调
        try {
            if (Objects.nonNull(consumerDownCallback)) {
                consumerDownCallback.callback();
            }
        } catch (Exception e) {
            log.error("excel consumer down callback invoke fail!", e);
        }

        completeDown.countDown();
    }

    //消费
    private void doReadDownCallback(List<T> models) {
        try {
            if (Objects.nonNull(modelConsumers)) {
                modelConsumers.accept(models);
            } else {
                log.warn("没有为excelReader添加消费者,数据丢失:{}", models);
            }
        } finally {
            consumerPermitsThread.release();
        }
    }

    @Override
    public void close() throws Exception {
        //退出标志
        exit.set(true);

        //关闭输入流
        if (autClose && Objects.nonNull(inputStream)) {
            inputStream.close();
        }

        //关闭线程池
        if (Objects.nonNull(executorService)) {
            executorService.shutdown();
        }
    }

    public static class ExelReaderBuilder<T> {
        private int parallelism;

        /**
         * 是否自动关闭输入流
         */
        private boolean autClose = true;

        /**
         * 同步等待消费完毕
         */
        private boolean synWaitConsumerDown;

        /**
         * 读取分页数
         */
        private int pageSize;

        /**
         * 线程池
         */
        private ExecutorService executorService;

        /**
         * 读取的输入流
         */
        private InputStream inputStream;

        /**
         * Excel模型类,依次有序
         */
        private Class<T> modelClz;

        /**
         * Excel模型类消费者
         */
        private Consumer<List<T>> modelConsumers;

        /**
         * 下拉数据项提供者<br/>
         * key: {@link ExcelColumn#selectionType()}
         * val: ExcelColumnSelectionProvider
         */
        private Map<String, ExcelColumnSelectionProvider> selectionProviders;

        /**
         * 数据读取完成回调
         */
        private Callback readDownCallback;

        /**
         * 数据消费完毕后回调
         */
        private Callback consumerDownCallback;

        public ExelReaderBuilder<T> synWaitConsumerDown(boolean synWait) {
            synWaitConsumerDown = synWait;
            return this;
        }

        public ExelReaderBuilder<T> readDownCallback(Callback readDownCallback) {
            this.readDownCallback = readDownCallback;
            return this;
        }

        public ExelReaderBuilder<T> consumerDownCallback(Callback consumerDownCallback) {
            this.consumerDownCallback = consumerDownCallback;
            return this;
        }

        public ExelReaderBuilder<T> selectionProviders(Map<String, ExcelColumnSelectionProvider> selectionProviders) {
            this.selectionProviders = selectionProviders;
            return this;
        }

        public ExelReaderBuilder<T> modelConsumers(Consumer<List<T>> modelConsumers) {
            this.modelConsumers = modelConsumers;
            return this;
        }

        public ExelReaderBuilder<T> parallelism(int parallelism) {
            this.parallelism = parallelism;
            return this;
        }

        public ExelReaderBuilder<T> autClose(boolean autClose) {
            this.autClose = autClose;
            return this;
        }

        public ExelReaderBuilder<T> pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public ExelReaderBuilder<T> executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public ExelReaderBuilder<T> inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public ExelReaderBuilder<T> modelClz(Class<T> modelClz) {
            this.modelClz = modelClz;
            return this;
        }

        public ExcelReader<T> build() {
            ExcelReader<T> excelReader = new ExcelReader<>();
            excelReader.parallelism = parallelism;
            excelReader.autClose = autClose;
            excelReader.pageSize = pageSize;
            excelReader.executorService = executorService;
            excelReader.inputStream = inputStream;
            excelReader.modelClz = modelClz;
            excelReader.modelConsumers = modelConsumers;
            excelReader.selectionProviders = selectionProviders;
            excelReader.readDownCallback = readDownCallback;
            excelReader.consumerDownCallback = consumerDownCallback;
            excelReader.synWaitConsumerDown = synWaitConsumerDown;

            //io密集
            if (parallelism == 0) {
                excelReader.parallelism = Runtime.getRuntime().availableProcessors() * 2;
            }

            //构建线程池
            if (Objects.isNull(executorService)) {
                executorService = Executors.newFixedThreadPool(excelReader.parallelism);
                excelReader.executorService = executorService;
            }

            if (Objects.isNull(modelClz)) {
                throw new IllegalArgumentException("Excel模型类不能为空");
            }

            if (Objects.isNull(modelConsumers)) {
                throw new IllegalArgumentException("Excel模型类消费者不能为空");
            }

            return excelReader;
        }
    }

}
