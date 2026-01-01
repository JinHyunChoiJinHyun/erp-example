package com.example.controller;

import com.example.domain.Member;
import com.example.dto.MemberDto;
import com.example.exception.DuplicateResourceException;
import com.example.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(@RequestBody MemberDto dto){
        try{
            memberService.create(dto);
        } catch (DuplicateResourceException e){
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "회원 가입 실패";
        }
        return "회원 가입 성공";
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 되지 않았습니다");
        }
        String email = authentication.getName();
        Member member = memberService.getCurrentMember(email);

        return ResponseEntity.ok(Map.of(
                "email", member.getEmail(),
                "name", member.getUsername()
        ));
    }
}
