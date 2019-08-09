package kz.ggi_dev.high_test_project_for_boostbrain.repos;

import kz.ggi_dev.high_test_project_for_boostbrain.model.weather.CurrentWeather;
import org.springframework.data.repository.CrudRepository;


public interface CurrentWeatherRepo extends CrudRepository<CurrentWeather, Long> {

        CurrentWeather findByNameAndCountry(String name, String country);
}
