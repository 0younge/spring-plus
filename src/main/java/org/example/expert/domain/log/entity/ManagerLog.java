package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class ManagerLog extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestUserId;
    private Long todoId;
    private Long managerUserId;
    private String message;

    public ManagerLog(Long requestUserId, Long todoId, Long managerUserId, String message) {
        this.requestUserId = requestUserId;
        this.todoId = todoId;
        this.managerUserId = managerUserId;
        this.message = message;
    }
}
