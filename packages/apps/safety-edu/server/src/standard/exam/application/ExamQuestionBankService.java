package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.util.SelectSheetWriteHandler;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.BatchOptResultVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamQuestionBankQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.contant.ImportConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamQuestionBankPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel.CustomRowHeightStrategy;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel.ExamQuestionBankImportListener;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel.ExcelTitleHandler;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamQuestionBankMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankImportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel.ExamQuestionBankImportListener.IMPORT_DATA_CACHE;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ExamQuestionBankService extends ServiceImpl<ExamQuestionBankMapper, ExamQuestionBankPO> {
    private final ThemeService themeService;

    public Page<ExamQuestionBankVO> getExamQuestionBankPage(ExamQuestionBankQuery examQuestionBankQuery) {
        return this.baseMapper.selectExamQuestionBankPage(PagingUtils.convert(examQuestionBankQuery), examQuestionBankQuery);
    }

    /**
     * 批量导入题库
     */

    /**
     * 数据写入
     *
     * @param importVOList
     * @param excelName
     * @param response
     */
    public void writeTemplate(List<ExamQuestionBankImportVO> importVOList, String excelName, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode;
        try {
            fileNameEncode = URLEncoder.encode(excelName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        //下拉框
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        List<String> themeNameList = themeService.list().stream().map(ThemePO::getName).distinct().collect(Collectors.toList());
        List<String> questionTypeList = Arrays.stream(ExamQuestionTypeEnum.values()).map(ExamQuestionTypeEnum::getDescription)
                .collect(Collectors.toList());
        //主题类型
        dropdownData.put(2, themeNameList);
        //题目类型
        dropdownData.put(3, questionTypeList);
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new ExcelTitleHandler(ImportConstant.TIP_CONTENT))
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData, 10))
                    .registerWriteHandler(getHorizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomRowHeightStrategy())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(ExamQuestionBankImportVO.class)
                    .sheet(excelName)
                    .doWrite(importVOList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置表头样式策略
     *
     * @return
     */
    private static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        // 头的样式策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 15);
        headWriteFont.setColor(IndexedColors.RED.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 15);
        contentWriteFont.setBold(false);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 考题数据导入
     *
     * @param file 导入excel
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BatchOptResultVO importExamQuestionBank(MultipartFile file) {
        List<String> errorList = new ArrayList<>();
        List<ExamQuestionBankImportVO> importVOList = this.readExcel(file);
        if (CollectionUtils.isEmpty(importVOList)) {
            errorList.add("无法导入空的excel");
            return BatchOptResultVO.builder()
                    .success(0)
                    .fail(0)
                    .errorMsg(errorList)
                    .build();
        }
        List<ExamQuestionBankPO> examQuestionBankPOList = new ArrayList<>();

        Map<String, String> themeNameMap = new HashMap<>();
        for (int i = 0; i < importVOList.size(); i++) {
            ExamQuestionBankImportVO importVO = importVOList.get(i);
            if (!validParam(i, importVO, errorList)) {
                continue;
            }
            themeNameMap.putIfAbsent(importVO.getName(), importVO.getThemeName());
            examQuestionBankPOList.add(this.buildExamQuestionBankPO(importVO));
        }
        Map<String, Long> themeIdByNameMap = new HashMap<>();
        if (MapUtils.isNotEmpty(themeNameMap)) {
            themeIdByNameMap = themeService.lambdaQuery()
                    .in(ThemePO::getName, themeNameMap.values().stream().distinct().collect(Collectors.toList()))
                    .list()
                    .stream().collect(Collectors.toMap(ThemePO::getName, ThemePO::getId));
        }
        Map<String, Long> finalThemeIdByNameMap = themeIdByNameMap;
        examQuestionBankPOList.forEach(po -> {
            String themeName = themeNameMap.get(po.getName());
            if (StringUtils.isNotBlank(themeName)) {
                po.setThemeId(finalThemeIdByNameMap.get(themeName));
            }
        });
        this.saveBatch(examQuestionBankPOList);
        //返回失败列表
        List<ExamQuestionBankImportVO> failImportData = importVOList.stream()
                .filter(t -> !t.isValidFail()).collect(Collectors.toList());
        return BatchOptResultVO.builder()
                .success(importVOList.size() - errorList.size())
                .fail(errorList.size())
                .errorMsg(errorList)
                .importErrorList(failImportData)
                .build();
    }

    /**
     * 构造导入参数
     *
     * @param importVO
     */
    private ExamQuestionBankPO buildExamQuestionBankPO(ExamQuestionBankImportVO importVO) {
        ExamQuestionBankPO po = ObjectUtils.copying(importVO, ExamQuestionBankPO.class);
        po.setQuestionContent(JsonUtils.serialize(importVO.getOptions()));
        po.setQuestionType(ExamQuestionTypeEnum.getQuestionType(importVO.getQuestionTypeDesc()));
        po.setScore(Float.parseFloat(importVO.getScoreStr()));
        return po;
    }

    /**
     * 参数校验
     *
     * @param i
     * @param importVO
     * @param errorList
     *
     * @return
     */
    private boolean validParam(int i, ExamQuestionBankImportVO importVO, List<String> errorList) {
        if (StringUtils.isEmpty(importVO.getName())) {
            errorList.add("第" + (i + 1) + "行，第1列，题目名称不能为空");
            return false;
        }
        if (importVO.getName().length() > 1000) {
            errorList.add("题目名称不能大于1000");
            return false;
        }
        if (StringUtils.isEmpty(importVO.getScoreStr())) {
            errorList.add("第" + (i + 1) + "行，第2列，分数不能为空");
            return false;
        }
        if (StringUtils.isEmpty(importVO.getThemeName())) {
            errorList.add("第" + (i + 1) + "行，第3列，题目主题不能为空");
            return false;
        }
        if (StringUtils.isEmpty(importVO.getQuestionTypeDesc())) {
            errorList.add("第" + (i + 1) + "行，第4列，题目类型不能为空");
            return false;
        }
        if (StringUtils.isEmpty(importVO.getRightAnswer())) {
            errorList.add("第" + (i + 1) + "行，第5列，正确答案不能为空");
            return false;
        }
        String[] rightAnswers = importVO.getRightAnswer().split(",");
        for (String rightAnswer : rightAnswers) {
            if (!rightAnswer.matches("[A-Z]")) {
                errorList.add("第" + (i + 1) + "行，第5列，正确答案只能为大写英文字母");
                return false;
            }
        }
        if (StringUtils.isNotBlank(importVO.getAnswerAnalysis())
            && importVO.getAnswerAnalysis().length() > 200) {
            errorList.add("第" + (i + 1) + "行,答案解析不能大于200");
            return false;
        }
        if (CollectionUtils.isEmpty(importVO.getOptions())) {
            errorList.add("第" + (i + 1) + "行，选项不能为空");
            return false;
        }
        if (ExamQuestionTypeEnum.JUDGE.getDescription().equals(importVO.getQuestionTypeDesc())
            && importVO.getOptions().size() != 2) {
            errorList.add("第" + (i + 1) + "行,判断题只能有两个选项");
            return false;
        }
        importVO.setValidFail(true);
        return true;
    }

    /**
     * 读取Excel文件
     *
     * @param file
     *
     * @return
     */
    private List<ExamQuestionBankImportVO> readExcel(MultipartFile file) {
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("考题导入读取excel失败{}", e);
            throw new ServiceException("文件读取失败");
        }
        // 读取第一个sheet的数据
        reader.sheet(0)
                .registerReadListener(new ExamQuestionBankImportListener())
                .doReadSync();
        return IMPORT_DATA_CACHE.get();
    }
}
