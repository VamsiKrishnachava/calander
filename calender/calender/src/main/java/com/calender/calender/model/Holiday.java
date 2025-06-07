package com.calender.calender.model;

import lombok.Data;

@Data
public class Holiday {
    private String name;
    private String description;
    private HolidayDate date;
}



