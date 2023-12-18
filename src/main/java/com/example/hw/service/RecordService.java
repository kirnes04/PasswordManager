package com.example.hw.service;

import com.example.hw.dto.CreateRecordDTO;
import com.example.hw.entities.Record;
import com.example.hw.dto.RecordDTO;

import java.util.List;
import java.util.Optional;

public interface RecordService {
    public List<RecordDTO> getAllRecords(Integer directoryId, String email);

    public CreateRecordDTO addRecord(CreateRecordDTO record, String email, Integer directoryId);

    public Optional<Record> getRecordById(Integer id, String email);

    public Record putRecordById(Integer id, Record record, String email);

    public Record patchRecord(Integer recordId, Integer directoryId, String email);
}
