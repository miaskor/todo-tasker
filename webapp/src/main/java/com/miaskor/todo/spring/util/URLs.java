package com.miaskor.todo.spring.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class URLs {

  public static final String LOGIN = "/auth";
  public static final String ABOUT = "/about";
  public static final String REGISTRATION = "/registration";
  public static final String CSS_FILES = "/css";
  public static final String JS_FILES = "/js";
  public static final String IMG_FILES = "/img";

  public static Set<String> getAllPublicURL() {
    return new HashSet<>(Arrays.asList(LOGIN, CSS_FILES, JS_FILES, IMG_FILES, REGISTRATION, ABOUT));
  }
}
