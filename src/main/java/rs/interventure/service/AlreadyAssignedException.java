package rs.interventure.service;

public class AlreadyAssignedException extends IllegalArgumentException {

  public AlreadyAssignedException(String message) {
    super(message);
  }
}
