package com.hellcourt.dto.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class RebirthExportDTO {
    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("魂魄ID")
    private Long soulId;

    @ExcelProperty("魂魄姓名")
    @ColumnWidth(15)
    private String soulName;

    @ExcelProperty("轮回道")
    @ColumnWidth(15)
    private String pathName;

    @ExcelProperty("投胎去向")
    @ColumnWidth(25)
    private String destination;

    @ExcelProperty("投胎日期")
    @ColumnWidth(20)
    private String rebirthDate;

    @ExcelProperty("操作人")
    @ColumnWidth(15)
    private String operatorName;

    @ExcelProperty("记录时间")
    @ColumnWidth(20)
    private String createdAt;
}
