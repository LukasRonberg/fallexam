package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.entities.Guide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuideDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int yearsOfExperience;

    @JsonIgnore
    private List<Long> tripIds;

    public GuideDTO(Guide guide) {
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();
        this.yearsOfExperience = guide.getYearsOfExperience();
        if (guide.getTrips() != null) {
            this.tripIds = guide.getTrips().stream().map(trip -> Long.valueOf(trip.getId())).toList();
        }
    }

    public GuideDTO(String firstName, String lastName, String email, String phone, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }
}
