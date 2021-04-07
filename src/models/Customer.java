package models;

public class Customer {

    private int id;
    private String address;
    private String firstName;
    private String lastName;
    private String password;

    public Customer(int id, String firstName, String lastName, String address, String password){
        this.id = id;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }
}