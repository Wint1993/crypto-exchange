package crypto.exchange.currency.service;

import crypto.exchange.client.CryptoExchangeFeignClient;
import crypto.exchange.currency.dto.*;
import crypto.exchange.currency.exception.EmptyListException;
import crypto.exchange.currency.exception.IncorrectInputDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CryptocurrencyCalculatorServiceTest {

    private final static String API_KEY = "5934FWE";

    private final static String BTC_CURRENCY = "BTC";
    private final static String ETH_CURRENCY = "ETH";
    private final static String DC_CURRENCY = "DC";
    private final static String BTH_CURRENCY = "BTH";

    @Mock
    private CryptoExchangeFeignClient client;

    private CryptocurrencyCalculatorService underTest;

    @Before
    public void setup() {
        underTest = new CryptocurrencyCalculatorService(client, API_KEY);
    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenInputCurrencyIsNull() {
        underTest.getQuotesForGivenCryptocurrency(null, Collections.emptySet());
    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenAmountIsNull() {
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(null, BTC_CURRENCY, prepareToCurrencySet());
        underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
    }


    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenToCurrencySetIsNull() {
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, BTC_CURRENCY, null);
        underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
    }

    @Test(expected = IncorrectInputDataException.class)
    public void shouldThrowException_whenFromCurrencyIsNull() {
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, null, prepareToCurrencySet());
        underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
    }

    @Test(expected = EmptyListException.class)
    public void shouldThrowException_whenMethodCryptoExchangeForCurrency_returnEmptyList() {
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(null);
        underTest.getQuotesForGivenCryptocurrency(BTC_CURRENCY, null);
    }

    @Test(expected = EmptyListException.class)
    public void shouldThrowException_whenMethodGetCryptocurrencyForecast_returnEmptyList() {
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, BTC_CURRENCY, prepareToCurrencySet());
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(null);
        underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
    }

    @Test
    public void shouldReturnZeroCurrencies_WhenFilterWasEmpty() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);

        CryptocurrencyQuotesResponseDTO result = underTest.getQuotesForGivenCryptocurrency(BTC_CURRENCY, Stream.of(BTH_CURRENCY).collect(Collectors.toSet()));
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY ,result.getSource());
        assertEquals(0 ,result.getRates().size());
    }

    @Test
    public void shouldReturnAllCurrencies_WhenFilterWasEmpty() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);

        CryptocurrencyQuotesResponseDTO result = underTest.getQuotesForGivenCryptocurrency(BTC_CURRENCY, null);
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY ,result.getSource());
        assertEquals(2 ,result.getRates().size());
    }

    @Test
    public void shouldReturnOnlyOneCurrencyFromTwo_WhenThereIsOneFilter() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);

        CryptocurrencyQuotesResponseDTO result = underTest.getQuotesForGivenCryptocurrency(BTC_CURRENCY, Stream.of(ETH_CURRENCY).collect(Collectors.toSet()));
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY ,result.getSource());
        assertEquals(1 ,result.getRates().size());
    }

    @Test
    public void shouldReturnTwoCurrenciesFromTwoExpected_whenTwoWasReceived() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, BTC_CURRENCY, prepareToCurrencySet());
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);

        CryptocurrencyResponseDTO result = underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY, result.getFrom());
        assertEquals(2, result.getRates().size());
    }

    @Test
    public void shouldReturnOneCurrencyFromTwoExpected_whenOneWasReceived() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(BTH_CURRENCY, ETH_CURRENCY);
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, BTC_CURRENCY, prepareToCurrencySet());
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);

        CryptocurrencyResponseDTO result = underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY, result.getFrom());
        assertEquals(1, result.getRates().size());
    }

    private Set<String> prepareToCurrencySet(){
        return Stream.of(ETH_CURRENCY, DC_CURRENCY).collect(Collectors.toSet());
    }

    private CryptocurrencyRequestDTO prepareCryptocurrencyRequestDTO(BigDecimal amount, String fromCurrency, Set<String> toCurrencySet) {
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = new CryptocurrencyRequestDTO();
        cryptocurrencyRequestDTO.setAmount(amount);
        cryptocurrencyRequestDTO.setFromCurrency(fromCurrency);
        cryptocurrencyRequestDTO.setToCurrencySet(toCurrencySet);
        return cryptocurrencyRequestDTO;
    }

    private CryptoRatesDTO prepareCryptoRatesDTO(String firstCurrency, String secondCurrency){
        CryptoRateDTO rateBTH = prepareCryptoRateDTO(firstCurrency, BigDecimal.TEN);
        CryptoRateDTO rateETH = prepareCryptoRateDTO(secondCurrency, BigDecimal.ONE);

        CryptoRatesDTO cryptoRatesDTO = new CryptoRatesDTO();
        cryptoRatesDTO.setRates(Arrays.asList(rateBTH, rateETH));
        return cryptoRatesDTO;
    }

    private CryptoRateDTO prepareCryptoRateDTO(String currency, BigDecimal rate) {
        CryptoRateDTO cryptoRateDTO = new CryptoRateDTO();
        cryptoRateDTO.setCurrency(currency);
        cryptoRateDTO.setRate(rate);
        return cryptoRateDTO;
    }
}
