package com.calender.calender.controller;

import com.calender.calender.model.Holiday;
import com.calender.calender.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
public class CalendarController {

    @Autowired
    private HolidayService holidayService;

    private List<Integer> years = List.of(2024, 2025, 2026), months = List.of(1,2,3,4,5,6,7,8,9,10,11,12);

    

    @GetMapping("/")
    public String showMonthlyForm(Model model) {
        model.addAttribute("countries", holidayService.getSupportedCountries());
        model.addAttribute("years", years);
        model.addAttribute("months", months);
        return "calendar";
    }

    @PostMapping("/show")
    public String showMonthlyCalendar(
            @RequestParam String country,
            @RequestParam int year,
            @RequestParam int month,
            Model model
    ) {
        List<Holiday> holidays = holidayService.getHolidays(country, year);
        Map<String, List<Holiday>> holidayMap = new HashMap<>();
        for (Holiday h : holidays) {
            holidayMap.computeIfAbsent(h.getDate().getIso(), d -> new ArrayList<>()).add(h);
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);

        while (start.getDayOfWeek().getValue() != 7) {
            start = start.minusDays(1);
        }

        List<List<LocalDate>> weeks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<LocalDate> week = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                week.add(start.plusDays(i * 7 + j));
            }
            weeks.add(week);
        }

        Map<String, String> weekCssClassMap = new HashMap<>();
        for (int i = 0; i < weeks.size(); i++) {
            List<LocalDate> week = weeks.get(i);
            List<LocalDate> weekdays = week.stream()
                .filter(d -> d.getDayOfWeek().getValue() >= 1 && d.getDayOfWeek().getValue() <= 5)
                .toList();

            long weekdayHolidays = weekdays.stream()
                .filter(d -> holidayMap.containsKey(d.toString()))
                .count();

            String weekKey = "week_" + i;
            if (weekdayHolidays == 1) {
                weekCssClassMap.put(weekKey, "light-green");
            } else if (weekdayHolidays > 1) {
                weekCssClassMap.put(weekKey, "dark-green");
            }
        }

        model.addAttribute("weeks", weeks);
        model.addAttribute("weekCssClassMap", weekCssClassMap);
        model.addAttribute("holidays", holidayMap);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("country", country);
        model.addAttribute("countries", holidayService.getSupportedCountries());
        model.addAttribute("years", years);
        model.addAttribute("months", months);
        return "calendar";
    }

    @GetMapping("/quarter")
    public String showQuarterForm(Model model) {
        model.addAttribute("countries", holidayService.getSupportedCountries());
        model.addAttribute("years", List.of(2024, 2025, 2026));
        return "quarter";
    }

    @PostMapping("/show-quarter")
    public String showQuarterCalendar(
            @RequestParam String country,
            @RequestParam int year,
            @RequestParam int quarter,
            Model model
    ) {
        List<Holiday> holidays = holidayService.getHolidays(country, year);
        Map<String, List<Holiday>> holidayMap = new HashMap<>();
        for (Holiday h : holidays) {
            holidayMap.computeIfAbsent(h.getDate().getIso(), d -> new ArrayList<>()).add(h);
        }

        Map<Integer, List<List<LocalDate>>> quarterWeeks = new LinkedHashMap<>();
        Map<String, String> weekCssClassMap = new HashMap<>();

        int startMonth = (quarter - 1) * 3 + 1;
        int weekCounter = 0;

        for (int m = startMonth; m < startMonth + 3; m++) {
            YearMonth ym = YearMonth.of(year, m);
            LocalDate start = ym.atDay(1);
            while (start.getDayOfWeek().getValue() != 7) {
                start = start.minusDays(1);
            }

            List<List<LocalDate>> weeks = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                List<LocalDate> week = new ArrayList<>();
                for (int j = 0; j < 7; j++) {
                    week.add(start.plusDays(i * 7 + j));
                }

                long weekdayHolidays = week.stream()
                    .filter(d -> d.getDayOfWeek().getValue() >= 1 && d.getDayOfWeek().getValue() <= 5)
                    .filter(d -> holidayMap.containsKey(d.toString()))
                    .count();

                String weekKey = "week_" + m + "_" + i;
                if (weekdayHolidays == 1) {
                    weekCssClassMap.put(weekKey, "light-green");
                } else if (weekdayHolidays > 1) {
                    weekCssClassMap.put(weekKey, "dark-green");
                }

                weeks.add(week);
                weekCounter++;
            }
            quarterWeeks.put(m, weeks);
        }

        model.addAttribute("quarter", quarter);
        model.addAttribute("country", country);
        model.addAttribute("year", year);
        model.addAttribute("holidays", holidayMap);
        model.addAttribute("quarterWeeks", quarterWeeks);
        model.addAttribute("weekCssClassMap", weekCssClassMap);
        model.addAttribute("countries", holidayService.getSupportedCountries());
        model.addAttribute("years", years);
        return "quarter";
    }
}
