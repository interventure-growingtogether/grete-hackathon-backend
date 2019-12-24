package rs.interventure.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Priority {
  LOW("low"),
  MEDIUM("medium"),
  HIGH("high"),
  URGENT("urgent"),
  SUPER_URGENT("super urgent");

  private final String name;

  @Override
  public String toString() {
    return name;
  }
}
