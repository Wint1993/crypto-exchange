package crypto.exchange.calculator;

import lombok.Getter;

import java.util.Map;

@Getter
abstract class AbstractCryptocurrencyResponseDTO<T> {

    private final String source;

    private final Map<String, T> rates;

    AbstractCryptocurrencyResponseDTO(String source, Map<String, T> rates) {
        this.source = source;
        this.rates = rates;
    }
}
