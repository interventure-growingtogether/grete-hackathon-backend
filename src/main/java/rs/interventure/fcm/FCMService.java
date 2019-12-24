package rs.interventure.fcm;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FCMService {

  public void sendMessage(PushNotificationRequest request)
    throws InterruptedException, ExecutionException {
    Message message = getPreconfiguredMessageWithoutData(request);
    String response = sendAndGetResponse(message);
    log.info("Sent message. Topic: " + request.getTopic() + ", " + response);
  }

  private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
    return FirebaseMessaging.getInstance().sendAsync(message).get();
  }

  private AndroidConfig getAndroidConfig(String topic) {
    return AndroidConfig.builder()
      .setTtl(Duration.ofMinutes(5).toMillis()).setCollapseKey(topic)
      .setPriority(AndroidConfig.Priority.HIGH)
      .setNotification(AndroidNotification.builder()
//        .setSound(NotificationParameter.SOUND.getValue())
//        .setColor(NotificationParameter.COLOR.getValue())
        .setTag(topic).build()).build();
  }

  private ApnsConfig getApnsConfig(String topic) {
    return ApnsConfig.builder()
      .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
  }

  private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
    return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
      .build();
  }

  private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
    AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
    ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
    return Message.builder()
      .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
        new Notification(request.getTitle(), request.getMessage()))
      .putData("alert_id", request.getAlertId());
  }

}