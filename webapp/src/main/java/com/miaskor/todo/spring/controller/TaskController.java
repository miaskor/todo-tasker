package com.miaskor.todo.spring.controller;

import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.model.task.CreateTaskRequest;
import by.miaskor.domain.model.task.TaskResponse;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "taskController")
@RequestMapping("/tasks")
public class TaskController {

  private final TaskConnector taskConnector;
  private final HttpSession httpSession;

  @Autowired
  public TaskController(TaskConnector taskConnector, HttpSession httpSession) {
    this.taskConnector = taskConnector;
    this.httpSession = httpSession;
  }

  @PostMapping("/save")
  public TaskResponse save(@RequestBody CreateTaskRequest task) {
    return taskConnector.create(task);
  }

  @GetMapping("/range")
  public Map<String, List<TaskResponse>> getAllByClientIdAndDateBetween(
      @RequestParam("date_from") String dateFrom,
      @RequestParam("date_to") String dateTo
  ) {
    int clientId = Integer.parseInt(httpSession.getAttribute("clientId").toString());
    return taskConnector.getAllByClientIdAndDateBetween(
        dateFrom,
        dateTo,
        clientId
    );
  }

  @GetMapping("/all")
  public List<TaskResponse> getAllByClientIdAndDateBetween(
      @RequestParam("client_id") Integer clientId) {
    return taskConnector.getAllByClientId(clientId);
  }

  @PatchMapping("/update/{id}")
  public TaskResponse update(@PathVariable("id") Integer taskId, @RequestBody CreateTaskRequest task) {
    return taskConnector.update(taskId, task);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Integer taskId) {
    taskConnector.delete(taskId);
  }
}
