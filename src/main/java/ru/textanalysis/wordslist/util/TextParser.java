package ru.textanalysis.wordslist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {

    private final List<List<String>> wordsInSentence;
    private final List<String> sentencesInText;
    private final List<String> wordsInText;

    private String text = "";

    public TextParser(String text) {
        wordsInSentence = new ArrayList<>();
        sentencesInText = new ArrayList<>();
        wordsInText = new ArrayList<>();
        this.text = text.replaceAll("\n", " ")
                .replaceAll("\r", " ")
                .replaceAll("\t", " ")
                .replaceAll("\"", "");
        splitTextBySentences();
        splitSentenceByWords();
        getWordsOfText();
    }

    private void getWordsOfText() {
        for (List<String> sentense : wordsInSentence) {
            sentense.stream().forEach(word -> wordsInText.add(word));
        }
    }

    private void splitSentenceByWords() {
        Pattern pattern = Pattern.compile(", |\\.\\.?\\.? ?|! ?|\\? ?| —?\\(?\\[?\\{?<?<?«? ?|; |: |\\)?\\]?\\}?>?>?»?,?\\.?;?:?!?\\?? ");
        for (int i = 0; i < sentencesInText.size(); i++) {
            wordsInSentence.add(new ArrayList<String>());
            String[] words = pattern.split(sentencesInText.get(i));
            for (String word : words) {
                if (!word.equals("")) {
                    wordsInSentence.get(i).add(word.toLowerCase());
                }
            }
        }
    }

    private void splitTextBySentences(){
        Pattern pattern = Pattern.compile("([А-Я]|[\\d*]).+(([А-Я]\\.)+\\s[А-Я].+)?[\\. |\\! |\\? | \\... ]");
        String[] sentencesArray;

        try {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                sentencesArray = text.split("(?<=\\. |! |\\? | \\... )");
                if (sentencesArray.length > 1) {
                    for (int j = 0; j < sentencesArray.length; j++) {
                        sentencesInText.add(sentencesArray[j]);
                    }
                } else {
                    sentencesInText.add(sentencesArray[0]);
                }
            }
            if (sentencesInText.size() == 0 && !text.trim().equals("")) {
                sentencesInText.add(text.trim());
            }
        } catch (Exception ex) {
            Logger.getLogger("TextParser").log(Level.WARNING, "Не удалось распознать текст: " + text);
        }
    }

    public List<List<String>> getWordsInSentence() {
        return wordsInSentence;
    }

    public List<String> getSentencesInText() {
        return sentencesInText;
    }

    public List<String> getWordsInText() {
        return wordsInText;
    }
}
