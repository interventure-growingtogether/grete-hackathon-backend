package rs.interventure.service;

import com.google.common.base.Preconditions;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.interventure.data.Alert;
import rs.interventure.fcm.PushNotificationRequest;
import rs.interventure.fcm.PushNotificationService;

@Service
@RequiredArgsConstructor
public class AlertService {

  private List<Alert> alerts = new ArrayList<>();

  private final PushNotificationService notificationService;
  {
//    alerts.add();
  }

  public Optional<Alert> findByID(String id) {
    return alerts.stream()
      .filter(a -> a.getId().equals(id))
      .findFirst();
  }

  public List<Alert> find(String creatorID, String tag, Boolean isOpen) {
    return alerts.stream()
      .filter(a -> creatorID == null || a.getCreatorID().equals(creatorID))
      .filter(a -> tag == null || a.getTag().equals(tag))
      .filter(a -> isOpen == null || a.isOpen() == isOpen)
      .collect(Collectors.toList());
  }

  public Alert create(Alert alert) {
    Preconditions.checkNotNull(alert.getTitle());
    Preconditions.checkNotNull(alert.getCreatorID());
    Preconditions.checkNotNull(alert.getDescription());
    Preconditions.checkNotNull(alert.getTag());
    Preconditions.checkNotNull(alert.getPriority());
    alert.setId(UUID.randomUUID().toString());
    alert.setCreatedAt(ZonedDateTime.now());
    alert.setOpen(true);
    alert.setAssigneeID(null);
    alerts.add(alert);

    PushNotificationRequest req = new PushNotificationRequest();
    req.setTopic(alert.getTag());
    req.setTitle(alert.getDescription());
    req.setMessage(alert.getDescription());

    notificationService.sendPushNotification(req);
    return alert;
  }

  public Optional<Alert> assign(String id, String assigneeID) {
    Optional<Alert> alert = findByID(id);
    alert.filter(a -> !a.isOpen()).ifPresent(a -> {
      throw new AlreadyAssignedException("Alert with id " + id + " already assigned");
    });
    alert.ifPresent(a -> a.setAssigneeID(assigneeID));
    alert.ifPresent(a -> a.setOpen(false));
    return alert;
  }
}
