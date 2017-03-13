package swt6.orm.domain;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class TemporaryEmployee extends Employee {

    @Column
    private String renter;
    @Column
    private double hourlyRate;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    public TemporaryEmployee() {
        super();
    }
    public TemporaryEmployee(String firstName, String lastName, Date dateOfBirth, String renter,
                             double hourlyRate, Date startDate, Date endDate) {
        super(firstName, lastName, dateOfBirth);
        this.renter = renter;
        this.hourlyRate = hourlyRate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return super.toString() + "\n     rented from: " + renter + ", hourly rate: " + hourlyRate + ", start date: " + startDate
                + ", end date: " + endDate;
    }
}
