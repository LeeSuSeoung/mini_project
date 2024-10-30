package com.hk.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.hk.board.dtos.MemberDto;

import java.util.List;

@Mapper
public interface MemberMapper {
	
	// 사용자 추가
	public boolean addUser(MemberDto dto);
	
	// 아이디 중복 체크
	public String idChk(String id);
	
	// 로그인 사용자 정보 조회
	public MemberDto loginUser(String id);
	
	// 전체 회원 목록 조회
	@Select("SELECT memberId, id, name, address, email FROM member")
	public List<MemberDto> getAllMembers(); // 모든 회원 정보를 가져오는 메서드
	
	// 선택된 회원 삭제
	@Delete({
		"<script>",
		"DELETE FROM member WHERE memberId IN ",
		"<foreach item='memberId' collection='list' open='(' separator=',' close=')'>",
		"#{memberId}",
		"</foreach>",
		"</script>"
	})
	public void deleteMembers(List<Integer> memberIds); // 선택된 회원 삭제 메서드
}  
