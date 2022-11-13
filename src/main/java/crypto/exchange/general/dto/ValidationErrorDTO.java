package crypto.exchange.general.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorDTO {

    @JsonProperty(required = true, value = "FieldName")
    private String fieldName;

    @JsonProperty(required = true, value = "ErrorDescription")
    private String description;

    @JsonProperty(required = true, value = "Value")
    private String value;
}
