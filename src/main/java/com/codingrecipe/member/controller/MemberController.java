package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    //생성자 주입 (@RequiredArgsConstructor) 어노테이션 사용 시, 컨트롤러 생성하면 자동으로 서비스 생성됨
    private final MemberService memberService;

    //회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);

        //DB 저장
        memberService.save(memberDTO);

        return "login";
    }

    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        MemberDTO loginResult = memberService.login(memberDTO);

        if(loginResult != null){
            //success
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            // fail
            return "login";
        }
    }

    @GetMapping("/member/")
    public String findAll(Model model){
        // 회원 목록 조회
        List<MemberDTO> memberDTOList = memberService.findAll();

        // 회원 목록 출력 화면 호출 (model 객체로 데이터 전달)
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        // rest api 방식의 주소체계를 사용할때는 @PathVariable 어노테이션 사용 (쿼리스트링 방식에서는 @RequestParam 사용)

        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model){
        String myEmail = (String)session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);

        //update 후 리다이렉트로 상세 페이지 호출
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id){
        memberService.deleteById(id);

        // 그냥 list로 이동하면 데이터가 없기 때문에 빈화면이 노출됨, 리당렉트로 호출
        return "redirect:/member/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail){
        //ajax를 사용할 경우 메소드 반환으로 @ResponseBody 사용해야 함
        System.out.println("memberEmail = " + memberEmail);

        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }

}
