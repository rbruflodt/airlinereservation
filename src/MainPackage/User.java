package MainPackage;

/**
 * Created by Rachel on 2/11/2017.
 */
public class User {
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isManager;

    public User(String firstName, String lastName, Long phoneNumber, String email, String password,boolean isManager, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isManager=isManager;
        this.isAdmin=isAdmin;
    }

    public User(){
        firstName="";
        lastName="";
        email="";
        password="";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
