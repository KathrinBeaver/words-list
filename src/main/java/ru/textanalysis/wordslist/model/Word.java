package ru.textanalysis.wordslist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    private String word;
    private String initialForm;
    private byte typeOfSpeech;
    private long morphCharateristics;
    private int sentenceNumber;
    private Map<Long, Long> morph;

    public String toString() {
       return initialForm + " : " + typeOfSpeech + " : " + word;
    }
}
