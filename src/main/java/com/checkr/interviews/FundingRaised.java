package com.checkr.interviews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FundingRaised {
    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = new FundingReader().getDados();
        String[] csvColumns = csvData.get(0);
        csvData.remove(0);

        csvData = new FundingFilter(csvData, options, csvColumns).aplicarFiltros();

        List<Map<String, String>> output = new ArrayList<>();

        for (String[] row : csvData){
            Map<String, String> mapped;
            mapped = new FundingMapper(csvColumns, row).mapperRow();
            output.add(mapped);
        }

        return output;
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException, NoSuchEntryException {
    List<String[]> csvData = new FundingReader().getDados();
    String[] csvColumns = csvData.get(0);
    csvData.remove(0);

    FundingFilter filter = new FundingFilter(csvData, options, csvColumns);
    Map<String, String> mapped = filter.filtrar();
    
    if (mapped == null) {
        throw new NoSuchEntryException();
    }
    
    return mapped;
}


    public static void main(String[] args) {
        try {
            Map<String, String> options = new HashMap<> ();
            options.put("company_name", "Facebook");
            options.put("round", "a");
            System.out.println(FundingRaised.where(options).size());
            System.out.println(FundingRaised.findBy(options));
        } catch(IOException e) {
            System.out.print(e.getMessage());
            System.out.print("error");
        } catch(NoSuchEntryException e){
            System.out.print(e.getMessage());
            System.out.print("error");
        }
    }
}

class NoSuchEntryException extends Exception {}