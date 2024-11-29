package com.checkr.interviews;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;

// SRP: Lida exclusivamente com leitura do CSV
class CsvReader {
    private final String filePath;

    public CsvReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> readData() throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> data = reader.readAll();
            return data.subList(1, data.size()); // Remove o cabeçalho
        }
    }
}

// SRP: Define critérios de filtragem
class FundingFilter {
    private final Map<String, String> criteria;

    public FundingFilter(Map<String, String> criteria) {
        this.criteria = criteria;
    }

    public boolean matches(String[] record) {
        return criteria.entrySet().stream().allMatch(entry -> {
            String field = entry.getKey();
            String value = entry.getValue();
            return switch (field) {
                case "company_name" -> record[1].equals(value);
                case "city" -> record[4].equals(value);
                case "state" -> record[5].equals(value);
                case "round" -> record[9].equals(value);
                default -> true;
            };            
        });
    }
}

// SRP: Mapeia registros para uma estrutura de dados mais clara
class FundingMapper {
    public static Map<String, String> mapRecord(String[] record) {
        Map<String, String> mapped = new HashMap<>();
        mapped.put("permalink", record[0]);
        mapped.put("company_name", record[1]);
        mapped.put("number_employees", record[2]);
        mapped.put("category", record[3]);
        mapped.put("city", record[4]);
        mapped.put("state", record[5]);
        mapped.put("funded_date", record[6]);
        mapped.put("raised_amount", record[7]);
        mapped.put("raised_currency", record[8]);
        mapped.put("round", record[9]);
        return mapped;
    }
}

// Serviço principal que utiliza os componentes acima
class FundingService {
    private final CsvReader csvReader;

    public FundingService(String filePath) {
        this.csvReader = new CsvReader(filePath);
    }

    public List<Map<String, String>> where(Map<String, String> criteria) throws IOException {
        List<String[]> csvData = csvReader.readData();
        FundingFilter filter = new FundingFilter(criteria);

        return csvData.stream()
                .filter(filter::matches)
                .map(FundingMapper::mapRecord)
                .collect(Collectors.toList());
    }

    public Map<String, String> findBy(Map<String, String> criteria) throws IOException, NoSuchEntryException {
        return where(criteria).stream()
                .findFirst()
                .orElseThrow(NoSuchEntryException::new);
    }
}

// Exceção personalizada para resultados não encontrados
class NoSuchEntryException extends Exception {
    public NoSuchEntryException() {
        super("No entry found matching the given criteria.");
    }
}

// Ponto de entrada
public class FundingRaised {
    public static void main(String[] args) {
        try {
            FundingService service = new FundingService("startup_funding.csv");
            Map<String, String> options = new HashMap<>();
            options.put("company_name", "Facebook");
            options.put("round", "a");

            System.out.println("Results: " + service.where(options).size());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
