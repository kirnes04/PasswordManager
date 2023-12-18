package com.example.hw.service.Implementation;

import com.example.hw.DirectoryMapper;
import com.example.hw.dto.DirectoryDTO;
import com.example.hw.entities.Directory;
import com.example.hw.repository.DirectoryRepository;
import com.example.hw.repository.UserRepository;
import com.example.hw.service.DirectoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DirectoryServiceImpl implements DirectoryService {
    private final UserRepository userRepository;
    private final DirectoryRepository directoryRepository;

    private final DirectoryMapper directoryMapper;

    public DirectoryServiceImpl(UserRepository userRepository, DirectoryRepository directoryRepository, DirectoryMapper directoryMapper) {
        this.userRepository = userRepository;
        this.directoryRepository = directoryRepository;
        this.directoryMapper = directoryMapper;
    }

    @Override
    public Directory createDirectory(String name, Integer parentId, String email) {
        var userId = userRepository.findIdByEmail(email);
        if (directoryRepository.getDirectoriesByName(name, parentId, userId).isPresent()) {
            throw new IllegalArgumentException(String.format("Directory with name %s already exists", name));
        }

        var dir = directoryRepository.findById(parentId);
        if (dir.isEmpty() || !Objects.equals(dir.get().getUserId(), userId)) {
            throw new IllegalArgumentException("Wrong parent directory id");
        }

        var directory = new Directory(0, name, userId, parentId);
        return directoryRepository.save(directory);
    }

    @Override
    public List<DirectoryDTO> getDirectories(String email, Integer parentId) {
        return directoryMapper.toDto(directoryRepository.getAllByUserId(userRepository.findIdByEmail(email), parentId));
    }
}
