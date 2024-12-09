package com.checkr.interviews;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class FundingReader{    
    public FundingReader(){
        
    }

    public List<String[]> getDados() throws IOException {
        List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(("startup_funding.csv")))) {
            String[] row;
            
            while((row = reader.readNext()) != null) {
                csvData.add(row);
            }
        }

        return csvData;
    }
}