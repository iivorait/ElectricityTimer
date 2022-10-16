package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.acme.models.PriceList;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

// @ApplicationScoped
@Path("/")
@RegisterRestClient
public interface EntsoeIntegrationAPI {

    @GET
    @Path("dap/{areacode}/list")
    //@Produces("PriceList.class")
    PriceList getDapList(@PathParam("areacode") String areacode);
}