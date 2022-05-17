package com.example.club.security.service;

import com.example.club.entity.ClubMember;
import com.example.club.repository.ClubMemberRepository;
import com.example.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername "+username);

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
        if(!result.isPresent()){//사용자가 존재하지 않으면 UsernameNotFoundException 으로 처리
            throw new UsernameNotFoundException("Check Email or Social ");
        }

        ClubMember clubMember = result.get();
        log.info("---------------------------------");
        log.info(clubMember);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(//UserDetails 타임으로 처리하기 위해 ClubMember를 ClubAuthMemberDTO 타입으로 변환
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().map(role ->
                new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())//스프링 시큐리티에서 사용하는 SimpleGrantedAuthority 로 변환
        );

        clubAuthMemberDTO.setName(clubMember.getName());
        clubAuthMemberDTO.setFromSocial(clubMember.isFromSocial());

        return clubAuthMemberDTO;
    }
}
