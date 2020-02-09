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

    public List<String> getAllWordList(String text, List<Integer> typesOfSpeech, boolean isSimpleMode) {
        String textClr = text.replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\"", "");
        TextParser parser = new TextParser(textClr);

        if (nonNull(typesOfSpeech) && typesOfSpeech.size() > 0) {
            List<String> allWords = new ArrayList<>();
            for (String word : parser.getWordsInText()) {
                if (isNeededTypeOfSpeech(word, typesOfSpeech, isSimpleMode)) {
                    allWords.add(word);
                }
            }
            return allWords;
        }

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

    public Map<String, Long> getWordList(String text, List<Integer> typesOfSpeech, boolean isSimpleMode) {
        String textClr = text.replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\"", "");
        TextParser parser = new TextParser(textClr);
        List<String> allWords = parser.getWordsInText();
        Map<String, Long> words = new HashMap<String, Long>();
        var jMorfSdk = morfologyService.getjMorfSdk();

        for (String word : allWords) {
            if (isNeededTypeOfSpeech(word, typesOfSpeech, isSimpleMode)) {
                List<String> initialForms = jMorfSdk.getStringInitialForm(word);
                String initialForm = nonNull(initialForms) && initialForms.size() > 0 ? initialForms.get(0) : word;

                if (words.containsKey(initialForm)) {
                    words.put(initialForm, words.get(initialForm) + 1);
                } else {
                    words.put(initialForm, 1L);
                }
            }
        }
        return words;
    }

    boolean isNeededTypeOfSpeech(String word, List<Integer> searchTypesOfSpeech, boolean isSimpleMode) {
        var jMorfSdk = morfologyService.getjMorfSdk();
        List<Byte> typesOfSpeech = jMorfSdk.getTypeOfSpeeches(word);
        if (typesOfSpeech.size() == 0) {
            return false;
        } else if (isSimpleMode) {
            return searchTypesOfSpeech.contains((int)typesOfSpeech.get(0));
        } else {
            return typesOfSpeech.stream().anyMatch(type -> searchTypesOfSpeech.contains((int) type));
        }
    }
}
