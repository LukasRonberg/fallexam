package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.entities.Guide;
import dat.entities.Trip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {

    private Integer id;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    private double longitude;
    private double latitude;
    private double price;
    private Long guideId;
    private Trip.Category category;

    @Getter
    @Setter
    @JsonIgnore
    private GuideDTO guide;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.longitude = trip.getLongitude();
        this.latitude = trip.getLatitude();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        if (trip.getGuide() != null) {
            this.guide = new GuideDTO(trip.getGuide());
        }
    }

    public TripDTO(String name, LocalDate startTime, LocalDate endTime, double longitude, double latitude, double price, Trip.Category category) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripDTO tripDto)) return false;

        return getId().equals(tripDto.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
