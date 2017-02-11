package MainPackage;

/**
 * Created by Rachel on 2/11/2017.
 */
public class User {
    private String firstName;
    private String lastName;
    private long phoneNumber;
    private String email;
    private String password;

    public User(String firstName, String lastName, long phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
