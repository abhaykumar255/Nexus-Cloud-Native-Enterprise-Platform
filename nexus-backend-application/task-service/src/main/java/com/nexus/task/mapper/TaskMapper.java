package com.nexus.task.mapper;

import com.nexus.task.domain.entity.Task;
import com.nexus.task.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for Task entity and DTO
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    
    TaskDto toDto(Task task);
    
    Task toEntity(TaskDto taskDto);
}

