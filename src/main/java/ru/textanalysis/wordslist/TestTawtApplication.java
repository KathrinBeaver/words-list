package ru.textanalysis.wordslist;

import lombok.extern.java.Log;
import ru.textanalysis.tawt.gama.main.Gama;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.loader.JMorfSdkFactory;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters;
import ru.textanalysis.tawt.ms.storage.ref.RefSentenceList;
import ru.textanalysis.wordslist.model.MorphSentence;
import ru.textanalysis.wordslist.model.SimpleWord;
import ru.textanalysis.wordslist.model.Word;
import ru.textanalysis.wordslist.model.WordMainForm;
import ru.textanalysis.wordslist.util.PrintWordsList;
import ru.textanalysis.wordslist.util.TextParser;
import ru.textanalysis.wordslist.util.TypeOfSpeech;
import ru.textanalysis.wordslist.util.WordListConverter;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Log
public class TestTawtApplication {
    public static void main(String[] args) throws IOException{

        JMorfSdk jMorfSdk = JMorfSdkFactory.loadFullLibrary();
//        Path filePath = Paths.get("texts\\1.txt");
        File fileName = new File("texts\\");
        File[] fileList = fileName.listFiles();

        for (File file: fileList) {
            if (file.isDirectory()) {
                continue;
            }
            Path filePath = file.toPath();//Paths.get("texts\\context_example.txt");
            String text = null;

            Gama gama = new Gama();
            gama.init();
            RefSentenceList sentenceList = gama.getMorphParagraph("Мой новый друг снисходительно улыбнулся.");
            System.out.println(sentenceList);

            sentenceList = gama.getMorphParagraph("Но как же я удивился, когда мой строгий судья вдруг просиял.");
            System.out.println(sentenceList);

            try {
                text = Files.readString(filePath);
//            System.out.println(content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            TextParser parser = new TextParser(text);
            List<List<String>> sentences = parser.getWordsInSentence();

            List<Word> allWords = new ArrayList<Word>();

        /*
            Маски морфологических характеристик для разных частей речи
         */
            long NOUN_MASK = MorfologyParameters.Numbers.IDENTIFIER |
                    MorfologyParameters.Gender.IDENTIFIER |
                    MorfologyParameters.Case.IDENTIFIER;
//                MorfologyParameters.Animacy.IDENTIFIER;

            long NAME_MASK = MorfologyParameters.Name.IDENTIFIER |
                    MorfologyParameters.Animacy.IDENTIFIER;

        /*
            Все слова по частям речи
         */
            List<Word> allNouns = new ArrayList<Word>();
            List<Word> allVerbs = new ArrayList<Word>();
            List<Word> allParticles = new ArrayList<Word>();
            List<Word> allAdverbs = new ArrayList<Word>();
            List<Word> allAdjectives = new ArrayList<Word>();

        /*
            Все слова по частям речи, сгруппированные по одинаковым морф. характеристикам
         */
            Map<Long, List<Word>> nounsMorph = new HashMap<Long, List<Word>>();
            Map<Long, List<Word>> verbsMorph = new HashMap<Long, List<Word>>();
            Map<Long, List<Word>> adverbsMorph = new HashMap<Long, List<Word>>();
            Map<Long, List<Word>> adjectivesMorph = new HashMap<Long, List<Word>>();
            Map<Long, List<Word>> particlesMorph = new HashMap<Long, List<Word>>();

        /*
             Все слова по частям речи, сгруппированные по одинаковым морф. характеристикам и предложениям
         */
            Map<MorphSentence, List<Word>> nounsMorphSent = new HashMap<MorphSentence, List<Word>>();
            Map<MorphSentence, List<Word>> verbsMorphSent = new HashMap<MorphSentence, List<Word>>();
            Map<MorphSentence, List<Word>> adverbsMorphSent = new HashMap<MorphSentence, List<Word>>();
            Map<MorphSentence, List<Word>> adjectivesMorphSent = new HashMap<MorphSentence, List<Word>>();
            Map<MorphSentence, List<Word>> particlesMorphSent = new HashMap<MorphSentence, List<Word>>();

        /*
             Все слова, сгруппированные по одинаковым морф. характеристикам и предложениям
        */
            Map<MorphSentence, List<Word>> allMorphSent = new HashMap<MorphSentence, List<Word>>();

//        Text text = new Text();
//        Map<Integer, String> paragraphs = text.splitTextByParagraphs(content);
//        SentenceProcessing proc = new SentenceProcessing();
//        List<List<String>> sentences = proc.splitSentenceByWords(paragraphs);

//        Map<Long, List<Word>> similarMorphWords = new HashMap<Long, List<Word>>();
            Map<Byte, Map<Long, List<Word>>> similarMorphWords = new HashMap<Byte, Map<Long, List<Word>>>();


//        Map<String, List<Integer>> nouns = new HashMap<>();

            for (int sentIndex = 0; sentIndex < sentences.size(); sentIndex++) {
                List<String> sentence = sentences.get(sentIndex);
                for (String wordInSentence : sentence) {
//                int finalSentIndex = sentIndex;

                    Word word = new Word();
                    word.setWord(wordInSentence);
                    word.setSentenceNumber(sentIndex);

                    List<String> initialForms = jMorfSdk.getStringInitialForm(wordInSentence);
                    List<Long> morphCharacteristics = jMorfSdk.getMorphologyCharacteristics(wordInSentence);
                    List<Byte> typeOfSpeeches = jMorfSdk.getTypeOfSpeeches(wordInSentence);

                    if (initialForms.size() == 0) {
                        continue;
                    }

                    if (initialForms.size() > 1) {
//                    log.info(wordInSentence + " has more 1 initial form, need analysis");
                    }

                    word.setInitialForm(initialForms.get(0));
                    word.setMorphCharateristics(morphCharacteristics.get(0));
                    word.setTypeOfSpeech(typeOfSpeeches.get(0));

                    allWords.add(word);

                    if (wordInSentence.length() > 2) {
                        switch (word.getTypeOfSpeech()) {
                            case MorfologyParameters.TypeOfSpeech.NOUN:
                                allNouns.add(word);
                                long morphClr = word.getMorphCharateristics();
                                //long morphClr = word.getMorphCharateristics() & NOUN_MASK;
                                if (nounsMorph.containsKey(morphClr)) {
                                    nounsMorph.get(morphClr).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    nounsMorph.put(morphClr, list);
                                }
                                MorphSentence morphSentNoun = new MorphSentence(morphClr, word.getSentenceNumber());
                                if (nounsMorphSent.containsKey(morphSentNoun)) {
                                    nounsMorphSent.get(morphSentNoun).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    nounsMorphSent.put(morphSentNoun, list);
                                }
                                break;
                            case MorfologyParameters.TypeOfSpeech.VERB:
                            case MorfologyParameters.TypeOfSpeech.INFINITIVE:
                            case MorfologyParameters.TypeOfSpeech.PREDICATE:
                            case MorfologyParameters.TypeOfSpeech.GERUND:
                            case MorfologyParameters.TypeOfSpeech.GERUND_SHI:
                            case MorfologyParameters.TypeOfSpeech.GERUNDIMPERFECT:
                                allVerbs.add(word);
                                if (verbsMorph.containsKey(word.getMorphCharateristics())) {
                                    verbsMorph.get(word.getMorphCharateristics()).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    verbsMorph.put(word.getMorphCharateristics(), list);
                                }
                                MorphSentence morphSentVerb = new MorphSentence(word.getMorphCharateristics(), word.getSentenceNumber());
                                if (verbsMorphSent.containsKey(morphSentVerb)) {
                                    verbsMorphSent.get(morphSentVerb).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    nounsMorphSent.put(morphSentVerb, list);
                                }
                                break;
                            case MorfologyParameters.TypeOfSpeech.ADJECTIVEFULL:
                            case MorfologyParameters.TypeOfSpeech.ADJECTIVESHORT:
                            case MorfologyParameters.TypeOfSpeech.COMPARATIVE:
                                allAdjectives.add(word);
                                if (adjectivesMorph.containsKey(word.getMorphCharateristics())) {
                                    adjectivesMorph.get(word.getMorphCharateristics()).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    adjectivesMorph.put(word.getMorphCharateristics(), list);
                                }
                                MorphSentence morphSentAdj = new MorphSentence(word.getMorphCharateristics(), word.getSentenceNumber());
                                if (adjectivesMorphSent.containsKey(morphSentAdj)) {
                                    adjectivesMorphSent.get(morphSentAdj).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    adjectivesMorphSent.put(morphSentAdj, list);
                                }
                                break;
                            case MorfologyParameters.TypeOfSpeech.ADVERB:
                                allAdverbs.add(word);
                                if (adverbsMorph.containsKey(word.getMorphCharateristics())) {
                                    adverbsMorph.get(word.getMorphCharateristics()).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    adverbsMorph.put(word.getMorphCharateristics(), list);
                                }
                                MorphSentence morphSentAdverb = new MorphSentence(word.getMorphCharateristics(), word.getSentenceNumber());
                                if (adverbsMorphSent.containsKey(morphSentAdverb)) {
                                    adverbsMorphSent.get(morphSentAdverb).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    adverbsMorphSent.put(morphSentAdverb, list);
                                }
                                break;
                            case MorfologyParameters.TypeOfSpeech.PARTICIPLE:
                            case MorfologyParameters.TypeOfSpeech.PARTICIPLEFULL:
                            case MorfologyParameters.TypeOfSpeech.PARTICLE:
                                allParticles.add(word);
                                if (particlesMorph.containsKey(word.getMorphCharateristics())) {
                                    particlesMorph.get(word.getMorphCharateristics()).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    particlesMorph.put(word.getMorphCharateristics(), list);
                                }
                                MorphSentence morphSentPart = new MorphSentence(word.getMorphCharateristics(), word.getSentenceNumber());
                                if (particlesMorphSent.containsKey(morphSentPart)) {
                                    particlesMorphSent.get(morphSentPart).add(word);
                                } else {
                                    List<Word> list = new ArrayList<Word>();
                                    list.add(word);
                                    particlesMorphSent.put(morphSentPart, list);
                                }
                                break;
                        }

                        // Без разделения по частям речи, по предложениям и морфологии
                        //long morphClr = word.getMorphCharateristics() & NOUN_MASK;
                        long morphClr = word.getMorphCharateristics();

                        MorphSentence morphSentAdverb = new MorphSentence(morphClr, word.getSentenceNumber());
                        if (allMorphSent.containsKey(morphSentAdverb)) {
                            allMorphSent.get(morphSentAdverb).add(word);
                        } else {
                            List<Word> list = new ArrayList<Word>();
                            list.add(word);
                            allMorphSent.put(morphSentAdverb, list);
                        }
                    }

//                jMorfSdk.getAllCharacteristicsOfForm(word).forEach(form -> {
//                    if (word.length() > 2 && form.getTypeOfSpeech() == MorfologyParameters.TypeOfSpeech.NOUN &&
//                            form.getTheMorfCharacteristics(MorfologyParameters.Gender.FEMININ) == MorfologyParameters.Gender.FEMININ &&
//                             form.getTheMorfCharacteristics(MorfologyParameters.Numbers.SINGULAR) == MorfologyParameters.Numbers.SINGULAR) {
////                            System.out.println(word);
//                        if (nouns.containsKey(form.getInitialFormString())) {
//                            nouns.get(form.getInitialFormString()).add(finalSentIndex);
//                        } else {
//                            List<Integer> lst = new ArrayList<Integer>();
//                            lst.add(finalSentIndex);
//                            nouns.put(form.getInitialFormString(), lst);
//                        }
//                    }
//                });
                }
//            sentIndex++;
            }

            PrintWordsList printUtil = new PrintWordsList();
            WordListConverter converter = new WordListConverter();

            printUtil.printWordsList(allWords, "result\\all_words.lst", "Все слова - ");
            Map<WordMainForm, List<SimpleWord>> uniqueAllWords = converter.convertToMainFormsList(allWords);
            printUtil.printWordsMap(uniqueAllWords, "result\\unique_all_words.lst", "Все слова - ");
            Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> uniqueMorphSentAll = converter.convertMorphSentToMainFormsList(allMorphSent);
            printUtil.printMorphSentWordsMap(uniqueMorphSentAll, "result\\unique_morph_sent_all.lst", "Имена существительные - ");

            printUtil.printWordsList(allNouns, "result\\nouns\\all_nouns.lst", "Имена существительные - ");
            Map<WordMainForm, List<SimpleWord>> uniqueNouns = converter.convertToMainFormsList(allNouns);
            printUtil.printWordsMap(uniqueNouns, "result\\nouns\\unique_nouns.lst", "Имена существительные - ");
            Map<Long, Map<WordMainForm, List<SimpleWord>>> uniqueMorphNouns = converter.convertMorphToMainFormsList(nounsMorph);
            printUtil.printMorphWordsMap(uniqueMorphNouns, "result\\nouns\\unique_morph_nouns.lst", "Имена существительные - ");
            Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> uniqueMorphSentNouns = converter.convertMorphSentToMainFormsList(nounsMorphSent);
            printUtil.printMorphSentWordsMap(uniqueMorphSentNouns, "result\\nouns\\unique_morph_sent_nouns.lst", "Имена существительные - ");

            printUtil.printWordsList(allVerbs, "result\\verbs\\all_verbs.lst", "Глаголы - ");
            Map<WordMainForm, List<SimpleWord>> uniqueVerbs = converter.convertToMainFormsList(allVerbs);
            printUtil.printWordsMap(uniqueVerbs, "result\\verbs\\unique_verbs.lst", "Глаголы - ");
            Map<Long, Map<WordMainForm, List<SimpleWord>>> uniqueMorphVerbs = converter.convertMorphToMainFormsList(verbsMorph);
            printUtil.printMorphWordsMap(uniqueMorphVerbs, "result\\verbs\\unique_morph_verbs.lst", "Глаголы - ");
            Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> uniqueMorphSentVerbs = converter.convertMorphSentToMainFormsList(verbsMorphSent);
            printUtil.printMorphSentWordsMap(uniqueMorphSentVerbs, "result\\verbs\\unique_morph_sent_verbs.lst", "Глаголы - ");

            printUtil.printWordsList(allAdverbs, "result\\adverbs\\all_adverbs.lst", "Наречия - ");
            Map<WordMainForm, List<SimpleWord>> uniqueAdverbs = converter.convertToMainFormsList(allAdverbs);
            printUtil.printWordsMap(uniqueAdverbs, "result\\adverbs\\unique_adverbs.lst", "Наречия - ");
            Map<Long, Map<WordMainForm, List<SimpleWord>>> uniqueMorphAdverbs = converter.convertMorphToMainFormsList(adverbsMorph);
            printUtil.printMorphWordsMap(uniqueMorphAdverbs, "result\\adverbs\\unique_morph_adverbs.lst", "Наречия - ");
            Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> uniqueMorphSentAdverbs = converter.convertMorphSentToMainFormsList(adverbsMorphSent);
            printUtil.printMorphSentWordsMap(uniqueMorphSentAdverbs, "result\\adverbs\\unique_morph_sent_adverbs.lst", "Наречия - ");

            printUtil.printWordsList(allAdjectives, "result\\adjectives\\all_adjectives.lst", "Имена прилагательные - ");
            Map<WordMainForm, List<SimpleWord>> uniqueAdjectives = converter.convertToMainFormsList(allAdjectives);
            printUtil.printWordsMap(uniqueAdjectives, "result\\adjectives\\unique_adjectives.lst", "Имена прилагательные - ");
            Map<Long, Map<WordMainForm, List<SimpleWord>>> uniqueMorphAdjectives = converter.convertMorphToMainFormsList(adjectivesMorph);
            printUtil.printMorphWordsMap(uniqueMorphAdjectives, "result\\adjectives\\unique_morph_adjectives.lst", "Имена прилагательные - ");
            Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> uniqueMorphSentAdjectives = converter.convertMorphSentToMainFormsList(adjectivesMorphSent);
            printUtil.printMorphSentWordsMap(uniqueMorphSentAdjectives, "result\\adjectives\\unique_morph_sent_adjectives.lst", "Имена прилагательные - ");

            printUtil.printWordsList(allParticles, "result\\particles\\all_adverbs.lst", "Причастия - ");
            Map<WordMainForm, List<SimpleWord>> uniqueParticles = converter.convertToMainFormsList(allParticles);
            printUtil.printWordsMap(uniqueParticles, "result\\particles\\unique_adverbs.lst", "Причастия - ");
            Map<Long, Map<WordMainForm, List<SimpleWord>>> uniqueMorphParticles = converter.convertMorphToMainFormsList(particlesMorph);
            printUtil.printMorphWordsMap(uniqueMorphParticles, "result\\particles\\unique_morph_adverbs.lst", "Причастия - ");
            Map<MorphSentence, Map<WordMainForm, List<SimpleWord>>> uniqueMorphSentParticles = converter.convertMorphSentToMainFormsList(particlesMorphSent);
            printUtil.printMorphSentWordsMap(uniqueMorphSentParticles, "result\\particles\\unique_morph_sent_adverbs.lst", "Причастия - ");

        /*
        Генерация выгрузки для WEKA
         */
            //Map<WordMainForm, List<SimpleWord>>
            for (Word word : allWords) {
                word.setMorph(converter.convertMophologyParametersToMap(word.getMorphCharateristics()));
            }
            for (Word noun : allNouns) {
                noun.setMorph(converter.convertMophologyParametersToMap(noun.getMorphCharateristics()));
            }

            printUtil.printMorphSentWordsMap(allWords, "result\\for_weka\\datAll_"+ file.getName() +".arff");
            printUtil.printMorphSentWordsMap(allNouns, "result\\for_weka\\dataNouns_"+ file.getName() +".arff");
        }

//        List<IOmoForm> characteristics5 = jMorfSdk.getAllC haracteristicsOfForm("мыл");
//        characteristics5.forEach((form) -> {
//            System.out.println(form);
//        });
//
//        jMorfSdk.getAllCharacteristicsOfForm("дорогой").forEach((form) -> {
//            //Пример поиска формы в родительном падеже
//            if (form.getTheMorfCharacteristics(MorfologyParameters.Case.IDENTIFIER) == MorfologyParameters.Case.GENITIVE) {
//                System.out.println("Форма в родительном падеже " + form);
//            }
//
//            if (form.getTypeOfSpeech() == MorfologyParameters.TypeOfSpeech.VERB) {
//                System.out.println("Форма с глаголом найдена " + form);
//            }
//        });
//
//        jMorfSdk.getAllCharacteristicsOfForm("дорогой").forEach(form -> {
//            if (form.getTypeOfSpeech() == MorfologyParameters.TypeOfSpeech.NOUN) {
//                System.out.println(form);
//            }
//        });
//
//        List<String> words = Arrays.asList("осенний", "осенней", "площадь", "стол", "играть", "конференций", "на", "бежала");
//        for (String word : words) {
//            jMorfSdk.getAllCharacteristicsOfForm(word).forEach(form -> {
//                if (form.getTheMorfCharacteristics(MorfologyParameters.Gender.class) == MorfologyParameters.Gender.FEMININ) {
//                    System.out.println(form + " - " + word);
//                }
//            });
//        }

    }
}
