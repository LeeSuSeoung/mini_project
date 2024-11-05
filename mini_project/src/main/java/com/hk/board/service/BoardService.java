package com.hk.board.service;

import java.io.IOException;
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

    // 글 추가, 파일 업로드 및 파일 정보 추가
    @Transactional(rollbackFor = Exception.class)
    public void insertBoard(InsertBoardCommand insertBoardCommand, MultipartRequest multipartRequest,
                            HttpServletRequest request) throws IllegalStateException, IOException {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(insertBoardCommand.getId());
        boardDto.setTitle(insertBoardCommand.getTitle());
        boardDto.setContent(insertBoardCommand.getContent());
        boardDto.setRegion(insertBoardCommand.getRegion());

        boardMapper.insertBoard(boardDto);

        if (!multipartRequest.getFiles("filename").get(0).isEmpty()) {
            String filepath = request.getSession().getServletContext().getRealPath("upload");
            List<FileBoardDto> uploadFileList = fileService.uploadFiles(filepath, multipartRequest);
            for (FileBoardDto fDto : uploadFileList) {
                fileMapper.insertFileBoard(new FileBoardDto(0, boardDto.getBoard_seq(),
                        fDto.getOrigin_filename(), fDto.getStored_filename()));
            }
        }
    }

    // 상세 내용 조회
    public BoardDto getBoard(int board_seq) {
        return boardMapper.getBoard(board_seq);
    }

    // 수정하기
    public boolean updateBoard(UpdateBoardCommand updateBoardCommand) {
        BoardDto dto = new BoardDto();
        dto.setBoard_seq(updateBoardCommand.getBoard_seq());
        dto.setTitle(updateBoardCommand.getTitle());
        dto.setContent(updateBoardCommand.getContent());
        dto.setRegion(updateBoardCommand.getRegion()); // region 설정 추가
        return boardMapper.updateBoard(dto);
    }


    // 여러 게시글 삭제
    public boolean mulDel(String[] seqs) {
        return boardMapper.mulDel(seqs);
    }

    // 특정 사용자가 게시글 소유자인지 확인
    public boolean checkOwnership(String[] seqs, String userId) {
        for (String seq : seqs) {
            BoardDto dto = boardMapper.getBoard(Integer.parseInt(seq));
            if (!dto.getId().equals(userId)) {
                return false;
            }
        }
        return true;
    }
}