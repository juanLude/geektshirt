/**
 * class from which Geek objects can be created
 */
public class Geek {

    private final String name;
    private final long phoneNumber;
    /**
     * Constructor used to create a Geek object
     * @param name a String value representing the Geek's first, last and any middle names
     * @param phoneNumber a long value representing the geek's 10 digit phone number
     */
    public Geek(String name, long phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    /**
     * full name, including first name, surname and any middle names
     * @return the Geek's name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the Geek's 10-digit phone number
     */
    public long getPhoneNumber() {
        return phoneNumber;
    }
}
