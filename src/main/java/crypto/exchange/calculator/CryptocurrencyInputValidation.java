package crypto.exchange.calculator;

import crypto.exchange.general.dto.ValidationErrorDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class CryptocurrencyInputValidation {

    static List<ValidationErrorDTO> validate(String currency, BigDecimal amount, Set<String> filters) {
        List<ValidationErrorDTO> validationErrorList = new ArrayList<>();
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            validationErrorList.add(new ValidationErrorDTO("amount", "Invalid amount", String.valueOf(amount)));
        }
        if (Objects.isNull(filters) || filters.isEmpty()) {
            validationErrorList.add(new ValidationErrorDTO("to", "Invalid filter list", String.valueOf(filters)));

        }
        ValidationErrorDTO currencyValidation = validateCurrency(currency);
        if (Objects.nonNull(currencyValidation)) {
            validationErrorList.add(currencyValidation);
        }
        return validationErrorList;
    }

    static ValidationErrorDTO validateCurrency(String currency) {
        return (Objects.isNull(currency)) ? new ValidationErrorDTO("from", "Invalid currency", null) : null;
    }
}
