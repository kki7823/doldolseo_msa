package com.doldolseo.api.member_rest;

import com.doldolseo.api.dto.MemberDTO;
import com.doldolseo.api.entity.Member;
import com.doldolseo.api.jwt.MemberRole;
import com.doldolseo.api.member_rest.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.HashSet;
import java.util.Set;

@Service
public class MemberAuthService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id));
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(MemberRole.USER.getVlaue()));

        if (member.getMemberRole().equals("CREWLEADER")) {
            grantedAuthorities.add(new SimpleGrantedAuthority(MemberRole.CREWLEADER.getVlaue()));
        }

        return new User(member.getId(), member.getPassword(), grantedAuthorities);
    }

    public MemberDTO authenticateMember(String id, String password) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return modelMapper.map(member, MemberDTO.class);
    }
}
