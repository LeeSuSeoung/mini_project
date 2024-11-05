package com.hk.board.dtos;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value = "boardDto") // mapper.xml에서 사용하는 변수명 설정
public class BoardDto {
    private int board_seq;
    private String id;
    private String title;
    private String content;
    private Date regdate;
    private String delflag = "N";
    private String region; // 지역 추가

    // Join용 멤버 필드
    private List<FileBoardDto> fileBoardDto;

    public BoardDto() {
        super();
    }

    public BoardDto(int board_seq, String id, String title, String content, Date regdate, String delflag,
                    String region, List<FileBoardDto> fileBoardDto) {
        super();
        this.board_seq = board_seq;
        this.id = id;
        this.title = title;
        this.content = content;
        this.regdate = regdate;
        this.delflag = delflag;
        this.region = region; // 지역 초기화
        this.fileBoardDto = fileBoardDto;
    }

    // Getter 및 Setter는 그대로 유지
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

    public List<FileBoardDto> getFileBoardDto() {
        return fileBoardDto;
    }

    public void setFileBoardDto(List<FileBoardDto> fileBoardDto) {
        this.fileBoardDto = fileBoardDto;
    }

    @Override
    public String toString() {
        return "BoardDto [board_seq=" + board_seq + ", id=" + id + ", title=" + title + ", content=" + content
                + ", regdate=" + regdate + ", delflag=" + delflag + ", region=" + region + ", fileBoardDto=" + fileBoardDto + "]";
    }
}