package com.theia.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PMDService {

//    public HashMap<String, Float> pmdValues(Double loc, String path) throws IOException, InterruptedException {
//        List<String> rulesets = new ArrayList<>(Arrays.asList("NullPointer", "Adjustability", "Assignment", "ExceptionHandling", "Logging", "MisusedFunctionality", "ResourceHandling"));
//        HashMap<String, Float> pmdValues = new HashMap<>();
//
//        for(String ruleset: rulesets){
//            pmdValues.put(ruleset, 0f);
//            Process process = Runtime.getRuntime().exec( System.getenv("HOME") +"/pmd-bin-6.30.0/bin/run.sh pmd -d " + path + " -R " + Path.of("").toAbsolutePath().toString() + "/Rulesets/" + ruleset +".xml -f csv -r " + path + "/" + ruleset +".csv");
//            List<List<String>> records = new ArrayList<List<String>>();
//
//            File file = new File(path  + ruleset + ".csv");
//
//            try {
//                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//
//                while(true){
//                    line = r.readLine();
//                    if(line == null){
//                        break;
//                    }
//                }
//            }catch (IOException e){
//                System.out.println(e.getMessage());
//            }
//
//
//            try (CSVReader csvReader = new CSVReader(new FileReader(path + "/" + ruleset + ".csv"));) {
//                String[] values = null;
//
//                while ((values = csvReader.readNext()) != null) {
//                    records.add(Arrays.asList(values));
//                }
//            } catch (CsvValidationException | FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if(records.size() == 0){
//                continue;
//            }else{
//                pmdValues.put(ruleset, MeasureService.measurePMDProperties(filterRecords(records), loc));
//            }
//        }
//
//        return pmdValues;
//    }

    public List<String> filterRecords(List<List<String>> records){
        records.remove(0);
        return records.stream().map(x -> x.get(x.size() -1)).collect(Collectors.toList());
    }

//   Services accepting user input producing results.
    public HashMap<String, Double> generateCustomPMDValues(Double loc, String path, List<String> rulesets) throws IOException {
        HashMap<String, Double> pmdValues = new HashMap<>();
        for(String ruleset: rulesets){
            pmdValues.put(ruleset, 0d);
            Process process = Runtime.getRuntime().exec( System.getenv("HOME") +"/pmd-bin-6.30.0/bin/run.sh pmd -d " + path + " -R " + Path.of("").toAbsolutePath().toString() + "/Rulesets/" + ruleset +".xml -f csv -r " + path + "/" + ruleset +".csv");
            List<List<String>> records = new ArrayList<List<String>>();

            File file = new File(path  + "/" + ruleset + ".csv");

            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while(true){
                    line = r.readLine();
                    if(line == null){
                        break;
                    }
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


            try (CSVReader csvReader = new CSVReader(new FileReader(path + "/" + ruleset + ".csv"));) {
                String[] values = null;

                while ((values = csvReader.readNext()) != null) {
                    records.add(Arrays.asList(values));
                }
            } catch (CsvValidationException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(records.size() == 0){
                continue;
            }else{
                pmdValues.put(ruleset, MeasureService.measurePMDProperties(filterRecords(records), loc));
            }
        }

        return pmdValues;
    }

}
