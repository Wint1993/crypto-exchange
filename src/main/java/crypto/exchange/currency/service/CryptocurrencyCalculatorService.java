package crypto.exchange.currency.service;

import crypto.exchange.client.CryptoExchangeFeignClient;
import crypto.exchange.currency.dto.*;
import crypto.exchange.currency.dto.CryptocurrencyQuotesResponseDTO;
import crypto.exchange.currency.exception.EmptyListException;
import crypto.exchange.currency.exception.IncorrectInputDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CryptocurrencyCalculatorService {

    private final static BigDecimal FEE = new BigDecimal("0.01");

    private final CryptoExchangeFeignClient client;

    private final String apiKey;

    @Autowired
    public CryptocurrencyCalculatorService(final CryptoExchangeFeignClient client,
                                           @Value("${crypto.exchange.api.key}") final String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    public CryptocurrencyQuotesResponseDTO getQuotesForGivenCryptocurrency(String currency, Set<String> filters) {
        validateCurrency(currency);
        CryptoRatesDTO rates = client.getCryptoExchangeForCurrency(apiKey, currency);
        validateCryptoRates(rates);
        return CryptocurrencyQuotesResponseDTO.builder()
                .source(currency)
                .rates((Objects.isNull(filters) || filters.isEmpty()) ? getResult(rates) : getFilteredResult(filters, rates))
                .build();
    }

    private Map<String, BigDecimal> getFilteredResult(Set<String> filters, CryptoRatesDTO rates) {
        return rates.getRates()
                .stream() //TODO lub .parallelStream() jeżeli chcemy wątkowości
                .filter(rate -> filters.stream().anyMatch(filter -> filter.equals(rate.getCurrency())))
                .collect(Collectors.toMap(CryptoRateDTO::getCurrency, CryptoRateDTO::getRate));
    }

    private Map<String, BigDecimal> getResult(CryptoRatesDTO rates) {
        return rates.getRates()
                .stream() //TODO lub .parallelStream() jeżeli chcemy wątkowości
                .collect(Collectors.toMap(CryptoRateDTO::getCurrency, CryptoRateDTO::getRate));
    }

    public CryptocurrencyResponseDTO getCryptocurrencyForecast(CryptocurrencyRequestDTO request) {
        validateInputs(request);
        CryptoRatesDTO rates = client.getCryptoExchangeForCurrency(apiKey, request.getFromCurrency());
        validateCryptoRates(rates);
        HashMap<String, CryptocurrencyDTO> result = new HashMap<>();
        /*request.getToCurrencySet().stream().forEach(currencyTo -> {*/
        // TODO: w zadaniu była możliwość uwzględnienia wątkowości, w swojej niedługiej karierze miałem okazję napisać
        //  kilka wątków lecz nigdy komercyjnie, w pracy nie piszemy wątków. Pozwoliłem sobie użyć featurea javy 8 parallelStream
        //  ze względu na to, że nie interesuje nas który wątek pierwszy rozpocznie zadanie tylko interesuje nas wynik końcowy,
        //  który w tym przypadku zawsze będzie taki sam.
        request.getToCurrencySet().parallelStream().forEach(currencyTo -> {
            rates.getRates()
                    .stream()
                    .filter(rate -> currencyTo.equals(rate.getCurrency()))
                    .findFirst()
                    .ifPresent(rate -> {
                        addRateToMap(request, result, rate);
                    });
        });

        return CryptocurrencyResponseDTO.builder()
                .from(request.getFromCurrency())
                .rates(result)
                .build();
    }

    private void addRateToMap(CryptocurrencyRequestDTO request, HashMap<String, CryptocurrencyDTO> result, CryptoRateDTO rate) {
        CryptocurrencyDTO currency = CryptocurrencyDTO.builder()
                .rate(rate.getRate())
                .amount(request.getAmount())
                .result(request.getAmount().multiply(rate.getRate()).add(request.getAmount().multiply(FEE)))
                .fee(FEE)
                .build();
        result.put(rate.getCurrency(), currency);
    }

    private void validateInputs(CryptocurrencyRequestDTO response) {
        if (Objects.isNull(response.getAmount()) || response.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectInputDataException(MessageFormat.format("Invalid amount, input amount: {0}", response.getAmount()));
        }
        if (Objects.isNull(response.getToCurrencySet()) || response.getToCurrencySet().isEmpty()) {
            throw new IncorrectInputDataException(MessageFormat.format("Invalid input list to, input to: {0}", response.getToCurrencySet()));
        }
        validateCurrency(response.getFromCurrency());
    }

    private void validateCurrency(String currency) {
        if (Objects.isNull(currency)) {
            throw new IncorrectInputDataException("Invalid input currency, currency is null");
        }
    }

    private void validateCryptoRates(CryptoRatesDTO rates) {
        if (Objects.isNull(rates) || Objects.isNull(rates.getRates()) || rates.getRates().isEmpty()) {
            throw new EmptyListException("Error during get crypto exchange for currency");
        }
    }

}
