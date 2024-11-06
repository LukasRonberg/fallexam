package dat.entities;

import dat.dtos.GuideDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips; // List of Trips associated with this Guide

    public Guide(GuideDTO guideDTO) {
        this.id = guideDTO.getId();
        this.firstName = guideDTO.getFirstName();
        this.lastName = guideDTO.getLastName();
        this.email = guideDTO.getEmail();
        this.phone = guideDTO.getPhone();
        this.yearsOfExperience = guideDTO.getYearsOfExperience();
    }

    public Guide(String firstName, String lastName, String email, String phone, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }
    public void setTrip(Trip trip) {
        trips.add(trip);
        trip.setGuide(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guide guide)) return false;
        return id != null && id.equals(guide.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
