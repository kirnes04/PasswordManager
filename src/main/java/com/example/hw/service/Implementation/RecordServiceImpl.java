package com.example.hw.service.Implementation;

import com.example.hw.RecordMapper;
import com.example.hw.repository.DirectoryRepository;
import com.example.hw.repository.RecordRepository;
import com.example.hw.dto.CreateRecordDTO;
import com.example.hw.entities.Record;
import com.example.hw.dto.RecordDTO;
import com.example.hw.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecordServiceImpl implements com.example.hw.service.RecordService {
    private final RecordRepository recordRepository;

    private final UserRepository userRepository;

    private final RecordMapper recordMapper;

    private final DirectoryRepository directoryRepository;

    public RecordServiceImpl(RecordRepository recordRepository, UserRepository userRepository, RecordMapper recordMapper, PasswordEncoder passwordEncoder, DirectoryRepository directoryRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.recordMapper = recordMapper;
        this.directoryRepository = directoryRepository;
    }

    public List<RecordDTO> getAllRecords(Integer directoryId, String email) {
        return recordMapper.toDto(recordRepository.findAllByUserEmail(email, directoryId));
    }

    public CreateRecordDTO addRecord(CreateRecordDTO record, String email, Integer directoryId) {
        if (record.getName() == null) {
            throw new IllegalArgumentException("Field name can't be empty");
        }
        if (record.getPassword() == null) {
            throw new IllegalArgumentException("Field password can't be empty");
        }

        if (directoryId != 0) {
            var dir = directoryRepository.findById(directoryId);
            if (dir.isEmpty()) {
                throw new IllegalArgumentException("Such directory does not exist");
            }

            if (!Objects.equals(dir.get().getUserId(), userRepository.findIdByEmail(email))) {
                throw new IllegalArgumentException("You cannot add record to this directory");
            }
        }

        var newRecord = recordMapper.fromDto(record);
        newRecord.setPassword(record.getPassword());
        newRecord.setUserId(userRepository.findIdByEmail(email));
        newRecord.setDirectoryId(directoryId);
        recordRepository.save(newRecord);
        return record;
    }

    public Record patchRecord(Integer recordId, Integer directoryId, String email) {
        if (!recordRepository.existsById(recordId)) {
            throw new IllegalArgumentException("Such record does not exist");
        }

        var record = recordRepository.getReferenceById(recordId);
        if (directoryId != 0) {
            var directory = directoryRepository.findById(directoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Such directory does not exist"));
            if (!Objects.equals(directory.getUserId(), userRepository.findIdByEmail(email))) {
                throw new IllegalArgumentException("You cannot add record to this directory");
            }
        }

        record.setDirectoryId(directoryId);
        return recordRepository.save(record);
    }

    public Optional<Record> getRecordById(Integer id, String email) {
        var res = recordRepository.findById(id);
        if (res.isPresent()) {
            if (!Objects.equals(res.get().getUserId(), userRepository.findIdByEmail(email))) {
                throw new IllegalArgumentException("Wrong record id");
            }
        }
        return res;
    }

    public Record putRecordById(Integer id, Record record, String email) {
        if (record.getName() == null) {
            throw new IllegalArgumentException("You can't change record's name to null");
        }
        if (record.getPassword() == null) {
            throw new IllegalArgumentException("You can't change record's password to null");
        }
        if (!Objects.equals(id, record.getId())) {
            throw new IllegalArgumentException("You can't change record's id");
        }
        if (!Objects.equals(userRepository.findIdByEmail(email), record.getUserId())) {
            throw new IllegalArgumentException("You can't change someone else's record");
        }

        if (recordRepository.findById(id).isPresent()) {
            var rec = recordRepository.findById(id).get();
            if (!Objects.equals(rec.getUserId(), record.getUserId())) {
                throw new IllegalArgumentException("You can't change the owner of the record this way");
            }
            if (!Objects.equals(rec.getDirectoryId(), record.getDirectoryId())) {
                throw new IllegalArgumentException("You can't change the directory of the record this way." +
                        " Use patch request instead");
            }
        }

        var recordToUpdate = recordRepository.getReferenceById(id);
        recordToUpdate.setName(record.getName());
        recordToUpdate.setPassword(record.getPassword());
        recordToUpdate.setLogin(record.getLogin());
        recordToUpdate.setUrl(record.getUrl());
        return recordRepository.save(recordToUpdate);
    }
}
