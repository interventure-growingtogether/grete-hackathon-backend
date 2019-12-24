package rs.interventure.service;

import com.google.common.base.Preconditions;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.interventure.data.Alert;
import rs.interventure.data.Priority;
import rs.interventure.fcm.PushNotificationRequest;
import rs.interventure.fcm.PushNotificationService;

@Service
@RequiredArgsConstructor
public class AlertService {

  private List<Alert> alerts = new ArrayList<>();
  private Map<String, String> usersByID = new HashMap<>();

  private final PushNotificationService notificationService;
  {
    usersByID.put("1", "Dejan");
    usersByID.put("2", "Alex");
    usersByID.put("3", "Ivan");
    usersByID.put("4", "Milos");

    alerts.add(generateOpenAlert("1", "JVM crash", "JVM crashes with OOM Killed", "java", Priority.LOW));
    alerts.add(generateOpenAlert("2", "Go JSON serializer doesn't work", "Can't serialize string to decimal", "golang", Priority.MEDIUM));
    alerts.add(generateOpenAlert("3", "K8S problem", "K8S stateful set max unavailable", "kubernetes", Priority.HIGH));
    alerts.add(generateOpenAlert("4", "neo4j problem", "Relation belongs to relation", "neo4j", Priority.URGENT));
    alerts.add(generateOpenAlert("4", "maven problem", "Surefire plugin doesn't work for Java14", "maven", Priority.SUPER_URGENT));

  }

  private Alert generateOpenAlert(String creatorID, String title, String description, String tag, Priority priority){
    Alert alert = new Alert();
    alert.setId(UUID.randomUUID().toString());
    alert.setCreatedAt(ZonedDateTime.now());
    alert.setCreatorID(creatorID).setCreatorName(usersByID.get(creatorID));
    alert.setTitle(title).setDescription(description).setTag(tag).setPriority(priority);
    alert.setOpen(true);

    return alert;
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
    req.setTitle(usersByID.get(alert.getCreatorID()) + " created an alert: " + alert.getTitle());
    req.setMessage(alert.getDescription());
    req.setAlertId(alert.getId());
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

    PushNotificationRequest req = new PushNotificationRequest();
    req.setTopic(id);
    req.setTitle("Your alert has bean taken by " + usersByID.get(assigneeID));
    req.setAlertId(id);
    notificationService.sendPushNotification(req);

    return alert;
  }
}
