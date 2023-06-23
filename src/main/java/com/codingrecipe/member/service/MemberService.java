package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        // 1. dto -> entity 객체로 변환 (서비스 or entity에서 구현)
        // 2. repository의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);

        // repository의 save 메서드 호출 (조건 : entity 객체를 넘겨줘야 함)
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /*
         1. 회원이 입력한 이메일로 DB에서 조회
         2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */

        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()){
            // 조회 결과가 있음
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                // 비밀번호 일치
                // 비밀번호 일치 시 Entity -> DTO 객체로 변환하여 리턴

                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;

            }  else {
                // 비밀번호 불일치
                return null;
            }

        } else {
            // 조회 결과가 없음
            return null;
        }

    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();

        // Entity List -> DTO List 로 변환하여 컨트롤러로 리턴
        // 리스트는 한번에 변환할 수 없고 반복해서 호출해서 변환해야 함
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
            //.isPresent() 조회 결과가 있는 경우
            // Entity -> DTO 객체로 변환하여 리턴
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            // 조회 결과가 없는 경우
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);

        if(optionalMemberEntity.isPresent()){
            //.isPresent() 조회 결과가 있는 경우
            // Entity -> DTO 객체로 변환하여 리턴
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            // 조회 결과가 없는 경우
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if(byMemberEmail.isPresent()){
            // 조회 결과가 있는 경우 - 동일한 email이 있는 경우
            return null;
        } else {
            // 조회 결과가 없는 경우 - 동일한 email이 없는 경우
            return "ok";
        }
    }
}
