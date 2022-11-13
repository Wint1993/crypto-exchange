package crypto.exchange.calculator;

import crypto.exchange.UnitTest;

import crypto.exchange.rates.CryptoRatesDTO;
import crypto.exchange.rates.CryptoRatesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class CryptocurrencyQuotesCalculatorServiceTest extends UnitTest {

    @Mock
    private CryptoRatesService cryptoRatesService;

    private CryptocurrencyQuotesCalculatorService underTest;

    @Before
    public void setup() {
        underTest = new CryptocurrencyQuotesCalculatorService(cryptoRatesService);
    }

    @Test
    public void shouldReturnAllRates_whenNoFiltersAdded() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        when(cryptoRatesService.getCryptoRates(BTC_CURRENCY)).thenReturn(rates);
        CryptocurrencyQuotesResponseDTO result = underTest.calculateCryptocurrency(BTC_CURRENCY, null, BigDecimal.ONE);
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertEquals(result.getRates().size(), 2);
    }

    @Test
    public void shouldReturnOnlyOneRate_whenTheSameFilterAdded() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(BTH_CURRENCY, DC_CURRENCY);
        when(cryptoRatesService.getCryptoRates(BTC_CURRENCY)).thenReturn(rates);
        CryptocurrencyQuotesResponseDTO result = underTest.calculateCryptocurrency(BTC_CURRENCY, prepareFilters(), BigDecimal.ONE);
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertEquals(result.getRates().size(), 1);
    }

    @Test
    public void shouldReturnNothing_whenFilterAddedNotExistInCryptoRateList() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(BTH_CURRENCY, DC_CURRENCY);
        when(cryptoRatesService.getCryptoRates(BTC_CURRENCY)).thenReturn(rates);
        CryptocurrencyQuotesResponseDTO result = underTest.calculateCryptocurrency(BTC_CURRENCY, Stream.of(ETH_CURRENCY).collect(Collectors.toSet()), BigDecimal.ONE);
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertEquals(result.getRates().size(), 0);
    }

    @Test
    public void shouldReturnCryptocurrencyQuotesResponseDTO_whenCallMethod_createResult() {
        HashMap<String, BigDecimal> currencyMap = new HashMap<>();
        currencyMap.put(BTC_CURRENCY, BigDecimal.ONE);
        currencyMap.put(ETH_CURRENCY, BigDecimal.TEN);
        CryptocurrencyQuotesResponseDTO result =  underTest.createResult(BTC_CURRENCY, currencyMap);
        assertEquals(result.getRates().size(), currencyMap.size());
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertFalse(result.getRates().isEmpty());
    }
}
