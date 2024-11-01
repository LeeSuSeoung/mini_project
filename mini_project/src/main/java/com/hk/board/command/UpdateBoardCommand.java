package com.hk.board.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateBoardCommand {

	private int board_seq;
	@NotBlank(message = "제목을 입력하세요")
	private String title;
	@NotBlank(message = "내용을 입력하세요")
	private String content;
	@NotBlank(message = "지역을 선택하세요")  // 지역 필드에 대한 유효성 검사 추가
	private String region;  // 새로운 필드 추가
	
	public UpdateBoardCommand() {
		super();
	}

	public UpdateBoardCommand(@NotNull int board_seq, @NotBlank(message = "제목을 입력하세요") String title,
			@NotBlank(message = "내용을 입력하세요") String content, 
			@NotBlank(message = "지역을 선택하세요") String region) {  // 지역 필드 추가
		super();
		this.board_seq = board_seq;
		this.title = title;
		this.content = content;
		this.region = region;  // 지역 필드 초기화
	}

	public int getBoard_seq() {
		return board_seq;
	}

	public void setBoard_seq(int board_seq) {
		this.board_seq = board_seq;
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

	public String getRegion() {  // 지역 필드 getter 추가
		return region;
	}

	public void setRegion(String region) {  // 지역 필드 setter 추가
		this.region = region;
	}

	@Override
	public String toString() {
		return "UpdateBoardCommand [board_seq=" + board_seq + ", title=" + title + ", content=" + content + ", region=" + region + "]";
	}
}
