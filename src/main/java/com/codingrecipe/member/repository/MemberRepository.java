package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // save(), findAll(), deleteById() 은 JpaRepository에 존재하는 메소드여서 상속받은 상태기 때문에 정의 없이 사용 가능


    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
