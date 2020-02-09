package ru.textanalysis.wordslist.dto.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TextWithFilters {
    private String text;
    private List<Integer> typesOfSpeech;
    private boolean isSimpleMode;
}
