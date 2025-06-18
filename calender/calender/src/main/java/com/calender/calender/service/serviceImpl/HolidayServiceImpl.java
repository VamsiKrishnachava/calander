package com.calender.calender.service.serviceImpl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.calender.calender.model.Country;
import com.calender.calender.model.CountryResponse;
import com.calender.calender.model.Holiday;
import com.calender.calender.model.HolidayResponse;
import com.calender.calender.service.HolidayService;



@Service
public class HolidayServiceImpl implements HolidayService{
    @Autowired
    private RestTemplate restTemplate;

    @Value("${calendarific.api.key}")
    private String apiKey;

    
    public List<Country> getSupportedCountries() {
        String url = "https://calendarific.com/api/v2/countries?api_key=" + apiKey;
        Optional<CountryResponse> response = Optional.ofNullable(restTemplate.getForObject(url, CountryResponse.class));
        return response.get().getResponse().getCountries();
    }

    public List<Holiday> getHolidays(String countryCode, int year) {
        String url = "https://calendarific.com/api/v2/holidays"
                + "?api_key=" + apiKey
                + "&country=" + countryCode
                + "&year=" + year
                + "&type=national"; 

        HolidayResponse response = restTemplate.getForObject(url, HolidayResponse.class);
        
        return response.getResponse().getHolidays();
    }


}

