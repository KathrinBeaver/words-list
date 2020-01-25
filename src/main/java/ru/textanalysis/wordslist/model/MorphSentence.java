package ru.textanalysis.wordslist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class MorphSentence {

    private long morphCharateristics;
    private int sentenceNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MorphSentence that = (MorphSentence) o;
        return morphCharateristics == that.morphCharateristics &&
                sentenceNumber == that.sentenceNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(morphCharateristics, sentenceNumber);
    }
}
