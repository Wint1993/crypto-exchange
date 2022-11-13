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

public class CryptocurrencyForecastCalculatorServiceTest extends UnitTest {

    @Mock
    private CryptoRatesService cryptoRatesService;

    private CryptocurrencyForecastCalculatorService underTest;

    @Before
    public void setup() {
        underTest = new CryptocurrencyForecastCalculatorService(cryptoRatesService);
    }

    @Test
    public void shouldReturnAllRates_whenNoFiltersAdded() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        when(cryptoRatesService.getCryptoRates(BTC_CURRENCY)).thenReturn(rates);
        CryptocurrencyForecastResponseDTO result = underTest.calculateCryptocurrency(BTC_CURRENCY, null, BigDecimal.ONE);
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertEquals(result.getRates().size(), 2);
    }

    @Test
    public void shouldReturnOnlyOneRate_whenTheSameFilterAdded() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(BTH_CURRENCY, DC_CURRENCY);
        when(cryptoRatesService.getCryptoRates(BTC_CURRENCY)).thenReturn(rates);
        CryptocurrencyForecastResponseDTO result = underTest.calculateCryptocurrency(BTC_CURRENCY, prepareFilters(), BigDecimal.ONE);
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertEquals(result.getRates().size(), 1);
    }

    @Test
    public void shouldReturnNothing_whenFilterAddedNotExistInCryptoRateList() {
        CryptoRatesDTO rates = prepareCryptoRatesDTO(BTH_CURRENCY, DC_CURRENCY);
        when(cryptoRatesService.getCryptoRates(BTC_CURRENCY)).thenReturn(rates);
        CryptocurrencyForecastResponseDTO result = underTest.calculateCryptocurrency(BTC_CURRENCY, Stream.of(ETH_CURRENCY).collect(Collectors.toSet()), BigDecimal.ONE);
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertEquals(result.getRates().size(), 0);
    }

    @Test
    public void shouldReturnCryptocurrencyQuotesResponseDTO_whenCallMethod_createResult() {
        HashMap<String, CryptocurrencyDTO> currencyMap = new HashMap<>();
        CryptocurrencyDTO cryptocurrencyDTO = prepareCryptocurrencyDTO();
        CryptocurrencyDTO cryptocurrencyDTO1 = prepareCryptocurrencyDTO();
        currencyMap.put(BTC_CURRENCY, cryptocurrencyDTO);
        currencyMap.put(ETH_CURRENCY, cryptocurrencyDTO1);
        CryptocurrencyForecastResponseDTO result =  underTest.createResult(BTC_CURRENCY, currencyMap);
        assertEquals(result.getRates().size(), currencyMap.size());
        assertEquals(result.getSource(), BTC_CURRENCY);
        assertFalse(result.getRates().isEmpty());
    }

    protected static CryptocurrencyDTO prepareCryptocurrencyDTO(){
        return CryptocurrencyDTO.builder()
                .rate(null)
                .amount(null)
                .fee(null)
                .result(null)
                .build();
    }
}
