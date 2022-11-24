package crypto.exchange.calculator;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

@Service
class CurrencyTypeStrategyFactory {

    private final Map<CalculatorTypeEnum, CalculatorTypeStrategy> strategiesMap;

    CurrencyTypeStrategyFactory(final List<CalculatorTypeStrategy> strategies) {
        this.strategiesMap = strategies
                .stream()
                .collect(toMap(CalculatorTypeStrategy::getCalculatorType, strategy -> strategy));
    }

    CalculatorTypeStrategy findStrategy(final CalculatorTypeEnum type) {
        final CalculatorTypeStrategy strategy = strategiesMap.get(type);
        if (Objects.isNull(strategy)) {
            throw new IllegalStateException(MessageFormat.format("Problems during getting calculator strategy: {0}", type));
        }
        return strategy;
    }
}
