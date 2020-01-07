package ru.textanalysis.wordslist.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.textanalysis.wordslist.util.TextParser;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class TextProcessingService {

    private final MorfologyService morfologyService;

    public List<String> getAllWordList(String text) {
        String textClr = text.replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\"", "");
        TextParser parser = new TextParser(textClr);
        return parser.getWordsInText();
    }

    public Map<String, Long> getWordList(String text) {
        String textClr = text.replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\"", "");
        TextParser parser = new TextParser(textClr);
        List<String> allWords = parser.getWordsInText();
        Map<String, Long> words = new HashMap<String, Long>();
        var jMorfSdk = morfologyService.getjMorfSdk();

        for (String word : allWords) {
            List<String> initialForms = jMorfSdk.getStringInitialForm(word);
            String initialForm = nonNull(initialForms) && initialForms.size() > 0 ? initialForms.get(0) : word;

            if (words.containsKey(initialForm)) {
                words.put(initialForm, words.get(initialForm) + 1);
            } else {
                words.put(initialForm, 1L);
            }
        }
        return words;
    }
}
