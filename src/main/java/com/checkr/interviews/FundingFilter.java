package com.checkr.interviews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FundingFilter {
    public List<String[]> data;
    public Map<String, String> options;
    public String[] colunas;

    public FundingFilter(List<String[] > data, Map<String, String> options, String[] colunas){
        this.data = data;
        this.options = options;
        this.colunas = colunas;
    }

    public List<String[]> aplicarFiltros() throws IOException{
        List<String[]> filteredData = new ArrayList<>(data);
        for(String key: options.keySet()){
            List<String[]> results = new ArrayList<>();
            int index = Arrays.asList(colunas).indexOf(key);
            if(index == -1){ continue; }
            for(String[] row: filteredData){
                if(row[index].equals(options.get(key))){
                    results.add(row);
                }
            }
            filteredData = results;
        }
        return filteredData;
    }
    

    public Map<String, String> filtrar() throws IOException, NoSuchEntryException{
        Map<String, String> mapped;
        for(String[] row : data){
            boolean match = true;
            for(String key: options.keySet()){
                int index = Arrays.asList(colunas).indexOf(key);
                if(index == -1){ 
                    match = false;
                    break; 
                }
                if(!row[index].equals(options.get(key))){
                    match = false;
                    break;
                }
            }

            if (match){
                mapped = new FundingMapper(colunas, row).mapperRow();
                return mapped;
            }
        }
        
        throw new NoSuchEntryException();
        
    }
}