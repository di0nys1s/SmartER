package com.di0nys1s.smarter.JavaClass;

import java.util.Date;

public class ElectricityUsage {
    private  Integer usageid;
    private Integer resid;
    private String date;
    private Integer usageHour;
    private Double fridgeUsage;
    private Double wmUsage;
    private Double acUsage;
    private Double temperature;

    public ElectricityUsage(Integer usageid, Integer resid,
                            String date,Integer usageHour,
                            Double fridgeUsage, Double wmUsage,
                            Double acUsage, Double temperature) {
        this.usageid = usageid;
        this.resid = resid;
        this.date = date;
        this.usageHour = usageHour;
        this.fridgeUsage = fridgeUsage;
        this.wmUsage = wmUsage;
        this.acUsage = acUsage;
        this.temperature = temperature;
    }

    public Integer getUsageid() {
        return usageid;
    }

    public void setUsageid(Integer usageid) {
        this.usageid = usageid;
    }

    public Integer getResid() {
        return resid;
    }

    public void setResid(Integer resid) {
        this.resid = resid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUsageHour() {
        return usageHour;
    }

    public void setUsageHour(Integer usageHour) {
        this.usageHour = usageHour;
    }

    public Double getFridgeUsage() {
        return fridgeUsage;
    }

    public void setFridgeUsage(Double fridgeUsage) {
        this.fridgeUsage = fridgeUsage;
    }

    public Double getWmUsage() {
        return wmUsage;
    }

    public void setWmUsage(Double wmUsage) {
        this.wmUsage = wmUsage;
    }

    public Double getAcUsage() {
        return acUsage;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
