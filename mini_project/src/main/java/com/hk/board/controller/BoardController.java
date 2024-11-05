package com.hk.board.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartRequest;

import com.hk.board.command.DelBoardCommand;
import com.hk.board.command.InsertBoardCommand;
import com.hk.board.command.UpdateBoardCommand;
import com.hk.board.dtos.BoardDto;
import com.hk.board.dtos.MemberDto;
import com.hk.board.service.BoardService;
import com.hk.board.service.FileService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private FileService fileService;

    // 게시글 목록 조회
    @GetMapping(value = "/boardList")
    public String boardList(@RequestParam(required = false) String region, Model model) {
        System.out.println("글목록 보기");

        List<BoardDto> list;
        if (region != null && !region.isEmpty()) {
            list = boardService.getListByRegion(region);
        } else {
            list = boardService.getAllList();
        }

        model.addAttribute("list", list);
        model.addAttribute("delBoardCommand", new DelBoardCommand());
        model.addAttribute("region", region);
        return "board/boardList";
    }

    // 게시글 추가 화면 이동
    @GetMapping(value = "/boardInsert")
    public String boardInsertForm(Model model) {
        model.addAttribute("insertBoardCommand", new InsertBoardCommand());
        return "board/boardInsertForm";
    }

    // 게시글 추가 처리
    @PostMapping(value = "/boardInsert")
    public String boardInsert(@Validated InsertBoardCommand insertBoardCommand,
                              BindingResult result, MultipartRequest multipartRequest,
                              HttpServletRequest request, Model model) throws IllegalStateException, IOException {
        if (result.hasErrors()) {
            System.out.println("글을 모두 입력하세요");
            return "board/boardInsertForm";
        }

        boardService.insertBoard(insertBoardCommand, multipartRequest, request);
        return "redirect:/board/boardList";
    }

    // 게시글 상세보기
    @GetMapping(value = "/boardDetail")
    public String boardDetail(int board_seq, Model model, HttpServletRequest request) {
        BoardDto dto = boardService.getBoard(board_seq);
        
        UpdateBoardCommand updateBoardCommand = new UpdateBoardCommand();
        updateBoardCommand.setBoard_seq(dto.getBoard_seq());
        updateBoardCommand.setTitle(dto.getTitle());
        updateBoardCommand.setContent(dto.getContent());
        updateBoardCommand.setRegion(dto.getRegion());

        // 로그인한 사용자 정보를 가져옵니다
        MemberDto loggedInUser = (MemberDto) request.getSession().getAttribute("mdto");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUserId", loggedInUser.getId());
        }

        model.addAttribute("updateBoardCommand", updateBoardCommand);
        model.addAttribute("dto", dto);
        return "board/boardDetail";
    }


 // 게시글 수정 처리
    @PostMapping(value = "/boardUpdate")
    public String boardUpdate(@Validated UpdateBoardCommand updateBoardCommand, 
                              BindingResult result, 
                              HttpServletRequest request, 
                              Model model) {

        // 검증 오류가 있을 경우
        if (result.hasErrors()) {
            System.out.println("수정내용을 모두 입력하세요");
            BoardDto dto = boardService.getBoard(updateBoardCommand.getBoard_seq());
            model.addAttribute("dto", dto);
            return "board/boardDetail"; // 오류 발생 시 상세보기 페이지로 리턴
        }

        MemberDto loggedInUser = (MemberDto) request.getSession().getAttribute("mdto");
        if (loggedInUser == null) {
            return "redirect:/user/login"; // 로그인 상태 확인
        }

        // 현재 게시글 작성자와 로그인한 사용자 확인
        BoardDto boardDto = boardService.getBoard(updateBoardCommand.getBoard_seq());
        if (!boardDto.getId().equals(loggedInUser.getId())) {
            // 수정 시 다른 사용자의 게시물을 수정하려고 할 경우 오류 메시지 설정
            model.addAttribute("errorMessage", "본인의 게시물만 수정할 수 있습니다."); // 에러 메시지 추가
            model.addAttribute("dto", boardDto); // 수정할 게시글 정보 다시 전달
            return "board/boardDetail"; // 게시글 상세보기 페이지로 리턴
        }

        // 게시글 수정 처리
        boardService.updateBoard(updateBoardCommand);
        
        // 수정 후 게시글 목록 페이지로 리다이렉트
        return "redirect:/board/boardList"; 
    }



    // 게시글 다중 삭제
    @RequestMapping(value = "mulDel", method = {RequestMethod.POST, RequestMethod.GET})
    public String mulDel(@Validated DelBoardCommand delBoardCommand, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            System.out.println("최소 하나 체크하기");
            List<BoardDto> list = boardService.getAllList();
            model.addAttribute("list", list);
            return "board/boardList";
        }

        MemberDto loggedInUser = (MemberDto) request.getSession().getAttribute("mdto");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }

        boolean hasPermission = boardService.checkOwnership(delBoardCommand.getSeq(), loggedInUser.getId());
        if (hasPermission) {
            boardService.mulDel(delBoardCommand.getSeq());
            System.out.println("글삭제함");
            return "redirect:/board/boardList";
        } else {
            model.addAttribute("errorMessage", "본인의 게시물만 삭제할 수 있습니다."); // 에러 메시지를 모델에 추가
            List<BoardDto> list = boardService.getAllList(); // 게시글 목록을 가져옵니다.
            model.addAttribute("list", list); // 모델에 목록 추가
            return "board/boardList"; // 목록 페이지로 이동
        }
    }

}