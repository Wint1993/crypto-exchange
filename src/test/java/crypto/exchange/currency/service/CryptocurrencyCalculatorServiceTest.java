package crypto.exchange.currency.service;

import crypto.exchange.UnitTest;
import crypto.exchange.client.CryptoExchangeFeignClient;
import crypto.exchange.currency.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CryptocurrencyCalculatorServiceTest extends UnitTest {

    private final static String API_KEY = "5934FWE";

    @Mock
    private CryptoExchangeFeignClient client;

    @Mock
    private CryptocurrencyValidationService cryptocurrencyValidationService;

    private CryptocurrencyCalculatorService underTest;

    @Before
    public void setup() {
        underTest = new CryptocurrencyCalculatorService(cryptocurrencyValidationService, client, API_KEY);
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

        CryptocurrencyRatesResponseDTO result = underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY, result.getSource());
        assertEquals(2, result.getRates().size());
    }

    @Test
    public void shouldReturnOneCurrencyFromTwoExpected_whenOneWasReceived() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(BTH_CURRENCY, ETH_CURRENCY);
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = prepareCryptocurrencyRequestDTO(BigDecimal.ONE, BTC_CURRENCY, prepareToCurrencySet());
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);

        CryptocurrencyRatesResponseDTO result = underTest.getCryptocurrencyForecast(cryptocurrencyRequestDTO);
        Mockito.verify(client, times(1)).getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY);
        assertEquals(BTC_CURRENCY, result.getSource());
        assertEquals(1, result.getRates().size());
    }

    private static CryptoRatesDTO prepareCryptoRatesDTO(String firstCurrency, String secondCurrency){
        CryptoRateDTO rateBTH = prepareCryptoRateDTO(firstCurrency, BigDecimal.TEN);
        CryptoRateDTO rateETH = prepareCryptoRateDTO(secondCurrency, BigDecimal.ONE);

        CryptoRatesDTO cryptoRatesDTO = new CryptoRatesDTO();
        cryptoRatesDTO.setRates(Arrays.asList(rateBTH, rateETH));
        return cryptoRatesDTO;
    }

    private static CryptoRateDTO prepareCryptoRateDTO(String currency, BigDecimal rate) {
        CryptoRateDTO cryptoRateDTO = new CryptoRateDTO();
        cryptoRateDTO.setCurrency(currency);
        cryptoRateDTO.setRate(rate);
        return cryptoRateDTO;
    }
}
