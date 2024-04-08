package rw.pacis.tekanaewallet.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets EGender
 */
public enum EGender {
  MALE("male"),
    FEMALE("female");

  private final String value;

  EGender(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EGender fromValue(String text) {
    for (EGender b : EGender.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
