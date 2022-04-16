package com.doldolseo.api.service;

import com.doldolseo.api.dto.CrewPostCommentDTO;
import com.doldolseo.api.dto.CrewPostCommentsDTO;
import com.doldolseo.api.entity.CrewPostComment;
import com.doldolseo.api.repsitory.CrewPostCommentRepository;
import com.doldolseo.api.entity.CrewPost;
import com.doldolseo.api.repsitory.CrewPostRepository;
import com.doldolseo.api.utils.OtherRestUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrewPostCommnetService {
    @Autowired
    CrewPostCommentRepository commentRepository;
    @Autowired
    CrewPostRepository crewPostRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OtherRestUtil restUtil;

    public CrewPostCommentDTO insertComment(CrewPostCommentDTO dto) {
        setWDate(dto);
        setCrewPost(dto);
        CrewPostComment comment = commentRepository.save(dtoToEntity(dto));

        /* crewRest : crewPoint 수정 */
        Long crewNo = comment.getCrewPost().getCrewNo();
        String uri_updateCrewPoint
                = "http://doldolseo-crew-rest.rest.svc.cluster.local:8080/doldolseo/crew/" + crewNo + "/point";
        restUtil.crew_UpdatePoint(uri_updateCrewPoint, 7);

        return entityToDto(comment);
    }

    public void setWDate(CrewPostCommentDTO dto) {
        dto.setWDate(LocalDateTime.now());
    }

    public void setCrewPost(CrewPostCommentDTO dto) {
        CrewPost crewPost = crewPostRepository.findAllByCrewPostNo(dto.getCrewPost().getCrewPostNo());
        dto.setCrewPost(crewPost);
    }

    public CrewPostCommentsDTO getComments(Long crewPostNo) {
        List<CrewPostCommentDTO> comments = entityListToDtoList(commentRepository.findAllByCrewPost_CrewPostNo(crewPostNo));
        Long numOfComments = commentRepository.countCrewPostCommentByCrewPost_CrewPostNo(crewPostNo);

        return new CrewPostCommentsDTO(comments, numOfComments);
    }

    public String getCommentWriter(Long commentNo) {
        return commentRepository.getWriterId(commentNo);
    }

    @Transactional
    public void updateComment(Long commentNo, CrewPostCommentDTO dto) {
        CrewPostComment comment = commentRepository.findByCommentNo(commentNo);
        comment.setWDate(LocalDateTime.now());
        comment.setContent(dto.getContent());
    }

    public void deleteComment(Long commentNo) {
        commentRepository.deleteById(commentNo);
    }

    public CrewPostComment dtoToEntity(CrewPostCommentDTO dto) {
        return modelMapper.map(dto, CrewPostComment.class);
    }

    public CrewPostCommentDTO entityToDto(CrewPostComment comment) {
        return modelMapper.map(comment, CrewPostCommentDTO.class);
    }

    public List<CrewPostCommentDTO> entityListToDtoList(List<CrewPostComment> commentList) {
        return modelMapper.map(commentList, new TypeToken<List<CrewPostCommentDTO>>() {
        }.getType());
    }
}
