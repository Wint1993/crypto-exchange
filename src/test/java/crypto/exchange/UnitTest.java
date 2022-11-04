package crypto.exchange;

import crypto.exchange.currency.dto.CryptocurrencyRequestDTO;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTest {

    protected final static String BTC_CURRENCY = "BTC";
    protected final static String ETH_CURRENCY = "ETH";
    protected final static String DC_CURRENCY = "DC";
    protected final static String BTH_CURRENCY = "BTH";

    protected static CryptocurrencyRequestDTO prepareCryptocurrencyRequestDTO(BigDecimal amount, String fromCurrency, Set<String> toCurrencySet) {
        CryptocurrencyRequestDTO cryptocurrencyRequestDTO = new CryptocurrencyRequestDTO();
        cryptocurrencyRequestDTO.setAmount(amount);
        cryptocurrencyRequestDTO.setFromCurrency(fromCurrency);
        cryptocurrencyRequestDTO.setToCurrencySet(toCurrencySet);
        return cryptocurrencyRequestDTO;
    }

    protected static Set<String> prepareToCurrencySet(){
        return Stream.of(ETH_CURRENCY, DC_CURRENCY).collect(Collectors.toSet());
    }
}
