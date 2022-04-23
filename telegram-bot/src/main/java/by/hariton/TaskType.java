package by.hariton;

public enum TaskType {
  COMPLETED("completed"),
  UPCOMING("upcoming"),
  IN_PROCESS("in_process"),
  FAILED("failed");

  private final String name;

  TaskType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
