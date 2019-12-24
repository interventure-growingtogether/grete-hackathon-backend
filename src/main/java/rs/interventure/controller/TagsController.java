package rs.interventure.controller;

import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("tags")
public class TagsController {

  @GetMapping
  public ResponseEntity<?> getTags() {

    return ResponseEntity.ok(Arrays.asList("java", "golang", "kubernetes", "mysql", "neo4j", "javascript", "react", "maven", "spring", "android"));
  }
}
