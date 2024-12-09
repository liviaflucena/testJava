package com.checkr.interviews;

import java.util.HashMap;
import java.util.Map;

public class FundingMapper {
    public String[] csvColumns;
    public String[] csvRow;

    public FundingMapper(String[] csvColumns, String[] csvRow){
        this.csvColumns = csvColumns;
        this.csvRow = csvRow;
    }

    public Map<String, String> mapperRow(){
        Map<String, String> mapped = new HashMap<>();
        for(int i=0; i<csvColumns.length; i++){
            mapped.put(csvColumns[i], csvRow[i]);
        }
        return mapped;
    }
    
}