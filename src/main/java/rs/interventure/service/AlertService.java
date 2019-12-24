package rs.interventure.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import rs.interventure.data.Alert;

@Service public class AlertService {

    public Optional<Alert> findByID(String id) {
        return Optional.empty();
    }

    public List<Alert> find(String userID, String tag) {
        return new ArrayList<>();
    }

    public Alert create(Alert alert) {
        return alert;
    }

    public Optional<Alert> assign(String id, String userID) {
        return Optional.empty();
    }
}
