package rs.interventure.data;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Priority {
  LOW("low"),
  MEDIUM("medium"),
  HIGH("high"),
  URGENT("urgent"),
  SUPER_URGENT("super urgent");

  private final String name;

  @JsonValue
  public int toValue() {
    return ordinal();
  }

  @Override
  public String toString() {
    return name;
  }
}
