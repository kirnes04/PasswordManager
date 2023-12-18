package com.example.hw.service.Implementation;

import com.example.hw.dao.request.ShareRecordRequest;
import com.example.hw.entities.Record;
import com.example.hw.entities.ShareToken;
import com.example.hw.repository.RecordRepository;
import com.example.hw.repository.ShareTokenRepository;
import com.example.hw.repository.UserRepository;
import com.example.hw.service.ShareTokenService;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShareTokenServiceImpl implements ShareTokenService {

    private final ShareTokenRepository shareTokenRepository;

    private final RecordRepository recordRepository;

    private final UserRepository userRepository;

    public ShareTokenServiceImpl(ShareTokenRepository shareTokenRepository, RecordRepository recordRepository, UserRepository userRepository) {
        this.shareTokenRepository = shareTokenRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShareToken shareRecord(ShareRecordRequest request, String email) {
        var userId = userRepository.findIdByEmail(email);
        var record = recordRepository.findById(request.getRecord_id());
        if (!(record.isPresent() && Objects.equals(record.get().getUserId(), userId))) {
            throw new IllegalArgumentException("You can share only your own record");
        }
        var token = new ShareToken(0, false,
                LocalDateTime.now(),
                (request.getExpirationDate() == null) ?
                        LocalDateTime.now().plusYears(1) :
                        request.getExpirationDate(),
                record.get().getId());
        return shareTokenRepository.save(token);
    }

    // token is token_id

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public Pair<Record, LocalDateTime> useToken(Integer tokenId, String email) {
        if (!shareTokenRepository.existsById(tokenId)) {
            throw new IllegalArgumentException("Such token does not exist");
        }

        var token = shareTokenRepository.getReferenceById(tokenId);
        var userId = userRepository.findIdByEmail(email);
        if (token.getIsUsed()) {
            throw new IllegalArgumentException("This token is already used");
        }
        if (!token.getExpirationDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("This token is expired");
        }

        token.setIsUsed(true);
        var record = recordRepository.findById(token.getRecordId());
        if (record.isEmpty()) {
            throw new IllegalArgumentException("Something went wrong and such record does not exist");
        }

        var newRecord = new Record(0,
                record.get().getName(),
                record.get().getLogin(),
                record.get().getPassword(),
                record.get().getUrl(),
                userId, 0);
        return new Pair<Record, LocalDateTime>(recordRepository.save(newRecord), token.getCreationDate());
    }
}
