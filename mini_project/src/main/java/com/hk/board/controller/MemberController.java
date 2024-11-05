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
import com.hk.board.mapper.MemberMapper;
import com.hk.board.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper; // MemberMapper 주입

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
    
    // 사용자 목록 조회
    @GetMapping("/userList")
    public String userList(HttpServletRequest request, Model model) {
        MemberDto memberDto = (MemberDto) request.getSession().getAttribute("mdto");
        if (memberDto == null) {
            return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
        }
        
        model.addAttribute("currentUser", memberDto); // 현재 사용자 정보 추가
        
        return "member/userList"; // 사용자 목록 페이지로 이동
    }

    // 마이페이지 리다이렉트
    @GetMapping("/myPage")
    public String myPage(HttpServletRequest request) {
        MemberDto memberDto = (MemberDto) request.getSession().getAttribute("mdto");
        if (memberDto == null) {
            return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
        }
        
        // 역할에 따라 리다이렉트
        if ("ADMIN".equals(memberDto.getRole()) || "MODERATOR".equals(memberDto.getRole())) {
            return "redirect:/user/Admin"; // ADMIN 또는 MODERATOR일 경우 관리자 페이지로 리다이렉트
        } else {
            return "redirect:/user/userList"; // 일반 사용자일 경우 사용자 목록 페이지로 리다이렉트
        }
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
    
    @PostMapping("/updateRoles")
    public String updateRoles(@RequestParam Map<String, String> roles, Model model) {
        try {
            for (Map.Entry<String, String> entry : roles.entrySet()) {
                int memberId = Integer.parseInt(entry.getKey().replace("roles[", "").replace("]", ""));
                String role = entry.getValue();

                // MemberMapper를 통해 역할 업데이트
                memberMapper.updateMemberRole(memberId, role);
            }
            model.addAttribute("successMessage", "회원 등급이 성공적으로 수정되었습니다."); // 추가된 메시지
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원 등급 수정 중 오류가 발생했습니다."); // 오류 메시지
            e.printStackTrace();
        }
        return "redirect:/user/Admin"; // 회원 목록 페이지로 리다이렉트
    }
    
    @PostMapping("/updateUserDetails")
    public String updateUserDetails(@RequestParam("memberId") int memberId,
                                    @RequestParam("address") String address,
                                    @RequestParam("email") String email,
                                    HttpServletRequest request, Model model) {
        try {
            memberService.updateUserDetails(memberId, address, email);
            model.addAttribute("successMessage", "정보가 성공적으로 수정되었습니다.");
            // 세션에서 현재 사용자 정보 업데이트
            MemberDto updatedUser = memberService.getMemberById(memberId);
            request.getSession().setAttribute("mdto", updatedUser);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "정보 수정 중 오류가 발생했습니다.");
        }
        return "redirect:/user/userList";
    }
}