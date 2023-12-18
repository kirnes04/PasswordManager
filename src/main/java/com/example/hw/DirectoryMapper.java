package com.example.hw;

import com.example.hw.dto.DirectoryDTO;
import com.example.hw.dto.RecordDTO;
import com.example.hw.entities.Directory;
import com.example.hw.entities.Record;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DirectoryMapper {
    List<DirectoryDTO> toDto(List<Directory> dto);
}
