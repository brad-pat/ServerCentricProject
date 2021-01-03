/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offlineRESTws;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.netbeans.xml.schema.shares.CurrenciesCollection;
import org.netbeans.xml.schema.shares.CurrencyValueToGBP;

/**
 * REST Web Service
 *
 * @author brad
 */
@Path("OfflineUseAPI")
public class Main {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Main
     */
    public Main() {
    }
    
    ArrayList<CurrencyValueToGBP> values = new ArrayList<CurrencyValueToGBP>();
    /**
     * Retrieves representation of an instance of offlineRESTws.Main
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    //Use Exchange rates
    //Update, Fetch
    //Update: Currency, Value.
    //Fetch: Currency
    public String getHtML(@QueryParam("query")String query, @QueryParam("currency")String currency, @QueryParam("value")String value) {
        unmarshalling();
        if(query.equalsIgnoreCase("update"))
        {
            //http://localhost:8080/OfflineUseAPI/webresources/OfflineUseAPI?query=update&currency=gBP&value=1
            if(currency != null && value != null)
            {
                CurrencyValueToGBP val = getObjectByName(currency);
                if(val != null)
                {
                    val.setValue(Double.parseDouble(value));
                }
                else
                {
                    val = new CurrencyValueToGBP();
                    val.setSymbol(currency);
                    val.setValue(Double.parseDouble(value));
                    values.add(val);
                }
                marshalling();
                return "<html><body><h1>DONE:" +  currency + "</body></h1></html>";
            }
        }
        if(query.equalsIgnoreCase("fetch"))
        {
            //http://localhost:8080/OfflineUseAPI/webresources/OfflineUseAPI?query=fetch&currency=GBP
            CurrencyValueToGBP val = getObjectByName(currency);
            if(val != null)
            {
                return "<html><body><h1>VALUE:" +  val.getValue() + "</body></h1></html>";
            }
            else
            {
                return "<html><body><h1>ERROR:NOT FOUND</body></h1></html>";
            }
            //double val = values.get(currency);
            
        }
        
        return "<html><body><h1>NOTHING</body></h1></html>";
    }
    
    public void exists()
    {
        
    }
    
    public void unmarshalling()
    {
        CurrenciesCollection cc = new CurrenciesCollection();
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(cc.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            cc = (CurrenciesCollection) unmarshaller.unmarshal(new java.io.File("Currencies.xml")); //NOI18N
            List<CurrencyValueToGBP> list = cc.getCurrencies();
            CurrencyValueToGBP conv;
            Iterator intt = list.iterator();
            while(intt.hasNext())
            {
                conv = (CurrencyValueToGBP) intt.next();
                values.add(conv);
            }
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        
        
    }
    
    public CurrencyValueToGBP getObjectByName(String name)
    {
        for(CurrencyValueToGBP c : values)
        {
            if(c.getSymbol().equalsIgnoreCase(name))
            {
                return c;
            }
        }
        return null;
    }
    
    public void marshalling()
    {
        CurrenciesCollection cc = new CurrenciesCollection();
        for(CurrencyValueToGBP c : values)
        {
            cc.getCurrencies().add(c);
            
        }
        
        File f = new File("Currencies.xml");
        try {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(cc.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(cc, f);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
    }

    /**
     * PUT method for updating or creating an instance of Main
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(@QueryParam("CurrencyAbb")String content) {

    }
}
