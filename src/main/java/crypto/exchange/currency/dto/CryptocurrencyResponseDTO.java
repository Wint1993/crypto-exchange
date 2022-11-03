package crypto.exchange.currency.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class CryptocurrencyResponseDTO {

    private String from;

    private HashMap<String, CryptocurrencyDTO> rates;

}
