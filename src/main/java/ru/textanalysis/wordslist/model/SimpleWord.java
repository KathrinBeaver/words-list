package ru.textanalysis.wordslist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleWord {
    private String word;
    private long morphCharateristics;
    private int sentenceNumber;

    public String toString() {
        return word + " : морф. характеристики - " + morphCharateristics + " : номер предложения - " + sentenceNumber;
    }
}
