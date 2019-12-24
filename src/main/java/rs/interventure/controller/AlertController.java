package rs.interventure.controller;


import java.net.URI;
import java.util.Collections;
import java.util.Optional;
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

@Slf4j
@RestController
@RequestMapping(AlertController.BASE_PATH)
public class AlertController extends AbstractController {

    static final String BASE_PATH = "/alerts";

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Alert> alert = Optional.empty();

        return alert
            .map(rsp -> (ResponseEntity) ResponseEntity.ok(rsp))
            .orElseGet(() -> notFoundResponseForId("Alert", id));
    }

    public ResponseEntity<?> get(@RequestParam(name = "user_id", required = false) String userID,
        @RequestParam(required = false) String tag) {
        Optional<Alert> resource = Optional.empty();

        return ResponseEntity.ok(Collections.<Alert>emptyList());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Alert alert) {
        Alert created = alert;
        return ResponseEntity.created(URI.create(BASE_PATH + "/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> assign(@PathVariable String id, @RequestBody Alert alert) {
        assertRequestIdsAreEqual(id, alert.getId());

        Optional<Alert> updated = Optional.of(alert);

        return updated
            .map(rsp -> (ResponseEntity) ResponseEntity.ok(rsp))
            .orElseGet(() -> notFoundResponseForId("Resource", id));
    }
}
