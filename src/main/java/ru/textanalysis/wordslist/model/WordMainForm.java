package ru.textanalysis.wordslist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class WordMainForm {
    private String initialForm;
    private byte typeOfSpeech;

    public String toString() {
        return initialForm + " : " + typeOfSpeech;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordMainForm that = (WordMainForm) o;
        return typeOfSpeech == that.typeOfSpeech &&
                Objects.equals(initialForm, that.initialForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialForm, typeOfSpeech);
    }
}
