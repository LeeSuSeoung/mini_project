package com.hk.board.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartRequest;

import com.hk.board.command.InsertBoardCommand;
import com.hk.board.command.UpdateBoardCommand;
import com.hk.board.dtos.BoardDto; 
import com.hk.board.dtos.FileBoardDto;
import com.hk.board.mapper.BoardMapper;
import com.hk.board.mapper.FileMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BoardService {
    
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileService fileService;

    // 글 목록 조회
    public List<BoardDto> getAllList() {
        return boardMapper.getAllList();
    }

    // 특정 지역의 게시글 목록 조회
    public List<BoardDto> getListByRegion(String region) {
        return boardMapper.getListByRegion(region);
    }

    // 글 추가, 파일업로드 및 파일정보 추가
    @Transactional(rollbackFor = Exception.class)
    public void insertBoard(InsertBoardCommand insertBoardCommand,
                            MultipartRequest multipartRequest,
                            HttpServletRequest request) 
                            throws IllegalStateException, IOException {
        // command:UI -> dto:DB 데이터 옮겨담기
        BoardDto boardDto = new BoardDto();
        boardDto.setId(insertBoardCommand.getId());
        boardDto.setTitle(insertBoardCommand.getTitle());
        boardDto.setContent(insertBoardCommand.getContent());
        boardDto.setRegion(insertBoardCommand.getRegion()); // region 설정 추가

        // 새글을 추가할때 파라미터로 전달된 boardDto객체에 자동으로,
        // 증가된 board_seq값이 저장
        boardMapper.insertBoard(boardDto); // 새글 추가

        // 첨부된 파일들이 있는 경우
        if (!multipartRequest.getFiles("filename").get(0).isEmpty()) {
            // 파일 저장 경로 설정: 절대 경로, 상대 경로
            String filepath = request.getSession().getServletContext().getRealPath("upload");
            // 파일 업로드 작업은 FileService쪽에서 업로드하고 업로드된 파일 정보 반환
            List<FileBoardDto> uploadFileList = fileService.uploadFiles(filepath, multipartRequest);
            // 파일 정보 DB에 추가
            for (FileBoardDto fDto : uploadFileList) {
                fileMapper.insertFileBoard(new FileBoardDto(0, boardDto.getBoard_seq(), // 증가된 board_seq값을 넣는다 
                                                             fDto.getOrigin_filename(),
                                                             fDto.getStored_filename()));
            }
        }
    } 

    // 상세내용 조회
    public BoardDto getBoard(int board_seq) {
        return boardMapper.getBoard(board_seq);
    }
    
    // 수정하기
    public boolean updateBoard(UpdateBoardCommand updateBoardCommand) {
        // command:UI ---> DTO:DB 
        BoardDto dto = new BoardDto();
        dto.setBoard_seq(updateBoardCommand.getBoard_seq());
        dto.setTitle(updateBoardCommand.getTitle());
        dto.setContent(updateBoardCommand.getContent());
        return boardMapper.updateBoard(dto);
    }

    // 여러 게시글 삭제
    public boolean mulDel(String[] seqs) {
        return boardMapper.mulDel(seqs);
    }
}
