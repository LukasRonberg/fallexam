package dat.security.entities;

import dat.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
@Entity
@Table(name = "users")
@NamedQueries(@NamedQuery(name = "User.deleteAllRows", query = "DELETE from User"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable, ISecurityUser {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "username", length = 25, unique = true)
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "phone_number")
    private int phoneNumber;

    @Column(name = "email")
    private String email;

    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_name", referencedColumnName = "username")}, inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Role> roles = new HashSet<>();

    public Set<String> getRolesAsStrings() {
        if (roles.isEmpty()) {
            return null;
        }
        Set<String> rolesAsStrings = new HashSet<>();
        roles.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }


    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
    }

    public User(String userName, String userPass) {
        this.username = userName;
        this.password = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public User(String userName, Set<Role> roleEntityList) {
        this.username = userName;
        this.roles = roleEntityList;
    }

    public User(String username, String password, int age, int phoneNumber, String email) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public User(UserDTO userDTO){
        this.username = userDTO.getUsername();
        this.age = userDTO.getAge();
        this.email = userDTO.getEmail();
        this.phoneNumber = userDTO.getPhoneNumber();
    }

    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(String userRole) {
        roles.stream()
                .filter(role -> role.getRoleName().equals(userRole))
                .findFirst()
                .ifPresent(role -> {
                    roles.remove(role);
                    role.getUsers().remove(this);
                });
    }
}

