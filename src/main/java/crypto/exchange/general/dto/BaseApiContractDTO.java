package crypto.exchange.general.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class BaseApiContractDTO<DATA> {

    @JsonProperty(required = true, value = "SpecifyContract")
    private DATA specifyContract;

    @JsonProperty(required = true, value = "ValidationError", defaultValue = "[]")
    private List<ValidationErrorDTO> validationErrors;

    public BaseApiContractDTO(DATA specifyContract) {
        this(specifyContract, Collections.emptyList());
    }

    public BaseApiContractDTO(List<ValidationErrorDTO> list) {
        this(null, list);
    }
}
