package rw.pacis.tekanaewallet.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ETransactionType {
   TRANSFER("TRANSFER"),
   RECEIVE("RECEIVE");

   private String value;

   ETransactionType(String value) {
      this.value = value;
   }

   @JsonCreator
   public static ETransactionType fromValue(String text) {
      for (ETransactionType b : ETransactionType.values()) {
         if (String.valueOf(b.value).equals(text)) {
            return b;
         }
      }
      return null;
   }
}
