package crypto.exchange.currency.service;

import crypto.exchange.currency.dto.CryptoRatesDTO;
import crypto.exchange.currency.dto.CryptocurrencyRequestDTO;
import crypto.exchange.currency.exception.EmptyListException;
import crypto.exchange.currency.exception.IncorrectInputDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Objects;

@Component
public class CryptocurrencyValidationService {

    @Autowired
    public CryptocurrencyValidationService() {
    }

    public void validateInputs(CryptocurrencyRequestDTO response) {
        if (Objects.isNull(response.getAmount()) || response.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectInputDataException(MessageFormat.format("Invalid amount, input amount: {0}", response.getAmount()));
        }
        if (Objects.isNull(response.getToCurrencySet()) || response.getToCurrencySet().isEmpty()) {
            throw new IncorrectInputDataException(MessageFormat.format("Invalid input list to, input to: {0}", response.getToCurrencySet()));
        }
        validateCurrency(response.getFromCurrency());
    }

    public void validateCurrency(String currency) {
        if (Objects.isNull(currency)) { // zwroc state
            throw new IncorrectInputDataException("Invalid input currency, currency is null");
        }
    }

    public void validateCryptoRates(CryptoRatesDTO rates) {
        if (Objects.isNull(rates) || Objects.isNull(rates.getRates()) || rates.getRates().isEmpty()) {
            throw new EmptyListException("Error during get crypto exchange for currency");
        }
    }
}
