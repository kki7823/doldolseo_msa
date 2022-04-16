package com.doldolseo.api.member_rest;

import com.doldolseo.api.dto.MemberDTO;
import com.doldolseo.api.entity.Member;
import com.doldolseo.api.member_rest.MemberRepository;
import com.doldolseo.api.utils.RedisUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class MemberService {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;

    public MemberDTO registMember(MemberDTO dto) {
        String rawPassword = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        dto.setPassword(encodedPassword);
        dto.setJoinDate(LocalDateTime.now());
        dto.setMemberRole("USER");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = null;
        try {
            birth = formatter.parse(dto.getBirth());
        } catch (ParseException e) {
            System.out.println("MemberService.registMember : " + e.getMessage());
        }

        Member memberEntity = modelMapper.map(dto, Member.class);
        Member member = repository.save(memberEntity);
        member.setBirth(birth);

        return modelMapper.map(member, MemberDTO.class);
    }

    public boolean checkMemberId(String id) {
        return repository.existsById(id);
    }

    public boolean checkMemberNickName(String nickName) {
        return repository.existsByNickname(nickName);
    }

    public MemberDTO getMember(String id) throws UsernameNotFoundException {
        Member member = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id));
        return modelMapper.map(member, MemberDTO.class);
    }

    public String getMemberNickname(String id) throws UsernameNotFoundException {
        if (redisUtil.isExist(id)) {
            return redisUtil.get(id);
        } else {
            String nickName = (String) repository.findNickNameById(id)
                    .orElseThrow(() -> new UsernameNotFoundException(id));
            redisUtil.put(id, nickName, 60L);
            return nickName;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMember(String id, MemberDTO dto) {
        System.out.println(dto.toString());
        Member member = repository
                .findById(id).orElseThrow(() -> new UsernameNotFoundException(id));

        if (dto.getMemberImg() != null)
            member.setMemberImg(dto.getMemberImg());


        if (dto.getEmail() != null)
            member.setEmail(dto.getEmail());

        member.setPhone(dto.getPhone());

        if (dto.getPassword() != null) {
            String rawPassword = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            member.setPassword(encodedPassword);
        }

        member.setGender(dto.getGender());
        System.out.println("updateMember is done.");
    }

    @Transactional
    public void updateUserToCrewLeader(String id) {
        Member member = repository
                .findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
        member.setMemberRole("CREWLEADER");
    }

    @Transactional
    public void updateCrewLeaderToUser(String id) {
        Member member = repository
                .findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
        member.setMemberRole("USER");
    }

    public void deleteMember(String id) {
        repository.deleteById(id);
        System.out.println("member id " + id + " is deleted.");
    }

}
