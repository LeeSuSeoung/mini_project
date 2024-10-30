package com.hk.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hk.board.command.AddUserCommand;
import com.hk.board.command.LoginCommand;
import com.hk.board.dtos.MemberDto;
import com.hk.board.mapper.MemberMapper;
import com.hk.board.status.RoleStatus;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private PasswordEncoder passwordEncoder; // <-- @Bean으로 등록: SecurityConfig 클래스에서..

	// 회원 가입
	public boolean addUser(AddUserCommand addUserCommand) {
		MemberDto mdto = new MemberDto();
		mdto.setId(addUserCommand.getId());
		mdto.setName(addUserCommand.getName());
		
		// 비밀번호 암호화하여 저장
		mdto.setPassword(passwordEncoder.encode(addUserCommand.getPassword()));
		mdto.setEmail(addUserCommand.getEmail());
		mdto.setAddress(addUserCommand.getAddress());
		mdto.setRole(RoleStatus.USER + ""); // 등급 추가
		return memberMapper.addUser(mdto);
	}
	
	// ID 중복 체크
	public String idChk(String id) {
		return memberMapper.idChk(id);
	}
	
	// 로그인 처리
	public String login(LoginCommand loginCommand, HttpServletRequest request, Model model) {
		MemberDto dto = memberMapper.loginUser(loginCommand.getId());
		String path = "home";
		if (dto != null) {
			// 로그인 폼에서 입력받은 패스워드 값과 DB에 암호화된 패스워드 비교
			if (passwordEncoder.matches(loginCommand.getPassword(), dto.getPassword())) {
				System.out.println("패스워드 같음: 회원이 맞음");
				// 세션 객체에 로그인 정보 저장
				request.getSession().setAttribute("mdto", dto);
				return path;
			} else {
				System.out.println("패스워드 틀림");
				model.addAttribute("msg", "패스워드를 확인하세요");
				path = "member/login";
			}
		} else {
			System.out.println("회원이 아닙니다.");
			model.addAttribute("msg", "아이디를 확인하세요");
			path = "member/login";
		}
		
		return path;
	}

	// 전체 회원 목록 조회
	public List<MemberDto> getAllMembers() {
		return memberMapper.getAllMembers();
	}

	// 선택된 회원 삭제
	public void deleteMembers(List<Integer> memberIds) {
		if (memberIds != null && !memberIds.isEmpty()) {
			memberMapper.deleteMembers(memberIds); // MemberMapper의 deleteMembers 메서드 호출
		}
	}
	
	public void updateMemberRole(int memberId, String role) {
        memberMapper.updateMemberRole(memberId, role);
    }
}
