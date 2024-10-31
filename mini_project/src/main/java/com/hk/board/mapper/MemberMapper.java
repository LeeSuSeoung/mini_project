package com.hk.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.hk.board.dtos.MemberDto;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 사용자 추가
    boolean addUser(MemberDto dto);

    // 아이디 중복 체크
    String idChk(String id);

    // 로그인 사용자 정보 조회
    MemberDto loginUser(String id);

    // 전체 회원 목록 조회
    @Select("SELECT memberId, id, name, address, email, role FROM member") // 등급도 가져오기
    List<MemberDto> getAllMembers(); // 모든 회원 정보를 가져오는 메서드

    // 선택된 회원 삭제
    @Delete({
        "<script>",
        "DELETE FROM member WHERE memberId IN ",
        "<foreach item='memberId' collection='list' open='(' separator=',' close=')'>",
        "#{memberId}",
        "</foreach>",
        "</script>"
    })
    void deleteMembers(List<Integer> memberIds); // 선택된 회원 삭제 메서드

    // 회원 등급 업데이트
    @Update("UPDATE member SET role = #{role} WHERE memberId = #{memberId}")
    void updateMemberRole(int memberId, String role); // 회원 등급 업데이트 메서드
    
 // 사용자 정보 업데이트
    @Update("UPDATE member SET address = #{address}, email = #{email} WHERE memberId = #{memberId}")
    void updateUser(MemberDto dto);

 // 사용자 ID로 정보 조회 쿼리 추가
    @Select("SELECT memberId, id, name, address, email, role FROM member WHERE memberId = #{memberId}")
    MemberDto getMemberById(int memberId);

}
