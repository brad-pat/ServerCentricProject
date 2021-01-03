package me.cw.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import sharesWeb.CompanyDetails;
import sharesWeb.CompanyDetails.SharePrice;

public class CompanyDataManager {
    
    private String symb;
    
    public CompanyDataManager(String symbol)
    {
        symb = symbol;
        //this.updateCompanyDetailsOnlineViaAPI(symb);
    }
    
    

    
    
    private void updateStockInformation(String symbol, int shares, double price, String currency, String lastupdated)
    {
        CompanyDetails org_comp = CompanyManager.getInstance().getCompanyDetailsBySymbol(symbol);
        String name = org_comp.getName();
        ArrayList<CompanyDetails> list = CompanyManager.getInstance().company_details;
        int index = list.indexOf(org_comp); list.remove(index);
        CompanyDetails new_comp = new CompanyDetails();
        SharePrice sp = new SharePrice();
        new_comp.setName(name);
        new_comp.setShares(shares);
        new_comp.setSymbol(symbol);
        sp.setShareCurrency(currency);
        sp.setShareValue(price);
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        try
        {
            Date date = format.parse(lastupdated);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar xmlc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            sp.setShareLastUpdated(xmlc);
        }
        catch(ParseException e)
        {
            
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        new_comp.setSharePrice(sp);
        CompanyManager.getInstance().company_details.add(new_comp);
    }
    

}
