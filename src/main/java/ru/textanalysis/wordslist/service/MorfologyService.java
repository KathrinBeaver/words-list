package ru.textanalysis.wordslist.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.loader.JMorfSdkFactory;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters;
import ru.textanalysis.wordslist.util.TypeOfSpeech;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MorfologyService {

    private JMorfSdk jMorfSdk;

    public MorfologyService() {
        jMorfSdk = JMorfSdkFactory.loadFullLibrary();
    }

    public JMorfSdk getjMorfSdk() {
        return jMorfSdk;
    }

    /**
     *
     * @return all types of speech
     */
    public Map<Byte, String> getTypesOfSpeech() {
        Map<Byte, String> typesOfSpeech = new HashMap<>();

        Class<TypeOfSpeech> typeOfSpeechClass = TypeOfSpeech.class;
        Field[] fields = typeOfSpeechClass.getFields();

        for (Field field: fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(typeOfSpeechClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            typesOfSpeech.put((Byte) value, field.getName());
        }
        return typesOfSpeech;
    }
}
