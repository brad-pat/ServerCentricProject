package me.cw.main;

import java.io.File;
import java.util.List;
import sharesWeb.CompanyDetails;
import sharesWeb.SharesCollection;

public class XMLWriter {
    
    public XMLWriter()
    {
        SharesCollection collection = new SharesCollection();
        List<CompanyDetails> company_list = CompanyManager.getInstance().company_details;
        for(CompanyDetails company : company_list)
        {
            if(company.getSharePrice().getShareValue() != 0)
            {
                System.out.println("Saving Company: " + company.getName() + " - (" + company.getSymbol() + ") " + " - Shares: " + company.getShares() + " - Value: " + company.getSharePrice().getShareValue()) ;
                collection.getSharesList().add(company);
            }
        }
        File f = new File("Share_prices.xml");
        try 
        {            
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(collection.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(collection, f);
        } 
        catch (javax.xml.bind.JAXBException ex) 
        {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
    }
}
