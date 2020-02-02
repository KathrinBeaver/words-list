package ru.textanalysis.wordslist.util;

import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters;
import ru.textanalysis.wordslist.model.MorphSentence;
import ru.textanalysis.wordslist.model.SimpleWord;
import ru.textanalysis.wordslist.model.Word;
import ru.textanalysis.wordslist.model.WordMainForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordListConverter {

    public Map<Long, Long> convertMophologyParametersToMap(long morphCharateristics) {
        Map<Long, Long> morphMap = new HashMap<Long, Long>();
        morphMap.put(MorfologyParameters.Animacy.IDENTIFIER, morphCharateristics & MorfologyParameters.Animacy.IDENTIFIER);
        morphMap.put(MorfologyParameters.Name.IDENTIFIER, morphCharateristics & MorfologyParameters.Name.IDENTIFIER);
        morphMap.put(MorfologyParameters.Gender.IDENTIFIER, morphCharateristics & MorfologyParameters.Gender.IDENTIFIER);
        morphMap.put(MorfologyParameters.Numbers.IDENTIFIER, morphCharateristics & MorfologyParameters.Numbers.IDENTIFIER);
        morphMap.put(MorfologyParameters.Act.IDENTIFIER, morphCharateristics & MorfologyParameters.Act.IDENTIFIER);
        morphMap.put(MorfologyParameters.Alone.IDENTIFIER, morphCharateristics & MorfologyParameters.Alone.IDENTIFIER);
        morphMap.put(MorfologyParameters.Case.IDENTIFIER, morphCharateristics & MorfologyParameters.Case.IDENTIFIER);
        morphMap.put(MorfologyParameters.Liso.IDENTIFIER, morphCharateristics & MorfologyParameters.Liso.IDENTIFIER);
        morphMap.put(MorfologyParameters.Mood.IDENTIFIER, morphCharateristics & MorfologyParameters.Mood.IDENTIFIER);
        morphMap.put(MorfologyParameters.TepePronoun.IDENTIFIER, morphCharateristics & MorfologyParameters.TepePronoun.IDENTIFIER);
        morphMap.put(MorfologyParameters.Time.IDENTIFIER, morphCharateristics & MorfologyParameters.Time.IDENTIFIER);
        morphMap.put(MorfologyParameters.TerminationForm.IDENTIFIER, morphCharateristics & MorfologyParameters.TerminationForm.IDENTIFIER);
        morphMap.put(MorfologyParameters.Transitivity.IDENTIFIER, morphCharateristics & MorfologyParameters.Transitivity.IDENTIFIER);
        morphMap.put(MorfologyParameters.View.IDENTIFIER, morphCharateristics & MorfologyParameters.View.IDENTIFIER);
        morphMap.put(MorfologyParameters.Voice.IDENTIFIER, morphCharateristics & MorfologyParameters.Voice.IDENTIFIER);
        return morphMap;
    }

    public Map<WordMainForm, List<SimpleWord>> convertToMainFormsList(List<Word> allWords) {
        Map<WordMainForm, List<SimpleWord>> words = new HashMap<WordMainForm, List<SimpleWord>>();

        for(Word word : allWords) {
            WordMainForm mainForm = new WordMainForm(word.getInitialForm(), word.getTypeOfSpeech());
            if (words.containsKey(mainForm)) {
                words.get(mainForm).add(new SimpleWord(word.getWord(), word.getMorphCharateristics(), word.getSentenceNumber()));
            } else {
                List<SimpleWord> list = new ArrayList<SimpleWord>();
                list.add(new SimpleWord(word.getWord(), word.getMorphCharateristics(), word.getSentenceNumber()));
                words.put(mainForm, list);
            }
        }

        return words;
    }

    public Map<Long, Map<WordMainForm, List<SimpleWord>>> convertMorphToMainFormsList(Map<Long, List<Word>> morphList) {
        Map<Long, Map<WordMainForm, List<SimpleWord>>> morphMainForms = new HashMap<Long, Map<WordMainForm, List<SimpleWord>>>();

        for(Long morphKey : morphList.keySet()) {
            morphMainForms.put(morphKey, convertToMainFormsList(morphList.get(morphKey)));
        }

        return morphMainForms;
    }

    public Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> convertMorphSentToMainFormsList(Map<MorphSentence, List<Word>> morphSentList) {
        Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> morphSentMainForms = new HashMap<MorphSentence, Map<WordMainForm, List<SimpleWord>>>();

        for(MorphSentence morphKey : morphSentList.keySet()) {
            morphSentMainForms.put(morphKey, convertToMainFormsList(morphSentList.get(morphKey)));
        }

        return morphSentMainForms;
    }
 }
