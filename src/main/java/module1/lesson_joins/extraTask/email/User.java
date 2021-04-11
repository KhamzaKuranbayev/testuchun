package module1.lesson_joins.extraTask.email;

public class User {

    private int user_id;
    private String email_address;
    private String password;
    private String firstname;
    private String lastname;

    public User(int user_id, String email_address, String password, String firstname, String lastname) {
        this.user_id = user_id;
        this.email_address = email_address;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}
