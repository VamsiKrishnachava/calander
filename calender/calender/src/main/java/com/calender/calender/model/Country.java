package com.calender.calender.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Country {
    @JsonProperty("iso-3166")
    private String iso3166;

    @JsonProperty("country_name")
    private String country_name;
      
}




