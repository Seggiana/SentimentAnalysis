package sample.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {
    private final Map<String, Integer> dictionaryAFINN;
    private final Map<String, Integer> dictionaryLexicon;
    private List<String> negativeDictionary;

    public Dictionary() {
        this.dictionaryAFINN = new HashMap<>();
        this.dictionaryLexicon = new HashMap<>();
        initializeNegativeDictionary();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\AFINN"), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                dictionaryAFINN.put(values[0], Integer.parseInt(values[1]));
            }
        } catch (
                IOException ioe) {
            ioe.printStackTrace();
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\Lexi"), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                dictionaryLexicon.put(values[0], Integer.parseInt(values[1]));
            }
        } catch (
                IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void initializeNegativeDictionary() {
        this.negativeDictionary = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\Negative"), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                negativeDictionary.add(values[0]);
            }
        } catch (
                IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isNegative(String s) {
        return negativeDictionary.contains(s);
    }

    public boolean calculateValue(List<String> firstParam, boolean isAFINN) {
        int predictedValue = 0;
        for (String s : firstParam) {
            if (isAFINN) {
                if (dictionaryAFINN.containsKey(s)) {
                    if ((firstParam.indexOf(s) - 1) != -1 &&
                            isNegative(firstParam.get(firstParam.indexOf(s) - 1))) {
                        predictedValue -= dictionaryAFINN.get(s);
                    } else {
                        predictedValue += dictionaryAFINN.get(s);
                    }
                }
            } else {
                if (dictionaryLexicon.containsKey(s)) {
                    if ((firstParam.indexOf(s) - 1) != -1 &&
                            isNegative(firstParam.get(firstParam.indexOf(s) - 1))) {
                        predictedValue -= dictionaryLexicon.get(s);
                    } else {
                        predictedValue += dictionaryLexicon.get(s);
                    }
                }
            }

        }
        return predictedValue >= 0;
    }
}
