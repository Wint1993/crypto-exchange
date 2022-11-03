package crypto.exchange.client;

import crypto.exchange.currency.dto.CryptoRatesDTO;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cryptoExchangeFeignClient", url = "${crypto.exchange.api.url}")
public interface CryptoExchangeFeignClient {

   @GetMapping("/{currency}")
   CryptoRatesDTO getCryptoExchangeForCurrency(@RequestHeader("X-CoinAPI-Key") @NonNull final String apiKey,
                                               @PathVariable String currency);
}
