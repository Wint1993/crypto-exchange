package crypto.exchange;

import crypto.exchange.rates.CryptoRateDTO;
import crypto.exchange.rates.CryptoRatesDTO;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTest {

    protected final static String BTC_CURRENCY = "BTC";
    protected final static String ETH_CURRENCY = "ETH";
    protected final static String DC_CURRENCY = "DC";
    protected final static String BTH_CURRENCY = "BTH";

    protected static Set<String> prepareFilters(){
        return Stream.of(ETH_CURRENCY, DC_CURRENCY).collect(Collectors.toSet());
    }

    protected static CryptoRatesDTO prepareCryptoRatesDTO(String firstCurrency, String secondCurrency){
        CryptoRateDTO rateBTH = prepareCryptoRateDTO(firstCurrency, BigDecimal.TEN);
        CryptoRateDTO rateETH = prepareCryptoRateDTO(secondCurrency, BigDecimal.ONE);
        CryptoRatesDTO cryptoRatesDTO = new CryptoRatesDTO();
        ReflectionTestUtils.setField(cryptoRatesDTO, "rates", Arrays.asList(rateBTH, rateETH));
        return cryptoRatesDTO;
    }

    protected static CryptoRateDTO prepareCryptoRateDTO(String currency, BigDecimal rate) {
        CryptoRateDTO cryptoRateDTO = new CryptoRateDTO();
        ReflectionTestUtils.setField(cryptoRateDTO, "currency", currency);
        ReflectionTestUtils.setField(cryptoRateDTO, "rate", rate);
        return cryptoRateDTO;
    }

}
