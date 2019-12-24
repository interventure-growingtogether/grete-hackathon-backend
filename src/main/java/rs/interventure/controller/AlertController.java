package rs.interventure.controller;


import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.interventure.data.Alert;
import rs.interventure.service.AlertService;

@Slf4j
@RestController
@RequestMapping(AlertController.BASE_PATH)
@RequiredArgsConstructor
public class AlertController extends AbstractController {

  static final String BASE_PATH = "/alerts";

  private final AlertService alertService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable String id) {
    return alertService.findByID(id)
        .map(rsp -> (ResponseEntity) ResponseEntity.ok(rsp))
        .orElseGet(() -> notFoundResponseForId("Alert", id));
  }

  @GetMapping
  public ResponseEntity<?> find(@RequestParam(name = "user_id", required = false) String userID,
      @RequestParam(required = false) String tag, @RequestParam(name = "is_open", required = false) Boolean isOpen) {
    return ResponseEntity.ok(alertService.find(userID, tag, isOpen));
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Alert alert) {
    Alert created = alertService.create(alert);
    return ResponseEntity.created(URI.create(BASE_PATH + "/" + created.getId())).body(created);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> assign(@PathVariable String id, @RequestBody Alert alert) {
    assertRequestIdsAreEqual(id, alert.getId());

    Optional<Alert> updated = alertService.assign(id, alert.getAssigneeID());

    return updated
        .map(rsp -> (ResponseEntity) ResponseEntity.ok(rsp))
        .orElseGet(() -> notFoundResponseForId("Resource", id));
  }
}
