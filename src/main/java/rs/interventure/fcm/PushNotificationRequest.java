package rs.interventure.fcm;

import lombok.Data;

@Data
public class PushNotificationRequest {
  private String topic;
  private String title;
  private String message;
}
