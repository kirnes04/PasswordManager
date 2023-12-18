package com.example.hw.service;

import com.example.hw.dto.DirectoryDTO;
import com.example.hw.entities.Directory;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DirectoryService {
    public Directory createDirectory(String name, Integer parentId, String email);

    public List<DirectoryDTO> getDirectories(String email, Integer parentId);
}
