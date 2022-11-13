package crypto.exchange.rates;

import crypto.exchange.general.exception.exceptions.EmptyListException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;

@Service
public class CryptoRatesService {

    private final CryptoRatesFeignClient client;

    private final String apiKey;

    @Autowired
    CryptoRatesService(@Value("${crypto.exchange.api.key}") final String apiKey,
                              final CryptoRatesFeignClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    public CryptoRatesDTO getCryptoRates(final String currency) {
        CryptoRatesDTO rates = client.getCryptoExchangeForCurrency(apiKey, currency);
        validateCryptoRates(rates, currency);
        return rates;
    }

    private void validateCryptoRates(CryptoRatesDTO rates, String currency) {
        if (Objects.isNull(rates) || Objects.isNull(rates.getRates()) || rates.getRates().isEmpty()) {
            throw new EmptyListException(MessageFormat.format("Error during get cryptocurrency list for currency: {0}", currency));
        }
    }
}
