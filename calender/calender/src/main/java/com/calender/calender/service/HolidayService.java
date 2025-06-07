package com.calender.calender.service;

import java.util.List;

import com.calender.calender.model.Country;
import com.calender.calender.model.Holiday;

public interface HolidayService {
    List<Holiday> getHolidays(String countryCode, int year);

    List<Country> getSupportedCountries();
}
