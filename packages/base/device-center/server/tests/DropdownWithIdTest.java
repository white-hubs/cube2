package com.guhecloud.cube.base.device.center;

import com.guhecloud.cube.base.device.center.anotation.*;
import com.guhecloud.cube.base.device.center.core.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.base.device.center.core.excel.ExcelReader;
import com.guhecloud.cube.base.device.center.core.excel.ExcelWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class DropdownWithIdTest {

    @Test
    void testGenerateTemplate() throws Exception {
        Map<String, ExcelColumnSelectionProvider> map = new HashMap<>();
        map.put("province", new ProvinceSelectionProvider());
        map.put("jobType", new JobTypeSelectionProvider());
        map.put("gender", new GenderSelectionProvider());
        map.put("city", new CitySelectionProvider());

        List<DemoUserExcelModel> userData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoUserExcelModel user = new DemoUserExcelModel();
            user.setUserCode("userId" + i);
            user.setUserName("userName" + i);
            user.setGender(new Random().nextInt(2) + "");
            user.setJobType("01");
            user.setHomeProvince("110000");
            user.setHomeCity("110100");
            userData.add(user);
        }

        ExcelWriter<DemoUserExcelModel> excelWriter = new ExcelWriter<>();
        byte[] data = excelWriter.writeHeaderToOutputStream(map, DemoUserExcelModel.class, userData);

        BlockingQueue<DemoUserExcelModel> demoUserExcelModels = new LinkedBlockingDeque<>();
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            ExcelReader<DemoUserExcelModel> excelReader = ExcelReader.<DemoUserExcelModel>builder()
                    .inputStream(in)
                    .executorService(Executors.newFixedThreadPool(10))
                    .parallelism(5)
                    .pageSize(2)
                    .consumerDownCallback(() -> System.out.println("消费完毕"))
                    .readDownCallback(() -> System.out.println("读取完毕"))
                    .modelConsumers(users -> users.forEach(demoUserExcelModels::offer))
                    .modelClz(DemoUserExcelModel.class)
                    .selectionProviders(map)
                    .build();

            excelReader.doRead();
        }

        TimeUnit.SECONDS.sleep(1);
        Assertions.assertEquals(10, demoUserExcelModels.size());

        List<DemoUserExcelModel> demoUserExcelModelList = new ArrayList<>(11);
        System.out.println(demoUserExcelModels.drainTo(demoUserExcelModelList));
        demoUserExcelModelList.forEach(System.out::println);
    }
}
