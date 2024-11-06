package dat.dtos;

import dat.security.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


/**
 * This UserDTO class extends the UserDTO from a library.
 * Allows us to add any extra information we want, such as phone number, email, etc.
 * REMEMBER: Add extra fields to the User entity in security.entities.User.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends dk.bugelhartmann.UserDTO {
    private int age;

    private int phoneNumber;

    private String email;

    private String username;

    private Set<String> roles = new HashSet();

    public UserDTO(User user) {
        //this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRolesAsStrings();
        this.age = user.getAge();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
    }


}
