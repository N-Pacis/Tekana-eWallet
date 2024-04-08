package rw.pacis.tekanaewallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccountLoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String userAgent;

    @Column
    private String deviceType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount user;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at")
    private LocalDateTime createdAt;

    public UserAccountLoginHistory(String userAgent, String deviceType, UserAccount user) {
        this.userAgent = userAgent;
        this.deviceType = deviceType;
        this.user = user;
    }
}
