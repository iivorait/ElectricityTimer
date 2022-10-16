package org.acme.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalculationResponse {
    
    public boolean active;
    public int resolution;
    public ArrayList<PricePoint> pricePoints;

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public ArrayList<PricePoint> getPricePoints() {
        return pricePoints;
    }
    public void setPricePoints(ArrayList<PricePoint> pricePoints) {
        this.pricePoints = pricePoints;
    }
    public int getResolution() {
        return resolution;
    }
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * Check if the current time is in the selected price points and set "active" property accordingly
     */
    public void activate() {
        this.active = false;
        ZonedDateTime now = ZonedDateTime.now();
        for (PricePoint pricePoint : pricePoints) {
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH).parse(pricePoint.getStartUTC() + "Z");
                ZonedDateTime startUTC = ZonedDateTime.ofInstant(start.toInstant(), ZoneId.of("Z"));
                ZonedDateTime stopUTC = startUTC.plusMinutes(resolution);
                if(now.isAfter(startUTC) && now.isBefore(stopUTC)) {
                    this.active = true;
                    System.out.println("ACTIVE: " + startUTC + " - " + stopUTC);
                    break;
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
}
