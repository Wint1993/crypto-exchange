package crypto.exchange.currency.rest;

import crypto.exchange.currency.dto.CryptocurrencyRequestDTO;
import crypto.exchange.currency.dto.CryptocurrencyRatesResponseDTO;
import crypto.exchange.currency.dto.CryptocurrencyQuotesResponseDTO;
import crypto.exchange.currency.exception.IncorrectInputDataException;
import crypto.exchange.currency.service.CryptocurrencyCalculatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Set;

@Log4j2
@RestController
@RequestMapping("/currencies")
public class CryptocurrencyCalculatorRestController {

    private final CryptocurrencyCalculatorService cryptoCurrencyCalculatorService;

    @Autowired
    public CryptocurrencyCalculatorRestController(final CryptocurrencyCalculatorService cryptoCurrencyCalculatorService) {
        this.cryptoCurrencyCalculatorService = cryptoCurrencyCalculatorService;
    }

    @ApiOperation(value = "Get quotes for given cryptocurrency",
            consumes = "application/json",
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response was processed successfully"),
            @ApiResponse(code = 400, message = "Bad Request (maybe a problem with filter list)"),
            @ApiResponse(code = 500, message = "Internal Server Error - not supported exception")
    })
    @GetMapping(value = "/{currency}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CryptocurrencyQuotesResponseDTO> getQuotesForGivenCryptocurrency(@PathVariable final String currency,
                                                                                           @RequestParam(value = "filter", required = false) final Set<String> filter) {
        try {
            return ResponseEntity.ok(cryptoCurrencyCalculatorService.getQuotesForGivenCryptocurrency(currency, filter));
        } catch (IncorrectInputDataException ex) {
            log.warn("Invalid input data", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(MessageFormat.format("Exception during getting crypto currency for currency: {0}", currency), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "Get cryptocurrency forecast",
            consumes = "application/json",
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response was processed successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error - not supported exception")
    })
    @PostMapping(value = "/exchange", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CryptocurrencyRatesResponseDTO> getCryptocurrencyForecast(@RequestBody @NonNull final CryptocurrencyRequestDTO request) {
        try {
            return ResponseEntity.ok(cryptoCurrencyCalculatorService.getCryptocurrencyForecast(request));
        } catch (IncorrectInputDataException ex) {
            log.warn("Invalid input data", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(MessageFormat.format("Exception during getting crypto currency for currency: {0}", request.getFromCurrency()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
