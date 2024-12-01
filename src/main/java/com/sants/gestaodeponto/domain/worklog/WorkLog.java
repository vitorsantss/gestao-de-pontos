package com.sants.gestaodeponto.domain.worklog;

import com.sants.gestaodeponto.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "work_logs")
@Entity(name = "work_logs")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkLog {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private LogType logType;

    private LocalDateTime timestamp;

    public WorkLog(User user, LogType logType, LocalDateTime timestamp) {
        this.user = user;
        this.logType = logType;
        this.timestamp = timestamp;
    }
}
