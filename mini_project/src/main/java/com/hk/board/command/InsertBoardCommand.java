package com.hk.board.command;

import jakarta.validation.constraints.NotBlank;

public class InsertBoardCommand {

    private String id;
    
    @NotBlank(message = "제목을 입력하세요")
    private String title;
    
    @NotBlank(message = "내용을 입력하세요")
    private String content;

    @NotBlank(message = "지역을 입력하세요")  // 지역 필드에 대한 유효성 검사 추가
    private String region;  // 지역 필드 추가

    public InsertBoardCommand() {
        super();
    }

    public InsertBoardCommand(String id, 
                              @NotBlank(message = "제목을 입력하세요") String title,
                              @NotBlank(message = "내용을 입력하세요") String content,
                              @NotBlank(message = "지역을 입력하세요") String region) {  // 지역 필드 추가
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.region = region;  // 지역 필드 초기화
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegion() {  // 지역 필드의 getter 추가
        return region;
    }

    public void setRegion(String region) {  // 지역 필드의 setter 추가
        this.region = region;
    }

    @Override
    public String toString() {
        return "insertBoardCommand [id=" + id + ", title=" + title + 
               ", content=" + content + ", region=" + region + "]";  // 지역 필드 추가
    }
}