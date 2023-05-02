package pl.edu.pg.accommodation.ping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date")
    private LocalDateTime pingDateTime;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getPingDateTime() {
        return pingDateTime;
    }

    public void setPingDateTime(final LocalDateTime time) {
        this.pingDateTime = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PingEntity{" +
                "id=" + id +
                ", pingDateTime=" + pingDateTime +
                ", message='" + message + '\'' +
                '}';
    }
}
