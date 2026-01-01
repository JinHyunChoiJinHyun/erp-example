package com.example.service;

import com.example.domain.Member;
import com.example.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MemberDetailService implements UserDetailsService {
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->  new UsernameNotFoundException(email));
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .build();
    }
}
