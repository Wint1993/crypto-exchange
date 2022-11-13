package crypto.exchange.calculator;

import lombok.Getter;

import java.util.Map;

@Getter
class AbstractCryptocurrencyResponseDTO<T> {

    private String source;

    private Map<String, T> rates;

    AbstractCryptocurrencyResponseDTO(String source, Map<String, T> rates) {
        this.source = source;
        this.rates = rates;
    }
}
