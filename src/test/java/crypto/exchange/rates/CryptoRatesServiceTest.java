package crypto.exchange.rates;

import crypto.exchange.UnitTest;
import crypto.exchange.general.exception.exceptions.EmptyListException;
import crypto.exchange.rates.CryptoRatesDTO;
import crypto.exchange.rates.CryptoRatesFeignClient;
import crypto.exchange.rates.CryptoRatesService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.Mockito.when;

public class CryptoRatesServiceTest extends UnitTest {

    private final static String API_KEY = "5934FWE";

    @Mock
    private CryptoRatesFeignClient client;

    private CryptoRatesService underTest;

    @Before
    public void setup() {
        underTest = new CryptoRatesService(API_KEY, client);
    }

    @Test(expected = EmptyListException.class)
    public void shouldThrowException_whenAPIReturnNull(){
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(null);
        underTest.getCryptoRates(BTC_CURRENCY);
    }

    @Test(expected = EmptyListException.class)
    public void shouldThrowException_whenAPIReturnObjectWithEmptyList(){
        CryptoRatesDTO cryptoRatesDTO = new CryptoRatesDTO();
        ReflectionTestUtils.setField(cryptoRatesDTO, "rates", Collections.emptyList());
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(cryptoRatesDTO);
        underTest.getCryptoRates(BTC_CURRENCY);
    }

    @Test(expected = EmptyListException.class)
    public void shouldThrowException_whenAPIReturnObjectWithNullList(){
        CryptoRatesDTO cryptoRatesDTO = new CryptoRatesDTO();
        ReflectionTestUtils.setField(cryptoRatesDTO, "rates", null);
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(cryptoRatesDTO);
        underTest.getCryptoRates(BTC_CURRENCY);
    }

    @Test
    public void shouldReturnCryptoRatesList(){
        CryptoRatesDTO rates = prepareCryptoRatesDTO(ETH_CURRENCY, DC_CURRENCY);
        when(client.getCryptoExchangeForCurrency(API_KEY, BTC_CURRENCY)).thenReturn(rates);
        CryptoRatesDTO result =  underTest.getCryptoRates(BTC_CURRENCY);
        Assert.assertEquals(rates.getRates().size(), result.getRates().size());
    }
}
