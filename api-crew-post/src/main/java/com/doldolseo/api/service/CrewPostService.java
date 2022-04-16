package com.doldolseo.api.service;

import com.doldolseo.api.entity.TaggedMemberId;
import com.doldolseo.api.utils.PagingParam;
import com.doldolseo.api.entity.CrewPost;
import com.doldolseo.api.entity.TaggedMember;
import com.doldolseo.api.dto.CrewPostAndMembersDTO;
import com.doldolseo.api.dto.CrewPostDTO;
import com.doldolseo.api.dto.CrewPostPageDTO;
import com.doldolseo.api.dto.TaggedMemberDTO;
import com.doldolseo.api.repsitory.CrewPostRepository;
import com.doldolseo.api.repsitory.TaggedMemberRepository;
import com.doldolseo.api.utils.OtherRestUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrewPostService {
    @Autowired
    CrewPostRepository crewPostRepository;
    @Autowired
    TaggedMemberRepository taggedMemberRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OtherRestUtil restUtil;

    /* Crew Post */
    public void saveCrewPost(CrewPostDTO dto, String taggedMembers) {
        dto.setHit(0);
        dto.setWDate(LocalDateTime.now());
        CrewPost crewPost = crewPostRepository.save((CrewPost) dtoToEntity(dto));

        /* crewRest : crewPoint 수정 */
        Long crewNo = dto.getCrewNo();
        String uri_updateCrewPoint
                = "http://doldolseo-crew-rest.rest.svc.cluster.local:8080/doldolseo/crew/" + crewNo + "/point";

        restUtil.crew_UpdatePoint(uri_updateCrewPoint, 20);

        if (taggedMembers.length() != 0)
            addTaggedMembers(taggedMembers, crewPost);
    }

    public CrewPostPageDTO getCrewPostPage(CrewPostDTO dtoIn, Pageable pageable) {
        CrewPostPageDTO crewPostPageDTO = new CrewPostPageDTO();

        Page<CrewPostDTO> crewPostPage;

        if (dtoIn.getCrewNo() != 0)
            crewPostPage = getCrewPostPage(dtoIn.getCrewNo(), pageable);
        else if (dtoIn.getCategory() != 0)
            crewPostPage = getCrewPostPage(dtoIn.getCategory(), pageable);
        else if (dtoIn.getWriterId().length() != 0 && dtoIn.getWriterId() != null)
            crewPostPage = getCrewPostPage(dtoIn.getWriterId(), pageable);
        else
            crewPostPage = getCrewPostPage(pageable);

        PagingParam pagingParam = new PagingParam(5, crewPostPage);

        crewPostPageDTO.setCrewPosts(crewPostPage.getContent());
        crewPostPageDTO.setEndBlockPage(pagingParam.getStartBlockPage());
        crewPostPageDTO.setEndBlockPage(pagingParam.getEndBlockPage());
        crewPostPageDTO.setTotalPages(pagingParam.getTotalPages());

        return crewPostPageDTO;
    }

    public Page<CrewPostDTO> getCrewPostPage(Pageable pageable) {
        return entityPageToDtoPage(crewPostRepository.findAll(pageable));
    }

    public Page<CrewPostDTO> getCrewPostPage(Long crewNo, Pageable pageable) {
        return entityPageToDtoPage(crewPostRepository.findAllByCrewNo(crewNo, pageable));
    }

    public Page<CrewPostDTO> getCrewPostPage(int category, Pageable pageable) {
        return entityPageToDtoPage(crewPostRepository.findAllByCategory(category, pageable));
    }

    public Page<CrewPostDTO> getCrewPostPage(String writerId, Pageable pageable) {
        return entityPageToDtoPage(crewPostRepository.findAllByWriterId(writerId, pageable));
    }

    @Transactional(rollbackFor = Exception.class)
    public CrewPostAndMembersDTO getCrewPostAndMembersAndHit(Long crewPostNo) {
        crewPostRepository.findById(crewPostNo).ifPresent(this::increaseHit);
        return getCrewPostAndMembers(crewPostNo);
    }

    public void increaseHit(CrewPost crewPost) {
        System.out.println(crewPost.getCrewPostNo());
        crewPost.setHit(crewPost.getHit() + 1);
    }

    public CrewPostAndMembersDTO getCrewPostAndMembers(Long crewPostNo) {
        CrewPostAndMembersDTO dto = new CrewPostAndMembersDTO();
        dto.setCrewPost(getCrewPost(crewPostNo));
        dto.setTaggedMemberList(this.getMemberList(crewPostNo));
        return dto;
    }

    public CrewPostDTO getCrewPost(Long crewPostNo) {
        return entityToDto(crewPostRepository.getById(crewPostNo));
    }

    public List<TaggedMemberDTO> getMemberList(Long crewPostNo) {
        return entityListToDtoList(taggedMemberRepository
                .findAllByCrewPost_CrewPostNo(crewPostNo));
    }

    public String getWriterId(Long crewPostNo) {
        return crewPostRepository.getWriterId(crewPostNo);
    }

    @Transactional
    public void updateCrewPost(CrewPostDTO dto, Long crewPostNo) {
        CrewPost crewPost = crewPostRepository.getById(crewPostNo);

        crewPost.setCategory(dto.getCategory());
        crewPost.setTitle(dto.getTitle());
        crewPost.setContent(dto.getContent());
    }

    public void deleteCrewPost(Long crewPostNo) {
        crewPostRepository.deleteById(crewPostNo);
    }

    @Transactional
    public void deleteCrewPostByMemberId(String memberId) {
        crewPostRepository.deleteAllByWriterId(memberId);
    }

    @Transactional
    public void deleteCrewPostByCrewNo(Long crewNo) {
        crewPostRepository.deleteAllByCrewNo(crewNo);
    }

    /* Tagged Member */
    public void addTaggedMembers(String taggedMembers, CrewPost crewPost) {
        for (String memberId : this.getTaggedMembersArray(taggedMembers)) {
            TaggedMemberDTO dto = new TaggedMemberDTO();
            dto.setMemberId(memberId);
            dto.setCrewPost(crewPost);

            taggedMemberRepository.save((TaggedMember) dtoToEntity(dto));
        }
    }

    public String[] getTaggedMembersArray(String taggedMembers) {
        taggedMembers = taggedMembers.substring(1, taggedMembers.length() - 1);
        if (taggedMembers.length() == 0) return null;

        taggedMembers = taggedMembers.replace("\"", "");
        return taggedMembers.split(",");
    }

    @Transactional
    public void saveMembers(Long crewPostNo, String memberId) {
        if (crewPostNo == 0) return;

        TaggedMemberDTO dto = new TaggedMemberDTO();
        dto.setCrewPost(crewPostRepository.getById(crewPostNo));
        dto.setMemberId(memberId);
        taggedMemberRepository.save((TaggedMember) dtoToEntity(dto));
    }

    public void deleteTaggedMember(TaggedMemberId taggedMemberId) {
        taggedMemberRepository.deleteById(taggedMemberId);
    }

    /* Utils */
    public Object dtoToEntity(CrewPostDTO dto) {
        return modelMapper.map(dto, CrewPost.class);
    }

    public Object dtoToEntity(TaggedMemberDTO dto) {
        return modelMapper.map(dto, TaggedMember.class);
    }

    public CrewPostDTO entityToDto(CrewPost crewPost) {
        return modelMapper.map(crewPost, CrewPostDTO.class);
    }

    public TaggedMemberDTO entityToDto(TaggedMember crewMemberWith) {
        return modelMapper.map(crewMemberWith, TaggedMemberDTO.class);
    }

    public Page<CrewPostDTO> entityPageToDtoPage(Page<CrewPost> crewPostPage) {
        return modelMapper.map(crewPostPage, new TypeToken<Page<CrewPostDTO>>() {
        }.getType());
    }

    public List<TaggedMemberDTO> entityListToDtoList(List<TaggedMember> taggedMemberList) {
        return modelMapper.map(taggedMemberList, new TypeToken<List<TaggedMemberDTO>>() {
        }.getType());
    }

}
