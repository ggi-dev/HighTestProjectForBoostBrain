package kz.ggi_dev.high_test_project_for_boostbrain.controller;

import kz.ggi_dev.high_test_project_for_boostbrain.model.weather.CurrentWeather;
import kz.ggi_dev.high_test_project_for_boostbrain.service.CurrentWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class CurrentWeatherController {

    @Autowired
    private CurrentWeatherService currentWeatherService;

    @GetMapping("/")
    public String main(Map<String, Object> model) {
        model.put("title", "Укажите город и страну");
        return "main";
    }

    @PostMapping("/")
    public String findCurrentWeather(
            @RequestParam String name, @RequestParam String country,
            Map<String, Object> model
    ) {
        CurrentWeather currentWeather = currentWeatherService.getCurrentWeather(name,country);
        if ("500".equals(currentWeather.getName())) {
            model.put("title", "Ошибка в работе сервера!");
            return "main";
        }
        if ("404".equals(currentWeather.getName())) {
            model.put("title", "Город не найден!");
            return "main";
        }
        model.put("title", "Текущие состояние");
        model.put("currentWeather", currentWeather);
        return "main";
    }
}


