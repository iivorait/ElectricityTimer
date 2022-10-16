package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.acme.models.CalculationResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Path("/calculate")
public class ElectricityResource {

    @Inject
    @RestClient
    EntsoeIntegrationAPI entsoe;

    @Inject
    CalculatorService calculator;

    @ConfigProperty(name = "areacode")
    public String areacode;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CalculationResponse calc(
            @QueryParam("minutes") int minutes, 
            @QueryParam("blocks") int blocks, 
            @QueryParam("minprice") float minprice,
            @QueryParam("cheapmode") int cheapmode
        ) {
        //TODO: minimum runtime, for example 30min when the 15min resolution is in use
        return calculator.calc(minutes, blocks, minprice, cheapmode, entsoe.getDapList(areacode));
    }

}