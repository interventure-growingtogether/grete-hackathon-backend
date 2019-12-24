package rs.interventure.fcm;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {

  private final FCMService fcmService;

  public void sendPushNotification(PushNotificationRequest request) {
    try {
      fcmService.sendMessage(request);
    } catch (InterruptedException | ExecutionException e) {
      log.error(e.getMessage());
    }
  }

}
