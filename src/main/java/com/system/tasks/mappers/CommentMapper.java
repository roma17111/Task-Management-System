package com.system.tasks.mappers;

import com.system.tasks.dto.CommentDto;
import com.system.tasks.entity.Comment;

import java.util.List;

public interface CommentMapper {
    Comment mapToComment(CommentDto commentDto);

    CommentDto mapToDto(Comment comment);

    List<Comment> mapAllToComment(List<CommentDto> commentDtos);

    List<CommentDto> mapAllToCommentDto(List<Comment> commentDtos);
}
