package crypto.exchange.currency.service;

import crypto.exchange.client.CryptoExchangeFeignClient;
import crypto.exchange.currency.dto.*;
import crypto.exchange.currency.dto.CryptocurrencyQuotesResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CryptocurrencyCalculatorService {

    private final static BigDecimal FEE = new BigDecimal("0.01");

    private final CryptocurrencyValidationService cryptocurrencyValidationService;
    private final CryptoExchangeFeignClient client;

    private final String apiKey;

    @Autowired
    public CryptocurrencyCalculatorService(final CryptocurrencyValidationService cryptocurrencyValidationService,
                                           final CryptoExchangeFeignClient client,
                                           @Value("${crypto.exchange.api.key}") final String apiKey) {
        this.cryptocurrencyValidationService = cryptocurrencyValidationService;
        this.client = client;
        this.apiKey = apiKey;
    }

    public CryptocurrencyQuotesResponseDTO getQuotesForGivenCryptocurrency(String currency, Set<String> filters) {
        cryptocurrencyValidationService.validateCurrency(currency);
        CryptoRatesDTO rates = getCryptoRates(currency);
        Map<String, BigDecimal> ratesMap = isFilterEmpty(filters) ? getResult(rates) : getFilteredResult(filters, rates);
        return new CryptocurrencyQuotesResponseDTO(currency, ratesMap);
    }

    private boolean isFilterEmpty(Set<String> filters) {
        return Objects.isNull(filters) || filters.isEmpty();
    }

    private Map<String, BigDecimal> getFilteredResult(Set<String> filters, CryptoRatesDTO rates) {
        return rates.getRates()
                .parallelStream()
                .filter(rate -> filters.stream().anyMatch(filter -> filter.equals(rate.getCurrency())))
                .collect(Collectors.toConcurrentMap(CryptoRateDTO::getCurrency, CryptoRateDTO::getRate));
    }

    private Map<String, BigDecimal> getResult(CryptoRatesDTO rates) {
        return rates.getRates()
                .parallelStream()
                .collect(Collectors.toConcurrentMap(CryptoRateDTO::getCurrency, CryptoRateDTO::getRate));
    }

    public CryptocurrencyRatesResponseDTO getCryptocurrencyForecast(CryptocurrencyRequestDTO request) {
        cryptocurrencyValidationService.validateInputs(request);
        CryptoRatesDTO rates = getCryptoRates(request.getFromCurrency());
        Map<String, CryptocurrencyDTO> result = new ConcurrentHashMap<>();
        request.getToCurrencySet().parallelStream().forEach(currencyTo -> {
            rates.getRates()
                    .stream()
                    .filter(rate -> currencyTo.equals(rate.getCurrency()))
                    .findFirst()
                    .ifPresent(rate -> {
                        result.put(rate.getCurrency(), addRateToMap(request, rate.getRate()));
                    });
        });

        return new CryptocurrencyRatesResponseDTO(request.getFromCurrency(), result);
    }

    private CryptoRatesDTO getCryptoRates(String request) {
        CryptoRatesDTO rates = client.getCryptoExchangeForCurrency(apiKey, request);
        cryptocurrencyValidationService.validateCryptoRates(rates);
        return rates;
    }

    private CryptocurrencyDTO addRateToMap(CryptocurrencyRequestDTO request, BigDecimal rate) {
        return CryptocurrencyDTO.builder()
                .rate(rate)
                .amount(request.getAmount())
                .result(request.getAmount().multiply(rate).add(request.getAmount().multiply(FEE).multiply(rate)))
                .fee(FEE)
                .build();
    }
}
