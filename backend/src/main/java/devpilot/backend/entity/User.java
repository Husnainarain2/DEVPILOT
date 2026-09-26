package devpilot.backend.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column (name = "github_id", unique = true, nullable = false)
    private Long githubId;

    @Column(name = "github_username", nullable = false,length = 100)
    private String githubUsername;

    @Column (name = "display_name", nullable = false,length = 100)
    private String displayName;
    @Column (name = "avatar_url", nullable = false, length = 555)
    private String avatarUrl;
    @Column (name = "access_token", nullable = false, length = 555)
    private String accessToken;
    @Column (name = "refresh_token", nullable = false, length = 555)
    private String tokenScope;
    @Column (name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist 
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}