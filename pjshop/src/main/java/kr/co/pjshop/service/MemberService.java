package kr.co.pjshop.service;

import kr.co.pjshop.dto.MemberDto;
import kr.co.pjshop.dto.MemberPageDto;
import kr.co.pjshop.dto.ProfileDto;
import kr.co.pjshop.entity.Member;
import kr.co.pjshop.entity.SearchMember;
import kr.co.pjshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    @Transactional(readOnly = true)
    public MemberPageDto findAllMemberByPaging(Pageable pageable) {
        MemberPageDto memberPageDto = new MemberPageDto();
        Page<MemberDto> memberBoards = memberRepository.searchAll(pageable);
        int homeStartPage = Math.max(1, memberBoards.getPageable().getPageNumber());
        int homeEndPage = Math.min(memberBoards.getTotalPages(), memberBoards.getPageable().getPageNumber() + 5);

        memberPageDto.setMemberBoards(memberBoards);
        memberPageDto.setHomeStartPage(homeStartPage);
        memberPageDto.setHomeEndPage(homeEndPage);

        return memberPageDto;
    }

    @Transactional(readOnly = true)
    public MemberPageDto findAllMemberByConditionByPaging(SearchMember searchMember, Pageable pageable) {
        MemberPageDto memberPageDto = new MemberPageDto();
        Page<MemberDto> memberBoards = memberRepository.searchByCondition(searchMember, pageable);

        int startPage = Math.max(1, memberBoards.getPageable().getPageNumber() - 2);
        int endPage = Math.min(memberBoards.getTotalPages(), startPage + 4);

        memberPageDto.setMemberBoards(memberBoards);
        memberPageDto.setHomeStartPage(startPage);
        memberPageDto.setHomeEndPage(endPage);

        return memberPageDto;
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당하는 회원이 존재하지 않습니다")
        );
    }

    @Transactional
    public Long deleteById(Long id) {
        memberRepository.deleteById(id);
        return id;
    }
    @Transactional
    public void updateProfile(String email, ProfileDto profileDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member findMember = memberRepository.findByEmail(email);
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        findMember.setName(profileDto.getName());
        findMember.setEmail(profileDto.getEmail());
        findMember.setPassword(passwordEncoder.encode(profileDto.getPassword()));

    }


}