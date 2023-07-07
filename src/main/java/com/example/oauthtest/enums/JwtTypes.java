package com.example.oauthtest.enums;

public enum JwtTypes {
  ADMIN("ADMIN"),
  MOBILE("MOBILE");

  private final String value;

  JwtTypes(String value) { this.value = value; }
  public String getValue() { return value; }

}
