package de.sample.schulung.accounts.boundary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UUID uuid;
  @NotNull
  @Size(min = 3, max = 100)
  private String name;
  @JsonProperty("birthdate")
  @NotNull
  private LocalDate dateOfBirth;
  private CustomerStateDto state = CustomerStateDto.ACTIVE;

  public enum CustomerStateDto {
    ACTIVE("active"),
    LOCKED("locked"),
    DISABLED("disabled");

    private final String value;

    CustomerStateDto(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }
  }

}
