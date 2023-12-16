package com.system.tasks.mappers.impl;

import com.system.tasks.dto.CommentDto;
import com.system.tasks.entity.Comment;
import com.system.tasks.mappers.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment mapToComment(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }

    @Override
    public CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .build();
    }

    @Override
    public List<Comment> mapAllToComment(List<CommentDto> commentDtos) {
        List<Comment> comments = new ArrayList<>();
        commentDtos.forEach(el -> {
            comments.add(mapToComment(el));
        });
        return comments;
    }

    @Override
    public List<CommentDto> mapAllToCommentDto(List<Comment> commentDtos) {
        List<CommentDto> comments = new ArrayList<>();
        commentDtos.forEach(el -> {
            comments.add(mapToDto(el));
        });
        return comments;
    }
}
