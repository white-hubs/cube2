package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.assembly;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcDetailsPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcDetailsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.NfcImportParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.NfcDetailsService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.NfcDetailsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NFC配置
 *
 * @author : lijie
 * @date : 2023-5-25
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NfcDetailsController {
    private final NfcDetailsService nfcDetailsService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    @GetMapping("/nfc/{id}")
    public NfcDetailsVO queryById(@PathVariable("id") Long id) {
        return nfcDetailsService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    @GetMapping("/nfc/queryNcfPage")
    public Pagination<NfcDetailsVO> queryNcfPage(@Valid NfcDetailsPageParam param) {
        return nfcDetailsService.queryNcfPage(param);
    }

    /**
     * 查询所有未绑定的芯片
     *
     * @param
     *
     * @return 芯片信息
     */
    @GetMapping("/nfc/queryNcfAll")
    public List<NfcDetailsVO> queryNcfAll(Long deviceId) {
        return nfcDetailsService.queryNcfAll(deviceId);
    }

    /**
     * 新增数据
     *
     * @param param nfc信息
     *
     * @return 新增结果
     */
    @PostMapping("/nfc/insert")
    public Boolean insert(@Valid @RequestBody NfcDetailsParam param) {
        return nfcDetailsService.insert(param);
    }

    /**
     * 下载模板
     */
    @PostMapping("/nfc/download/template")
    public void downloadNfcTemplate(HttpServletResponse response) {
        try {
            log.info("----------------------开始导出nfc模板----------------------");
            Map<String, Map<String, String>> title = new HashMap<>();
            Map<String, List> data = new HashMap<>();
            Map<String, String> map = new HashMap<>();
            title.put("nfc模板", map);
            // 导出Excel文件
            ExcelUtile.uploadExcelList(response, data, title, "nfc.xlsx");
        } catch (Exception exception) {
            log.error("nfc模板导出--", exception.getLocalizedMessage());
        }

    }

    /**
     * 批量导入
     *
     * @param file excel文件
     */
    @PostMapping("/nfc/import")
    public void importDeviceFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = EasyExcel.read(file.getInputStream());
        // 读取第一个sheet的数据
        List<NfcImportParam> data = reader.sheet(0).head(NfcImportParam.class).doReadSync();
        nfcDetailsService.insert(ConvertUtils.toList(NfcDetailsParam.class, data));
    }

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    @PutMapping("/nfc/update")
    public Boolean update(@RequestBody NfcDetailsParam param) {
        return nfcDetailsService.update(param);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/nfc/delete/{id}")
    public Boolean deleteById(@PathVariable("id") Long id) {
        return nfcDetailsService.deleteById(id);
    }
}
