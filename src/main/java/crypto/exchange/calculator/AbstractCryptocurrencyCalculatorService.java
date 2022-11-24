package crypto.exchange.calculator;

import crypto.exchange.rates.CryptoRatesDTO;
import crypto.exchange.rates.CryptoRatesService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractCryptocurrencyCalculatorService<T extends AbstractCryptocurrencyResponseDTO<Y>, Y> {

    protected final static BigDecimal FEE = new BigDecimal("0.01");

    private final CryptoRatesService cryptoRatesService;

    AbstractCryptocurrencyCalculatorService(final CryptoRatesService cryptoRatesService) {
        this.cryptoRatesService = cryptoRatesService;
    }

    protected T calculateCryptocurrency(final String currency, final Set<String> filters, final BigDecimal amount) {
        CryptoRatesDTO rates = cryptoRatesService.getCryptoRates(currency);
        Map<String, Y> resultMap = populateCurrencyMap(rates, filters, amount);
        return createResult(currency, resultMap);
    }

    private Map<String, Y> populateCurrencyMap(final CryptoRatesDTO rates, final Set<String> filters, final BigDecimal amount) {
        Map<String, Y> currencyResultMap = new ConcurrentHashMap<>();
        rates.getRates().parallelStream().forEach(rate -> {
            if (isFilterEmpty(filters)) {
                currencyResultMap.put(rate.getCurrency(), returnCurrencyResult(rate.getRate(), amount));
            } else {
                filters.stream()
                        .filter(currencyTo -> currencyTo.equals(rate.getCurrency()))
                        .findFirst()
                        .ifPresent(currencyTo -> {
                            currencyResultMap.put(rate.getCurrency(), returnCurrencyResult(rate.getRate(), amount));
                        });
            }
        });
        return currencyResultMap;
    }

    private boolean isFilterEmpty(final Set<String> filters) {
        return Objects.isNull(filters) || filters.isEmpty();
    }

    protected abstract T createResult(final String currency, final Map<String, Y> currencyMap);

    protected abstract Y returnCurrencyResult(final BigDecimal rate, final BigDecimal amount);
}
