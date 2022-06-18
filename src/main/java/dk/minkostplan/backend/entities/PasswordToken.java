package dk.minkostplan.backend.entities;

import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PasswordToken {
    @Id
    @Getter
    @Setter
    @Column(name = "unique_id", unique = true, nullable = false)
    private UUID uniqueId;

    @Getter
    @Setter
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="user_id", nullable = false, unique = true)
    private User user;

    @Getter
    @Setter
    @Timestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Timestamp
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public PasswordToken(User user){
        setUser(user);
        setUniqueId(UUID.randomUUID());
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(15);
    }
}
