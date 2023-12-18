package com.example.hw.repository;

import com.example.hw.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.*;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    @Query(value = "select * from public.record" +
            " WHERE user_id = (SELECT id FROM public.user WHERE email = ?1)" +
            " and directory_id = ?2",
            nativeQuery = true)
    List<Record> findAllByUserEmail(String email, Integer directoryId);
}
