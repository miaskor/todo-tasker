package com.miaskor.todo.spring.handler;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class CustomErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;

  public CustomErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  @RequestMapping("/error")
  public ResponseEntity<ErrorDto> error(WebRequest webRequest) {
    Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest,
        ErrorAttributeOptions.of(
            Include.EXCEPTION,
            Include.MESSAGE)
    );
    return ResponseEntity
        .status((Integer) errorAttributes.get("status"))
        .body(
            new ErrorDto(
                (String) errorAttributes.get("error"),
                (String) errorAttributes.get("message")
            )
        );
  }
}
