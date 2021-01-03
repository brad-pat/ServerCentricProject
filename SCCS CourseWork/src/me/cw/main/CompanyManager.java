package me.cw.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import sharesWeb.CompanyDetails;

public class CompanyManager {
    
    private static CompanyManager instance = null;
    private CompanyManager() {}
    public static CompanyManager getInstance()
    {
        if(instance == null) { instance = new CompanyManager(); }
        return instance;
    }
    /*
    * TOKEN DEPENDS ON DEMO DATE    
    */
    String token = "KcvHRUvd9bGLnKfUeHyAoyJxez8n34TvxitKns5TbckXEGLx09ZmHvi1sgbt";
    //String token = "2qhyw2p601hnTrUEmT1t5GHQx6Sb3aKYYeNIN2KNXn8nAqn7htZ2kj1TjVs9";
    public ArrayList<CompanyDetails> company_details = new ArrayList<>();
    public HashMap<String, Double> conversion_rates = new HashMap<String, Double>();
    //public ArrayList<CompanyDetails> post_converted_comp = new ArrayList<>();
    
    public double getConvRatesFromAPI(String name)
    {
        try
        {
            URL url = new URL("http://localhost:8080/OfflineUseAPI/webresources/OfflineUseAPI?query=fetch&currency=" + name);
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
            String s = sb.toString();
            //System.out.println(s);
            String result = s.split(":")[0].split(">")[3]; String value = s.split(":")[1].split("<")[0];
            //System.out.println(result + " | " + value);
            
            if(result.equalsIgnoreCase("VALUE"))
            {
                return Double.parseDouble(value);
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
        return 0;
    }
    
    public void addCompany(CompanyDetails company)
    {
        company_details.add(company);
    }
    
    public void deleteCompany(CompanyDetails company)
    {
        int index = company_details.indexOf(company);
        company_details.remove(index);
    }
    
    /**
     * SEARCH BAR LEFT
     * @param name
     * @return 
     */
    
    public CompanyDetails getCompanyDetailsByName(String name)
    {
        for(CompanyDetails company : company_details)
        {
            String company_name = company.getName();
            if(company_name.equalsIgnoreCase(name))
            {
                return company;
            }
        }
        return null;
    }
    /**
     * SEARCH BAR RIGHT
     * @param symbol
     * @return 
     */
    public CompanyDetails getCompanyDetailsBySymbol(String symbol)
    {
        for(CompanyDetails company : company_details)
        {
            String company_symbol = company.getSymbol();
            if(company_symbol.equalsIgnoreCase(symbol))
            {
                return company;
            }
        }
        return null;
    }
   
    /**
     * MIDDLE BOTTOM BUTTON
     * @return 
     */
    public ArrayList<CompanyDetails> orderBySharesLowToHigh()
    {
        ArrayList<CompanyDetails> sorted_list = new ArrayList<>();
        for(CompanyDetails company : company_details)
        {
            sorted_list.add(company);
        }
        Collections.sort(sorted_list, new Comparator<CompanyDetails>()
        {
            @Override
            public int compare(CompanyDetails c1, CompanyDetails c2)
            {
                Long comp1 = new Long(c1.getShares());
                Long comp2 = new Long(c2.getShares());
                return comp1.compareTo(comp2);
            }
        });
        return sorted_list;
    }
       
    /**
     * MIDDLE TOP BUTTON
     * @return 
     */
    public ArrayList<CompanyDetails> orderBySharesHighToLow()
    {
        ArrayList<CompanyDetails> sorted_list = new ArrayList<>();
        for(CompanyDetails company : company_details)
        {
            sorted_list.add(company);
        }
        Collections.sort(sorted_list, new Comparator<CompanyDetails>()
        {
            @Override
            public int compare(CompanyDetails c1, CompanyDetails c2)
            {
                Long comp1 = new Long(c1.getShares());
                Long comp2 = new Long(c2.getShares());
                return comp2.compareTo(comp1);
            }
        });
        return sorted_list;
    }
    
    /**
     * LEFT BOTTOM BUTTON
     * @return 
     */
    public ArrayList<CompanyDetails> orderByLowestSharePrices()
    {
        ArrayList<CompanyDetails> sorted_list = new ArrayList<>();
        for(CompanyDetails company : company_details)
        {
            sorted_list.add(company);
        }
        Collections.sort(sorted_list, new Comparator<CompanyDetails>()
        {
            @Override
            public int compare(CompanyDetails c1, CompanyDetails c2)
            {
                sharesWeb.CompanyDetails.SharePrice sp1 = c1.getSharePrice();
                sharesWeb.CompanyDetails.SharePrice sp2 = c2.getSharePrice();
                Double comp1 = sp1.getShareValue();
                Double comp2 = sp2.getShareValue();
                return comp1.compareTo(comp2);
            }
        });   
        return sorted_list;
    }
        
    /**
     * LEFT TOP BUTTON
     * @return 
     */
    public ArrayList<CompanyDetails> orderByHighestSharePrices()
    {
        ArrayList<CompanyDetails> sorted_list = new ArrayList<>();
        for(CompanyDetails company : company_details)
        {
            sorted_list.add(company);
        }
        Collections.sort(sorted_list, new Comparator<CompanyDetails>()
        {
            @Override
            public int compare(CompanyDetails c1, CompanyDetails c2)
            {
                sharesWeb.CompanyDetails.SharePrice sp1 = c1.getSharePrice();
                sharesWeb.CompanyDetails.SharePrice sp2 = c2.getSharePrice();
                Double comp1 = sp1.getShareValue();
                Double comp2 = sp2.getShareValue();
                return comp2.compareTo(comp1);
            }
        });   
        return sorted_list;
    }
    
    public void updateCompanyDetailsOnlineViaAPI(String symbol)
    {
        try
        {
            //System.out.println(symbol);
            URL url = new URL("https://api.worldtradingdata.com/api/v1/stock?symbol=" + symbol + "&api_token=" + token);
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
            //String timezone = "";
            //String time = "";
            //String shares = "";
            String price = "";
            //String currency = "";
            //String name = "";
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
                for(String ss : details)
                {
                    //System.out.println(ss);
                    String[] colonsplit = ss.split(":");
                    String detail = colonsplit[0];
                    String thedata = colonsplit[1];
                    String[] removespe = thedata.split("\"");
                    String actual = removespe[1];
                    /*
                    if(detail.contains("\"name\""))
                    {
                        name = actual;
                    }
                    if(detail.contains("shares"))
                    {
                        shares = actual;
                        //System.out.println(actual + " : " + shares);
                    }
                    if(detail.contains("last_trade_time"))
                    {
                        time = actual;
                        //System.out.println(actual + " : " + time);
                    }
                    if(detail.contains("currency"))
                    {
                        currency = actual;
                    }
*/
                    if(detail.contains("\"price\""))
                    {
                        price = actual;
                        //System.out.println(actual);
                    }
                }
                //System.out.println("FINAL DETAILS:");
                
                int share = 0;
                double value = 0;
                try
                {                   
                    value = Double.parseDouble(price.trim());
                }                
                catch(NumberFormatException e)
                {
                    System.out.println(e.getMessage());
                }
                /*
                try
                {
                    share = Integer.parseInt(shares.trim());
                }
                catch(NumberFormatException e)
                {                
                }
                */
                //System.out.println("Name: " + name + " Symbol: " + symbol + " Shares: " + share + " Price: " + value + " Currency: " + currency + " Last Updated: " + time);
                //updateStockInformation(symbol, share, value, currency, time);
                //create(name, symbol, share, value, currency, time);
                //System.out.println("\nURL: " + url);
                CompanyDetails cd = getCompanyDetailsBySymbol(symbol);
                double old = cd.getSharePrice().getShareValue();
                cd.getSharePrice().setShareValue(value);
                System.out.println("Updated share value for symbol: " + symbol + " From: " + old + " To: " + value);
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
}