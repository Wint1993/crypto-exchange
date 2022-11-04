package crypto.exchange.currency.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CryptocurrencyResponseDTO<T> {

    private String source;

    private Map<String, T> rates;

    public CryptocurrencyResponseDTO(String source, Map<String, T> rates) {
        this.source = source;
        this.rates = rates;
    }
}
