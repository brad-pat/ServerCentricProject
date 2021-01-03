package me.cw.main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import sharesWeb.CompanyDetails;

public class Main {


    public static void main(String[] args)
    {
        new XMLReader();
        //new Login_Screen();
        makehashmapGBP();
        new XMLWriter();
        new Shares_Page();
        //loop();
        //new CurrencyManager("GBP");       
    }
    
    /*
    public static void loop()
    {
        HashMap<String, Double> map = CompanyManager.getInstance().conversion_rates;
        for(String s : map.keySet())
        {
            double value = map.get(s);
            offline(s, value + "");
        }
    }
    
    public static void offline(String currency, String value)
    {
        try
        {
            URL url = new URL("http://localhost:8080/OfflineUseAPI/webresources/OfflineUseAPI?query=update&currency=" + currency + "&value=" + value);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() != 200) {
                throw new IOException(con.getResponseMessage());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inString;
            StringBuilder sb = new StringBuilder();
            while ((inString = in.readLine()) != null) {
                sb.append(inString + "\n");
            }    
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }        
    
    }
   */
    /**
    public static void restful_stocks()
    {
        try
        {
        URL url = new URL("https://api.worldtradingdata.com/api/v1/stock_search?currency=GBP&page=25&stock_exchange=LSE&api_token=2qhyw2p601hnTrUEmT1t5GHQx6Sb3aKYYeNIN2KNXn8nAqn7htZ2kj1TjVs9");
     // URL url = new URL("https://api.worldtradingdata.com/api/v1/stock?symbol=FLXD.L&api_token=2qhyw2p601hnTrUEmT1t5GHQx6Sb3aKYYeNIN2KNXn8nAqn7htZ2kj1TjVs9");
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        if (con.getResponseCode() != 200) {
            throw new IOException(con.getResponseMessage());
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inString;
        StringBuilder sb = new StringBuilder();
        while ((inString = in.readLine()) != null) {
            sb.append(inString + "\n");
        }
            //System.out.println(sb.toString());
            String input = sb.toString();
            String[] exclude = input.split("\\[");
            String exc = exclude[1];
            String[] exlu = exc.split("\\]");
            String fin = exlu[0];
            String[] data = fin.split("\\},\\{");
            for(String s : data)
            {
                //System.out.println(s);
                String[] details = s.split(",");
                
                 * 0 = Symbol
                 * 1 = Name
                 * 2 = Currency 
                 * 3 = Price
                 *
                String comp_name = "";
                String comp_symb = "";
                String comp_curr = "";
                String comp_pric = "";
                for(String deets : details)
                {
                    //System.out.println(deets);
                    String[] colonsplit = deets.split(":");
                    String detail = colonsplit[0];
                    String thedata = colonsplit[1];
                    String[] removespe = thedata.split("\"");
                    String actual = removespe[1];
                    if(detail.contains("name"))
                    { 
                        comp_name = actual;
                        //System.out.println(actual + " : " + comp_name);
                    }
                    if(detail.contains("symbol"))
                    {
                        comp_symb = actual;
                        //System.out.println(actual + " : " + comp_symb);  
                    }
                    if(detail.contains("currency"))
                    {
                        comp_curr = actual;
                        //System.out.println(actual + " : " + comp_curr); 
                    }
                    if(detail.contains("price"))
                    {
                        comp_pric = actual;
                        //System.out.println(actual + " : " + comp_pric);  
                    }
                }
                    //System.out.println("Details so far for " + comp_name + ": Symbol: " + comp_symb + " Curr: " + comp_curr + " Price: " + comp_pric);
                    //getOtherDetails(comp_name, comp_symb, comp_curr, comp_pric);
                    //System.out.println(comp_symb);
                    updateCompanyDetailsOnlineViaAPI(comp_symb);
            }
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    } */
    
    public static void create(String name, String symbol, int shares, double price, String currency, String lastupdated)
    {
        CompanyDetails comp = new CompanyDetails();
        CompanyDetails.SharePrice sp = new CompanyDetails.SharePrice();
        comp.setName(name);
        comp.setSymbol(symbol);
        comp.setShares(shares);
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
            //sp.getShareLastUpdated();
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        comp.setSharePrice(sp);
        
        CompanyManager.getInstance().company_details.add(comp);
    }
  
    private static void print(CompanyDetails company) {
        System.out.println("---------------------------");
        System.out.println("Company Name: " + company.getName());
        System.out.println("Company Symbol: " + company.getSymbol());
        System.out.println("Company Stocks: " + company.getShares());
        CompanyDetails.SharePrice sp = company.getSharePrice();
        System.out.println("Share Price: " + sp.getShareValue() + "" + sp.getShareCurrency() + " Last Updated: " + sp.getShareLastUpdated());
    }
    
    private static void makehashmapGBP()
    {
        List<String> codes = getCurrencyCodes();
        for(String s : codes)
        {
            String code = s.split("-")[0].trim();
            double conversion = getConversionRate("GBP", code);
            CompanyManager.getInstance().conversion_rates.put(code, conversion);
            System.out.println("HashMap: " + code + " : " + conversion);
        }
    }

        // CODE AND CODE
    private static java.util.List<java.lang.String> getCurrencyCodes() {
        docwebservices.CurrencyConversionWSService service = new docwebservices.CurrencyConversionWSService();
        docwebservices.CurrencyConversionWS port = service.getCurrencyConversionWSPort();
        return port.getCurrencyCodes();
    }

    private static double getConversionRate(java.lang.String arg0, java.lang.String arg1) {
        docwebservices.CurrencyConversionWSService service = new docwebservices.CurrencyConversionWSService();
        docwebservices.CurrencyConversionWS port = service.getCurrencyConversionWSPort();
        return port.getConversionRate(arg0, arg1);
    }
    
}
