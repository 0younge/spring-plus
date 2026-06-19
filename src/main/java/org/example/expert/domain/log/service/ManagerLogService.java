package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.ManagerLog;
import org.example.expert.domain.log.repository.ManagerLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerLogService {

    private final ManagerLogRepository managerLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveManagerLog(Long requestUserId, Long todoId, Long managerUserId) {
        ManagerLog managerLog = new ManagerLog(
                requestUserId,
                todoId,
                managerUserId,
                "매니저 등록 요청"
        );

        managerLogRepository.save(managerLog);
    }
}
