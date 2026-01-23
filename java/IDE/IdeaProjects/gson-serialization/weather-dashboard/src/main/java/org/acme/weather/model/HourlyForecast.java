package org.acme.weather.model;

import com.google.gson.annotations.SerializedName;

public class HourlyForecast {
    
    @SerializedName("hour1")
    private HourData hour1;
    
    @SerializedName("hour2")
    private HourData hour2;
    
    @SerializedName("hour3")
    private HourData hour3;
    
    @SerializedName("hour4")
    private HourData hour4;
    
    @SerializedName("hour5")
    private HourData hour5;
    
    @SerializedName("hour6")
    private HourData hour6;
    
    @SerializedName("hour7")
    private HourData hour7;
    
    @SerializedName("hour8")
    private HourData hour8;
    
    @SerializedName("hour9")
    private HourData hour9;
    
    @SerializedName("hour10")
    private HourData hour10;
    
    @SerializedName("hour11")
    private HourData hour11;
    
    @SerializedName("hour12")
    private HourData hour12;
    
    @SerializedName("hour13")
    private HourData hour13;
    
    @SerializedName("hour14")
    private HourData hour14;
    
    @SerializedName("hour15")
    private HourData hour15;
    
    @SerializedName("hour16")
    private HourData hour16;
    
    @SerializedName("hour17")
    private HourData hour17;
    
    @SerializedName("hour18")
    private HourData hour18;
    
    @SerializedName("hour19")
    private HourData hour19;
    
    @SerializedName("hour20")
    private HourData hour20;
    
    @SerializedName("hour21")
    private HourData hour21;
    
    @SerializedName("hour22")
    private HourData hour22;
    
    @SerializedName("hour23")
    private HourData hour23;
    
    @SerializedName("hour24")
    private HourData hour24;
    
    @SerializedName("hour25")
    private HourData hour25;

    // Getters
    public HourData getHour1() { return hour1; }
    public HourData getHour2() { return hour2; }
    public HourData getHour3() { return hour3; }
    public HourData getHour4() { return hour4; }
    public HourData getHour5() { return hour5; }
    public HourData getHour6() { return hour6; }
    public HourData getHour7() { return hour7; }
    public HourData getHour8() { return hour8; }
    public HourData getHour9() { return hour9; }
    public HourData getHour10() { return hour10; }
    public HourData getHour11() { return hour11; }
    public HourData getHour12() { return hour12; }
    public HourData getHour13() { return hour13; }
    public HourData getHour14() { return hour14; }
    public HourData getHour15() { return hour15; }
    public HourData getHour16() { return hour16; }
    public HourData getHour17() { return hour17; }
    public HourData getHour18() { return hour18; }
    public HourData getHour19() { return hour19; }
    public HourData getHour20() { return hour20; }
    public HourData getHour21() { return hour21; }
    public HourData getHour22() { return hour22; }
    public HourData getHour23() { return hour23; }
    public HourData getHour24() { return hour24; }
    public HourData getHour25() { return hour25; }

    // Get all hours as array
    public HourData[] getAllHours() {
        return new HourData[]{
            hour1, hour2, hour3, hour4, hour5, hour6, hour7, hour8,
            hour9, hour10, hour11, hour12, hour13, hour14, hour15, hour16,
            hour17, hour18, hour19, hour20, hour21, hour22, hour23, hour24, hour25
        };
    }
    
    // Get non-null hours
    public HourData[] getNonNullHours() {
        return java.util.Arrays.stream(getAllHours())
                .filter(h -> h != null)
                .toArray(HourData[]::new);
    }
}