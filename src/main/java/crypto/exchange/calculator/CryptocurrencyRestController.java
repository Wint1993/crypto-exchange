package crypto.exchange.calculator;

import crypto.exchange.general.dto.ValidationErrorDTO;
import crypto.exchange.general.dto.BaseApiContractDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/currencies")
public class CryptocurrencyRestController {

    private final CryptocurrencyForecastCalculatorService cryptocurrencyForecastCalculatorService;

    private final CryptocurrencyQuotesCalculatorService cryptocurrencyQuotesCalculatorService;

    @Autowired
    CryptocurrencyRestController(final CryptocurrencyForecastCalculatorService cryptocurrencyForecastCalculatorService,
                                        final CryptocurrencyQuotesCalculatorService cryptocurrencyQuotesCalculatorService) {
        this.cryptocurrencyForecastCalculatorService = cryptocurrencyForecastCalculatorService;
        this.cryptocurrencyQuotesCalculatorService = cryptocurrencyQuotesCalculatorService;
    }

    @ApiOperation(value = "Get quotes for given cryptocurrency",
            consumes = "application/json",
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response was processed successfully"),
            @ApiResponse(code = 400, message = "Bad Request (maybe a problem with filter list)"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error - not supported exception")
    })
    @GetMapping(value = "/{currency}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseApiContractDTO<CryptocurrencyQuotesResponseDTO>> getQuotesForGivenCryptocurrency(@PathVariable final String currency, @RequestParam(value = "filter", required = false) final Set<String> filter) {
        ValidationErrorDTO validationErrorDTO = CryptocurrencyInputValidation.validateCurrency(currency);
        if (Objects.nonNull(validationErrorDTO)) {
            return ResponseEntity.badRequest().body(new BaseApiContractDTO<>(Collections.singletonList(validationErrorDTO)));
        }
        return ResponseEntity.ok(new BaseApiContractDTO<>(cryptocurrencyQuotesCalculatorService.calculate(currency, filter, null)));
    }

    @ApiOperation(value = "Get cryptocurrency forecast",
            consumes = "application/json",
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response was processed successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal Server Error - not supported exception")
    })
    @PostMapping(value = "/exchange", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseApiContractDTO<CryptocurrencyForecastResponseDTO>> getCryptocurrencyForecast(@RequestBody @NonNull final CryptocurrencyRequestDTO request) {
        List<ValidationErrorDTO> validationErrorDTOList = CryptocurrencyInputValidation.validate(request.getCurrency(), request.getAmount(), request.getFilters());
        if (!validationErrorDTOList.isEmpty()) {
            return ResponseEntity.badRequest().body(new BaseApiContractDTO<>(validationErrorDTOList));
        }
        return ResponseEntity.ok(new BaseApiContractDTO<>(cryptocurrencyForecastCalculatorService.calculate(request.getCurrency(), request.getFilters(), request.getAmount())));
    }
}
