package crypto.exchange.calculator;

import crypto.exchange.UnitTest;
import crypto.exchange.general.dto.ValidationErrorDTO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class CryptocurrencyInputValidationTest extends UnitTest {

    @Test
    public void shouldReturnValidationError_whenCurrencyIsNull() {
        ValidationErrorDTO result = CryptocurrencyInputValidation.validateCurrency(null);
        assertNotNull(result);
    }

    @Test
    public void shouldReturnNull_whenCurrencyIsValid() {
        ValidationErrorDTO result = CryptocurrencyInputValidation.validateCurrency("USD");
        assertNull(result);
    }

    @Test
    public void shouldReturnValidationErrorList_WhenInputsAreNotValid() {
        List<ValidationErrorDTO> noArgumentValid = CryptocurrencyInputValidation.validate(null, null, null);
        List<ValidationErrorDTO> onlyCurrencyArgumentValid = CryptocurrencyInputValidation.validate("USD", null, null);
        List<ValidationErrorDTO> onlyAmountArgumentValid = CryptocurrencyInputValidation.validate(null, BigDecimal.TEN, null);
        List<ValidationErrorDTO> onlyFilterArgumentValid = CryptocurrencyInputValidation.validate(null, null, prepareFilters());
        List<ValidationErrorDTO> validResult = CryptocurrencyInputValidation.validate("USD", BigDecimal.TEN, prepareFilters());

        assertEquals(noArgumentValid.size(), 3);
        assertEquals(onlyCurrencyArgumentValid.size(), 2);
        assertEquals(onlyAmountArgumentValid.size(), 2);
        assertEquals(onlyFilterArgumentValid.size(), 2);
        assertEquals(validResult.size(), 0);
    }

    @Test
    public void shouldReturnNull_whenCurrencyIsVali2d() {
        ValidationErrorDTO result = CryptocurrencyInputValidation.validateCurrency("USD");
        assertNull(result);
    }
}
