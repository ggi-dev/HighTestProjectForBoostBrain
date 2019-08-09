package kz.ggi_dev.high_test_project_for_boostbrain.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import kz.ggi_dev.high_test_project_for_boostbrain.model.weather.CurrentWeather;
import kz.ggi_dev.high_test_project_for_boostbrain.model.weather.CurrentWeatherFromJSON;
import kz.ggi_dev.high_test_project_for_boostbrain.repos.CurrentWeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@Scope("singleton")
public class CurrentWeatherService {
    private final String CODE_404 = "404";
    private final String CODE_500 = "500";
    private final String STRING_FOR_ADD_APPID_IN_URL = "&appid=";
    private final int TIME_IN_SECONDS_FOR_UPDATE = 2 * 60 * 60;
    private final double DOUBLE_NUMBER_FOR_CONVERT_TEMP = 273.15;

    @Value("${weather.key}")
    private String weather_key;
    @Value("${weather.urlapi}")
    private String urlApiWeatherForecastFiveDays;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CurrentWeatherRepo currentWeatherRepo;


    public CurrentWeather getCurrentWeather(final String name, final String country) {
        CurrentWeather result = null;
        CurrentWeather cwFromDB = currentWeatherRepo.findByNameAndCountry(this.getValidName(name),country.toUpperCase());
        if ((cwFromDB != null) &&
                (((System.currentTimeMillis()/1000) - cwFromDB.getDate()) < TIME_IN_SECONDS_FOR_UPDATE))  {
            return cwFromDB;
        }

        URL urlToApi = getValidUrl(name, country);
        if (urlToApi == null) {
            return new CurrentWeather(CODE_500);
        }

        CurrentWeatherFromJSON cwFromJSON = getCurrentWeatherFromJSON(urlToApi);
        if (cwFromJSON == null) {
            return new CurrentWeather(CODE_404);
        }

        if (cwFromDB != null) {
            cwFromDB.setDate(cwFromJSON.getDt());
            cwFromDB.setTemp((int)Math.round(cwFromJSON.getMain().getTemp() - DOUBLE_NUMBER_FOR_CONVERT_TEMP));
            currentWeatherRepo.save(cwFromDB);
            result = cwFromDB;
        } else {
            CurrentWeather cw = new CurrentWeather();
            cw.setName(this.getValidName(name));
            cw.setCountry(country.toUpperCase());
            cw.setDate(cwFromJSON.getDt());
            cw.setTemp((int)Math.round(cwFromJSON.getMain().getTemp() - DOUBLE_NUMBER_FOR_CONVERT_TEMP));
            currentWeatherRepo.save(cw);
            result = cw;
        }
        return result;

    }

    private URL getValidUrl(final String name, final String country) {
        URL urlToApi = null;
        String strUrl = urlApiWeatherForecastFiveDays +
                        name + "," + country +
                        STRING_FOR_ADD_APPID_IN_URL + weather_key;
        try {
            urlToApi = new URL(strUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlToApi;
    }

    private CurrentWeatherFromJSON getCurrentWeatherFromJSON(final URL urlToApi) {
        CurrentWeatherFromJSON result = null;
        try {
            result = this.mapper.readValue(urlToApi, CurrentWeatherFromJSON.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getValidName(final String name) {
        StringBuilder result = new StringBuilder();
        if ((name!= null) && (name.length() == 0)) return "";
        if (name.length() == 1) return name.toUpperCase();
        result.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));
        return result.toString();
    }
}

