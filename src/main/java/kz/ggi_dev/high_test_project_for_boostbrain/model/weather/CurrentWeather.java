package kz.ggi_dev.high_test_project_for_boostbrain.model.weather;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CurrentWeather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String country;
    private long date;
    private int temp;

    public CurrentWeather() {
    }

    public CurrentWeather(String name) {
        this.name = name;
    }

    public CurrentWeather(String name, String country, long date, int temp) {
        this.name = name;
        this.country = country;
        this.date = date;
        this.temp = temp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
