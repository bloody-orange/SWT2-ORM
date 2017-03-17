package swt6.orm.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AddressId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String zipCode;
    private String city;
    private String street;

    public AddressId(String zipCode, String city, String street) {
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
    }

    public AddressId() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return zipCode + " " + city + ", " + street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof AddressId) {
            AddressId adressId = (AddressId) o;
            return zipCode.equals(adressId.zipCode) && city.equals(adressId.city) && street.equals(adressId.street);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = zipCode.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + street.hashCode();
        return result;
    }
}
