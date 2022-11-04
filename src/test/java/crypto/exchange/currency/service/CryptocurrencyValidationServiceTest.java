package crypto.exchange.currency.service;

import crypto.exchange.UnitTest;
import crypto.exchange.currency.dto.CryptoRatesDTO;
import crypto.exchange.currency.dto.CryptocurrencyRequestDTO;
import crypto.exchange.currency.exception.EmptyListException;
import crypto.exchange.currency.exception.IncorrectInputDataException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class CryptocurrencyValidationServiceTest extends UnitTest {

    private CryptocurrencyValidationService underTest;

    @Before
    public void setup() {
        underTest = new CryptocurrencyValidationService();
    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenToCurrencySetIsNull() {
        CryptocurrencyRequestDTO request = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, BTC_CURRENCY, null);
        underTest.validateInputs(request);

    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenFromCurrencyIsNull() {
        CryptocurrencyRequestDTO request = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, null, prepareToCurrencySet());
        underTest.validateInputs(request);

    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenAmountIsNull() {
        CryptocurrencyRequestDTO request = prepareCryptocurrencyRequestDTO(null, BTC_CURRENCY, prepareToCurrencySet());
        underTest.validateInputs(request);

    }

    @Test(expected = EmptyListException.class)
    public void shouldThrowException_whenMethodGetCryptocurrencyForecast_returnEmptyList() {
        underTest.validateCryptoRates(new CryptoRatesDTO());
    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenInputCurrencyIsNull() {
        underTest.validateCurrency(null);
    }


}
