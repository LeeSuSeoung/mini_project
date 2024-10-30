package com.hk.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hk.board.command.AddUserCommand;
import com.hk.board.command.LoginCommand;
import com.hk.board.dtos.MemberDto;
import com.hk.board.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원 가입 페이지
    @GetMapping(value = "/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("addUserCommand", new AddUserCommand());
        return "member/addUserForm"; // 회원 가입 폼 뷰
    }

    // 회원 추가
    @PostMapping(value = "/addUser")
    public String addUser(@Validated AddUserCommand addUserCommand,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "member/addUserForm"; // 에러 발생 시 다시 회원 가입 폼으로 이동
        }
        try {
            memberService.addUser(addUserCommand);
            return "redirect:/user/Admin"; // 회원 추가 후 Admin 페이지로 리다이렉트
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "회원 추가 중 오류가 발생했습니다.");
            return "member/addUserForm"; // 에러 발생 시 다시 회원 가입 폼으로 이동
        }
    }

    // 아이디 중복 체크
    @ResponseBody
    @GetMapping(value = "/idChk")
    public Map<String, String> idChk(String id) {
        String resultId = memberService.idChk(id);
        Map<String, String> map = new HashMap<>();
        map.put("id", resultId);
        return map; // 결과 반환
    }

    // 로그인 페이지
    @GetMapping(value = "/login")
    public String loginForm(Model model) {
        model.addAttribute("loginCommand", new LoginCommand());
        return "member/login"; // 로그인 폼 뷰
    }

    // 로그인 처리
    @PostMapping(value = "/login")
    public String login(@Validated LoginCommand loginCommand,
                        BindingResult result,
                        Model model,
                        HttpServletRequest request) {
        if (result.hasErrors()) {
            return "member/login"; // 에러 발생 시 다시 로그인 폼으로 이동
        }
        String path = memberService.login(loginCommand, request, model);
        return path; // 로그인 후 경로 반환
    }

    // 로그아웃 처리
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate(); // 세션 무효화
        return "redirect:/"; // 로그아웃 후 홈으로 리다이렉트
    }

    // 전체 회원 목록 조회 (관리자 페이지)
    @GetMapping(value = "/Admin")
    public String getAllUserList(Model model) {
        List<MemberDto> userList = memberService.getAllMembers(); // 모든 회원 목록 조회
        model.addAttribute("list", userList); // 모델에 회원 목록 추가
        return "board/admin"; // 관리자 페이지 뷰로 이동
    }

    // 선택된 회원 삭제 기능 추가
    @PostMapping(value = "/mulDel")
    public String deleteMembers(@RequestParam(value = "memberId", required = false) List<Integer> memberIds, Model model) {
        if (memberIds != null && !memberIds.isEmpty()) {
            try {
                memberService.deleteMembers(memberIds); // 선택된 회원 삭제
                model.addAttribute("successMessage", "선택된 회원이 성공적으로 삭제되었습니다.");
            } catch (Exception e) {
                model.addAttribute("errorMessage", "회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            model.addAttribute("errorMessage", "삭제할 회원을 선택해 주세요."); // 선택된 회원이 없을 경우 메시지
        }
        return "redirect:/user/Admin"; // 삭제 후 Admin 페이지로 리다이렉트
    }
}
