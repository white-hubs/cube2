package com.guhecloud.cube.iot.mgt.device.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.guhecloud.cube.iot.mgt.comm.excel.ExcelReader;
import com.guhecloud.cube.iot.mgt.comm.excel.ExcelWriter;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.device.param.BatchCreateDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.BatchSyncDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParamNew;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.cube.iot.mgt.device.vo.BatchOptResult;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;
import static com.guhecloud.cube.iot.mgt.comm.thread.ThreadUtil.DEFAULT_THREAD_SIZE;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/device/mgt/batch")
public class DeviceBatchController {

    @Resource
    private DevDeviceService devDeviceService;
    @Resource(name = "iotCommonPool")
    private ExecutorService executorService;

    /**
     * 构建一个读取监听器<br/>
     * 用于将读取到的数据放入阻塞队列中
     *
     * @param parentId           父设备id
     * @param productId          产品id
     * @param blockingDeque      阻塞队列
     * @param excelParseComplete excel解析完毕后进行countDown
     *
     * @return ReadListener
     */
    @Deprecated
    private static ReadListener<DeviceCreateParam> buildReadListener(Long parentId,
                                                                     Long productId,
                                                                     BlockingDeque<DeviceCreateParam> blockingDeque,
                                                                     CountDownLatch excelParseComplete
                                                                    ) {
        return new PageReadListener<DeviceCreateParam>(deviceCreateParams -> {
            for (DeviceCreateParam deviceCreateParam : deviceCreateParams) {
                //fill param
                deviceCreateParam.setParentDeviceId(parentId);
                deviceCreateParam.setProductId(productId);
                deviceCreateParam.setDeviceType(1);

                try {
                    blockingDeque.put(deviceCreateParam);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }) {
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                excelParseComplete.countDown();
            }
        };
    }

    @GetMapping("/export-excel-template")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + "批量导入模板.xlsx");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");

        ExcelWriter<DeviceCreateParamNew> writer = new ExcelWriter<>();
        writer.writeData(Collections.emptyList(), Collections.emptyMap(), DeviceCreateParamNew.class, response.getOutputStream(), false);

        response.flushBuffer();
        log.info("exportExcel");
    }

    @PostMapping("/cache-syn")
    public BatchOptResult synDeviceByCache(@RequestBody BatchSyncDeviceParam syncDeviceParam) {
        List<BatchSyncDeviceParam.SynDeviceInfo> synDevices = syncDeviceParam.getSynDevices();
        Assert.notEmpty(synDevices, "要同步的设备信息不能为空");
        List<String> errorMsg = devDeviceService.synDeviceByCache(syncDeviceParam);
        //信息汇总
        BatchOptResult batchOptResult = new BatchOptResult();
        batchOptResult.setErrorMsg(errorMsg);
        batchOptResult.setSuccess(synDevices.size() - errorMsg.size());
        batchOptResult.setFail(errorMsg.size());
        return batchOptResult;
    }

    @PostMapping("/import-excel-batch-new")
    public BatchOptResult importExcelNew(BatchCreateDeviceParam batchCreateDeviceParam) throws IOException, InterruptedException {
        Requester requester = SessionContextHolder.getRequester();
        Principal principal = SecurityContextHolder.getPrincipal();
        Long parentId = batchCreateDeviceParam.getParentDeviceId();
        Long productId = Long.valueOf(batchCreateDeviceParam.getProductId());

        List<String> saveModelErrorMsg = new CopyOnWriteArrayList<>();
        List<String> parsedErrorMsg = new ArrayList<>();//解析失败的消息
        long rowSize = 0;//总行数

        try (ExcelReader<DeviceCreateParamNew> excelReader = ExcelReader.<DeviceCreateParamNew>builder()
                .selectionProviders(Collections.emptyMap())
                .autClose(true)
                .pageSize(10)
                .modelClz(DeviceCreateParamNew.class)
                .inputStream(batchCreateDeviceParam.getExcel().getInputStream())
                .synWaitConsumerDown(true)
                .modelConsumers(modelList -> {
                    SessionContextHolder.setRequester(requester);//租户信息
                    SecurityContextHolder.setPrincipal(principal);//设置用户信息
                    saveModelErrorMsg.addAll(devDeviceService.batchSaveDevice(modelList, parentId, productId));//批量保存
                }).build()) {

            //等待读取
            excelReader.doRead();

            //获取解析错误的消息
            parsedErrorMsg = excelReader.fetchParseErrorMsgIfParsed();
            rowSize = excelReader.fetchCurtReadRowSize();
        } catch (Exception e) {
            log.error("close excel Reader fail", e);

            BatchOptResult batchOptResult = new BatchOptResult();
            batchOptResult.setErrorMsg(Collections.singletonList("导入失败，原因：请根据设备模板样式上传"));
            batchOptResult.setSuccess(0);
            batchOptResult.setFail(0);
            return batchOptResult;
        }

        parsedErrorMsg.addAll(saveModelErrorMsg);

        //信息汇总
        BatchOptResult batchOptResult = new BatchOptResult();
        batchOptResult.setErrorMsg(parsedErrorMsg);
        batchOptResult.setSuccess(rowSize - parsedErrorMsg.size());
        batchOptResult.setFail(parsedErrorMsg.size());
        return batchOptResult;
    }

    @PostMapping("/import-excel-batch")
    @Deprecated
    public List<String> importExcel(BatchCreateDeviceParam batchCreateDeviceParam) throws IOException, InterruptedException {
        Integer tenantId = SessionContextHolder.getRequester().getTenant();
        Long parentId = batchCreateDeviceParam.getParentDeviceId();
        Long productId = Long.valueOf(batchCreateDeviceParam.getProductId());

        CountDownLatch excelParseComplete = new CountDownLatch(1);
        BlockingDeque<String> errorList = new LinkedBlockingDeque<>();
        BlockingDeque<DeviceCreateParam> blockingDeque = new LinkedBlockingDeque<>();
        AtomicBoolean stopEventLoop = new AtomicBoolean(false);

        try {
            EasyExcelFactory.read(batchCreateDeviceParam.getExcel().getInputStream(),
                                  DeviceCreateParam.class,
                                  buildReadListener(parentId, productId, blockingDeque, excelParseComplete))
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            log.error("导入错误", e);
            return Collections.singletonList("导入失败，原因：请根据设备模板样式上传");
        }

        //创建一个任务分发队列，将数据分发到各个线程
        buildEventLoop(blockingDeque, errorList, tenantId, stopEventLoop);

        //wait
        excelParseComplete.await();

        //等待所有任务执行完毕后退出
        while (!blockingDeque.isEmpty()) {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        //stop eventLoop
        stopEventLoop.set(true);

        //copy to list
        List<String> errList = new LinkedList<>();
        errorList.drainTo(errList);
        return errList;
    }

    @Deprecated
    private void buildEventLoop(BlockingDeque<DeviceCreateParam> blockingDeque,
                                BlockingDeque<String> errorList,
                                Integer tenantId,
                                AtomicBoolean stopEventLoop
                               ) {
        Semaphore permitsThread = new Semaphore(DEFAULT_THREAD_SIZE - 2);

        Thread eventLoop = new Thread(() -> {
            while (!stopEventLoop.get()) {
                DeviceCreateParam deviceCreateParam = null;

                try {
                    //被唤醒后直接退出
                    deviceCreateParam = blockingDeque.take();
                    permitsThread.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                executorService.submit(createDeviceSave(tenantId, deviceCreateParam, errorList, permitsThread));
            }
        });
        eventLoop.start();
    }

    @Deprecated
    private Runnable createDeviceSave(Integer tenantId, DeviceCreateParam deviceCreateParam, BlockingDeque<String> errorList,
                                      Semaphore permitsThread
                                     ) {
        return () -> {
            SessionContextHolder.requestTenant(tenantId);

            try {
                devDeviceService.saveDevice(deviceCreateParam);
            } catch (Exception e) {
                try {
                    errorList.put("导入【" + deviceCreateParam.getDeviceName() + "-" + deviceCreateParam.getDeviceNumber() + "】失败，原因：" +
                                  e.getMessage());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                permitsThread.release();
            }
        };
    }
}
