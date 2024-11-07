package com.hk.board.dtos;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "boardDto")
public class BoardDto {
    private int board_seq;
    private String id;
    private String title;
    private String content;
    private Date regdate;
    private String delflag = "N";
    private String region;
    private Integer parent_id;

    // 댓글을 위한 멤버 필드 추가
    private List<BoardDto> replies;

    // Join용 멤버 필드
    private List<FileBoardDto> fileBoardDto;

    public BoardDto() {
        super();
    }

    public BoardDto(int board_seq, String id, String title, String content, Date regdate, String delflag,
                    String region, Integer parent_id, List<FileBoardDto> fileBoardDto, List<BoardDto> replies) {
        super();
        this.board_seq = board_seq;
        this.id = id;
        this.title = title;
        this.content = content;
        this.regdate = regdate;
        this.delflag = delflag;
        this.region = region;
        this.parent_id = parent_id;
        this.fileBoardDto = fileBoardDto;
        this.replies = replies;
    }

    // Getter 및 Setter 메소드
    public int getBoard_seq() {
        return board_seq;
    }

    public void setBoard_seq(int board_seq) {
        this.board_seq = board_seq;
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

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public List<FileBoardDto> getFileBoardDto() {
        return fileBoardDto;
    }

    public void setFileBoardDto(List<FileBoardDto> fileBoardDto) {
        this.fileBoardDto = fileBoardDto;
    }

    // 댓글 리스트를 위한 Getter 및 Setter 메소드 추가
    public List<BoardDto> getReplies() {
        return replies;
    }

    public void setReplies(List<BoardDto> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "BoardDto [board_seq=" + board_seq + ", id=" + id + ", title=" + title + ", content=" + content
                + ", regdate=" + regdate + ", delflag=" + delflag + ", region=" + region + ", parent_id=" + parent_id
                + ", fileBoardDto=" + fileBoardDto + ", replies=" + replies + "]";
    }
}
