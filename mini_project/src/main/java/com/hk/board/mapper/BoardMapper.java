package com.hk.board.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.hk.board.dtos.BoardDto;

@Mapper
public interface BoardMapper {

    // 글 목록 조회
    List<BoardDto> getAllList();

    // 글 상세 조회
    BoardDto getBoard(int board_seq);

    // 글 추가
    boolean insertBoard(BoardDto dto);

    // 댓글 조회 메소드
    List<BoardDto> getCommentsByParentId(int parentId);

    // 글 수정
    boolean updateBoard(BoardDto dto);

    // 글 삭제
    boolean mulDel(String[] seqs);

    // 지역별 게시글 조회
    List<BoardDto> getListByRegion(String region);
}
