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

    public List<BoardDto> getAllList() {
        List<BoardDto> boardList = boardMapper.getAllList();
        for (BoardDto board : boardList) {
            // 부모 ID가 없는 경우만 댓글을 가져와 설정합니다.
            if (board.getParent_id() == null) {
                List<BoardDto> replies = boardMapper.getCommentsByParentId(board.getBoard_seq());
                board.setReplies(replies);
            }
        }
        return boardList;
    }

    public List<BoardDto> getListByRegion(String region) {
        List<BoardDto> boardList = boardMapper.getListByRegion(region);
        for (BoardDto board : boardList) {
            // 부모 ID가 없는 경우만 댓글을 가져와 설정합니다.
            if (board.getParent_id() == null) {
                List<BoardDto> replies = boardMapper.getCommentsByParentId(board.getBoard_seq());
                board.setReplies(replies);
            }
        }
        return boardList;
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertBoard(InsertBoardCommand insertBoardCommand, MultipartRequest multipartRequest,
                            HttpServletRequest request) throws IllegalStateException, IOException {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(insertBoardCommand.getId());
        boardDto.setTitle(insertBoardCommand.getTitle());
        boardDto.setContent(insertBoardCommand.getContent());
        boardDto.setRegion(insertBoardCommand.getRegion());
        boardDto.setParent_id(insertBoardCommand.getParentId());
        boardDto.setDelflag("N");

        boardMapper.insertBoard(boardDto);

        if (!multipartRequest.getFiles("filename").isEmpty()) {
            String filepath = request.getSession().getServletContext().getRealPath("upload");
            List<FileBoardDto> uploadFileList = fileService.uploadFiles(filepath, multipartRequest);

            for (FileBoardDto fDto : uploadFileList) {
                fileMapper.insertFileBoard(new FileBoardDto(0, boardDto.getBoard_seq(),
                        fDto.getOrigin_filename(), fDto.getStored_filename()));
            }
        }
    }

    public BoardDto getBoard(int board_seq) {
        BoardDto board = boardMapper.getBoard(board_seq);
        List<BoardDto> replies = boardMapper.getCommentsByParentId(board_seq);
        board.setReplies(replies);
        return board;
    }

    public boolean updateBoard(UpdateBoardCommand updateBoardCommand) {
        BoardDto dto = new BoardDto();
        dto.setBoard_seq(updateBoardCommand.getBoard_seq());
        dto.setTitle(updateBoardCommand.getTitle());
        dto.setContent(updateBoardCommand.getContent());
        dto.setRegion(updateBoardCommand.getRegion());
        return boardMapper.updateBoard(dto);
    }

    public boolean mulDel(String[] seqs) {
        return boardMapper.mulDel(seqs);
    }

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
