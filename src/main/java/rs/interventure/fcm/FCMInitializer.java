package rs.interventure.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class FCMInitializer {

  @Value("${app.firebase-configuration-file}")
  private String firebaseConfigPath;

  private Logger logger = LoggerFactory.getLogger(FCMInitializer.class);

  @PostConstruct
  public void initialize() {
    try {
      InputStream serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();

      FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://aleksjova013.firebaseio.com")
        .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        logger.info("Firebase application has been initialized");
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}


