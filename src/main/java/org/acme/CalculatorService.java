package org.acme;

import java.util.ArrayList;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;

import org.acme.models.PriceList;
import org.acme.models.PricePoint;
import org.acme.models.CalculationResponse;

@ApplicationScoped
public class CalculatorService {
    
    public CalculationResponse calc(int minutes, int blocks, float minprice, int cheapmode, PriceList pricelist) {
        int resolution = Integer.parseInt(pricelist.getResolution(), 2, 4, 10); //60 or 15 minutes
        
        ArrayList<PricePoint> selected = new ArrayList<>();

        //if blocks (quarters) are not active, find lowest prices amongst all prices...
        if(blocks <= 1) {
            selectCheapestPoints(minutes, pricelist.getPrices(), resolution, selected);
            
        } else { //...but if blocks (quarters) are active, split the day into blocks
            ArrayList<PricePoint> firstRun = new ArrayList<>();
            ArrayList<ArrayList<PricePoint>> blocksArray = splitToBlocks(blocks, pricelist); 
            for (ArrayList<PricePoint> prices : blocksArray) {
                selectCheapestPoints(minutes / blocks, prices, resolution, firstRun);
            }
            //There can be some extra blocks due to divisioning not being the same as the price resolution, so remove extra selections in cheap mode
            if(cheapmode == 1) {
                selectCheapestPoints(minutes, firstRun, resolution, selected);
            } else {
                selected = firstRun;
            }
        }

        //if minimum limit is active, add those
        for (PricePoint pricePoint : pricelist.getPrices()) {
            //if the price is below the minimum limit, select this
            if(pricePoint.getPrice() <= minprice) {
                selected.add(pricePoint);
            }
        }

        CalculationResponse response = new CalculationResponse();
        response.setResolution(resolution);
        selected.sort((o1, o2) -> o1.getStartUTC().compareTo(o2.getStartUTC()));
        response.setPricePoints(selected);
        response.activate();
        return response;
    }

    private void selectCheapestPoints(int minutes, ArrayList<PricePoint> pricelist, int resolution, ArrayList<PricePoint> selected) {
        int selectedAmount = 0;
        pricelist.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));
        for (PricePoint pricePoint : pricelist) {
            selected.add(pricePoint);
            selectedAmount += resolution;
            if(selectedAmount >= minutes) {
                break;
            }
        }
    }

    private ArrayList<ArrayList<PricePoint>> splitToBlocks(int blocks, PriceList pricelist) {
        int blocksize = pricelist.getPrices().size() / blocks;
        ArrayList<ArrayList<PricePoint>> blockArray = new ArrayList<>();
        for(int i=0;i < pricelist.getPrices().size(); i += blocksize){
            PricePoint original[] = pricelist.getPrices().toArray(new PricePoint[1]);
            PricePoint block[] = Arrays.copyOfRange(original, i, Math.min(original.length,i+blocksize));
            blockArray.add(new ArrayList<PricePoint>(Arrays.asList(block)));
        }
        return blockArray;
    }

}
