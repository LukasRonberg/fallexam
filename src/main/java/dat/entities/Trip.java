package dat.entities;

import dat.dtos.TripDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Setter
    @Column(name = "start_time", nullable = false)
    private LocalDate startTime;

    @Setter
    @Column(name = "end_time", nullable = false)
    private LocalDate endTime;

    @Setter
    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Setter
    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Setter
    @Column(name = "price", nullable = false)
    private double price;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide; // Reference to the Guide that this Trip is associated with


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Trip(String name, LocalDate startTime, LocalDate endTime, double longitude, double latitude, double price, Category category) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.category = category;
    }

    public Trip(TripDTO tripDTO) {
        this.id = tripDTO.getId();
        this.name = tripDTO.getName();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.longitude = tripDTO.getLongitude();
        this.latitude = tripDTO.getLatitude();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
        if (tripDTO.getGuide() != null) {
            this.guide = new Guide(tripDTO.getGuide());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(name, trip.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public enum Category {
        BEACH, CITY, FOREST, LAKE, SEA, SNOW
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
