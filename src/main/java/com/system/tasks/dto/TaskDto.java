package com.system.tasks.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class TaskDto {

    String title;
    String description;
    String taskStatus;
    String taskPriority;
    TaskUserDto author;
    TaskUserDto executor;
    List<CommentDto> comments;

}
