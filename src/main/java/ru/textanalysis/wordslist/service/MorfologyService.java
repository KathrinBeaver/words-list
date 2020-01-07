package ru.textanalysis.wordslist.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.loader.JMorfSdkFactory;

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
}
