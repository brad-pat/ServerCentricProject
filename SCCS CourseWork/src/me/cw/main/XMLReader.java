package me.cw.main;

import java.util.Iterator;
import java.util.List;
import sharesWeb.CompanyDetails;
import sharesWeb.SharesCollection;

public class XMLReader {

    public XMLReader() {
        SharesCollection shares = new SharesCollection();
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(shares.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            shares = (SharesCollection) unmarshaller.unmarshal(new java.io.File("Share_Prices.xml")); //NOI18N
            List<CompanyDetails> shares_list = shares.getSharesList();
            CompanyDetails company;
            Iterator intt = shares_list.iterator();
            while (intt.hasNext()) {
                company = (CompanyDetails) intt.next();
                CompanyManager.getInstance().company_details.add(company);
                //print(company);
            }
        } 
        catch (javax.xml.bind.JAXBException ex) 
        {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
    }

    private void print(CompanyDetails company) {
        System.out.println("---------------------------");
        System.out.println("Company Name: " + company.getName());
        System.out.println("Company Symbol: " + company.getSymbol());
        System.out.println("Company Stocks: " + company.getShares());
        CompanyDetails.SharePrice sp = company.getSharePrice();
        System.out.println("Share Price: " + sp.getShareValue() + "" + sp.getShareCurrency() + " Last Updated: " + sp.getShareLastUpdated());
    }
}