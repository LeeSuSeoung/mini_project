package com.hk.board.command;

import jakarta.validation.constraints.NotBlank;

public class InsertBoardCommand {

    private String id; // 게시물 ID

    @NotBlank(message = "제목을 입력하세요") // 제목 필드는 비어 있을 수 없음
    private String title; // 게시물 제목

    @NotBlank(message = "내용을 입력하세요") // 내용 필드는 비어 있을 수 없음
    private String content; // 게시물 내용

    @NotBlank(message = "지역을 입력하세요") // 지역 필드는 비어 있을 수 없음
    private String region; // 게시물 지역

    private Integer parentId; // 부모 게시물 ID (답글을 위한 필드)

    // 기본 생성자
    public InsertBoardCommand() {
    }

    // 모든 필드를 포함한 생성자
    public InsertBoardCommand(String id, 
                              @NotBlank(message = "제목을 입력하세요") String title,
                              @NotBlank(message = "내용을 입력하세요") String content,
                              @NotBlank(message = "지역을 입력하세요") String region, 
                              Integer parentId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.region = region;  
        this.parentId = parentId;  
    }

    // Getter 및 Setter 메소드
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    // toString 메소드
    @Override
    public String toString() {
        return "InsertBoardCommand [id=" + id + ", title=" + title + 
               ", content=" + content + ", region=" + region + 
               ", parentId=" + parentId + "]";
    }
}
