package com.example.service;

import com.example.domain.Member;
import com.example.dto.MemberDto;
import com.example.exception.DuplicateResourceException;
import com.example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(MemberDto memberDto) {
        if(memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicateResourceException("이미 존재하는 회원입니다.");
        }
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());

        Member member = memberDto.toEntity(encodedPassword);

        this.memberRepository.save(member);
    }

    public Member getCurrentMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다"));
    }
}
