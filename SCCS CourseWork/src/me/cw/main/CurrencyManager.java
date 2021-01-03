package me.cw.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CurrencyManager {
    
    private String currency;
    private List<String> country_codes;
    
    public CurrencyManager(String base_currency)
    {
        currency = base_currency;
        country_codes = getCurrencyCodes();
        this.getLatestExchangeRatesOnBase(currency);
    }
    
    private void getLatestExchangeRatesOnBase(String base)
    {
        try
        {
            URL url = new URL("https://api.exchangeratesapi.io/latest?base=" + base);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if(con.getResponseCode() != 200) {throw new IOException(con.getResponseMessage());}
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inString;
            StringBuilder sb = new StringBuilder();
            while((inString = in.readLine()) != null)
            {
                sb.append(inString + "\n");
            }
            String input = sb.toString();
            String[] exclude = input.split("\\{");
            String everything = exclude[2];
            String[] exclude2 = everything.split("\\}");
            String everything2 = exclude2[0];
            String[] currencies = everything2.split(",");
            for(String s : currencies)
            {
                //System.out.println(s);
                String[] cu = s.split("\"");
                String cuu = cu[1] + cu[2];
                //System.out.println(cuu);
                String symbol = cuu.split(":")[0].trim();
                double value = Double.parseDouble(cuu.split(":")[1]);
                if(CompanyManager.getInstance().conversion_rates.containsKey(symbol))
                {
                    //System.out.println("Updating Map. " + symbol + " - " + CompanyManager.getInstance().conversion_rates.get(symbol) + " to " + value);
                    CompanyManager.getInstance().conversion_rates.remove(symbol);
                    CompanyManager.getInstance().conversion_rates.put(symbol, value);
                }
            }
            
            /**
            String input = sb.toString();
            String[] sqw = input.split(":");
            String test = sqw[1];
            String[] splt = input.split(",");
            for(String s : splt)
            {
                System.out.println(s.trim());
            }
            *
            * */
            in.close();
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
