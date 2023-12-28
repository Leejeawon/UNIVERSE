package com.shop.controller;

import com.shop.dto.MemberDetailsDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인하세요");
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    // @Valid : springMVC에서 검증 기능을 활성화하는 역할
    // bindingResult : 검증을 수행한 객체와 검증 결과에 대한 정보
    @PostMapping(value = "new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        // 오류가 있으면 회원가입페이지로 이동
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            // 회원가입
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/memberDetails/{id}")
    public String viewMemberDetails(@PathVariable Long id, Model model) {
        Member member = memberService.findMemberById(id);
        MemberDetailsDto memberDetailsDto = convertToMemberDetailsDto(member);
        model.addAttribute("memberDetails", memberDetailsDto);
        return "member/memberDetails";
    }

    @GetMapping("/memberEditForm/{id}/edit")
    public String editMemberForm(@PathVariable Long id, Model model) {
        Member member = memberService.findMemberById(id);
        MemberFormDto memberFormDto = convertToMemberFormDto(member);
        model.addAttribute("memberFormDto", memberFormDto);
        return "member/memberEditForm";
    }

    @PostMapping("/memberEditForm/{id}/edit")
    public String editMember(@PathVariable Long id, @Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the edit form with error messages
            return "member/memberEditForm";
        }

        try {
            // Retrieve the existing member
            Member existingMember = memberService.findMemberById(id);

            // Update the member information with the data from the form
            // ... (existing update logic) ...

            // Save the updated member
            memberService.saveMember(existingMember);
        } catch (Exception e) {
            // Handle any exceptions (e.g., data validation, database errors)
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberEditForm";
        }

        // Redirect to the member details page after a successful edit
        return "redirect:/members/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/";
    }

    private MemberDetailsDto convertToMemberDetailsDto(Member member) {
        MemberDetailsDto memberDetailsDto = new MemberDetailsDto();
        memberDetailsDto.setId(member.getId());
        memberDetailsDto.setLoginid(member.getLoginid());
        memberDetailsDto.setName(member.getName());
        memberDetailsDto.setEmail(member.getEmail());
        memberDetailsDto.setAddress(member.getAddress());
        memberDetailsDto.setPhonenumber(member.getPhonenumber());
        memberDetailsDto.setRole(member.getRole());
        memberDetailsDto.setOauth(member.getOauth());
        return memberDetailsDto;
    }

    private MemberFormDto convertToMemberFormDto(Member member) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setLoginid(member.getLoginid());
        memberFormDto.setPassword(""); // Set an empty password or leave it as null if you don't want to display the password
        memberFormDto.setPasswordCheck(""); // Set an empty passwordCheck or leave it as null if you don't want to display the passwordCheck
        memberFormDto.setName(member.getName());
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setAddress(member.getAddress());
        memberFormDto.setPhonenumber(member.getPhonenumber());
        return memberFormDto;
    }
}
