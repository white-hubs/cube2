package com.guhecloud.cube.srv.gen.library.management.server.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowRecordResult {
    private Long id;
    private String borrowCardNumber; // 借阅证号
    private String bookName;
    private LocalDateTime borrowTime; // 借阅时间
    private LocalDateTime shouldReturnTime; // 应还时间
    private LocalDateTime returnTime; // 实际还书时间
    private String bookAuthor; // 图书作者
    private String publishingHouse; // 出版社
    private String isbnNumber; // ISBN号
    private String status; // 借阅状态
}
