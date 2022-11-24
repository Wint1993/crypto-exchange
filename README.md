# crypto-exchange

## Version 1.0.0 - Published 2022 Nov 13


This project is about cryptocurrency exchange. COINAPI was used in project (https://www.coinapi.io) If you want to test your endpoints, open localhost:8080 in a browser, you will be redirected to swagger ui.

## Project structure (packages)

- `configuration` - package for configuring the project
  
- `general` - package containing things that can be used throughout the project

- `rates` - package responsible for getting crypto rates from COINAPI

- `calculator` - package responsible for calculate cryptocurrency 

## general: 
- `BaseApiContractDTO` - dto which we return each time a request comes to the application, it contains a responsive body and a list of errors
- `ValidationErrorDTO` - list of errors which is used in `BaseApiContractDTO`
- `EmptyListException` - the exception we throw when we have an empty list
- `GeneralExceptionHandler` - global exception handler

## configuration:
- `SwaggerConfiguration` - needed to enable swagger in project

## rates:
- `CryptoRatesDTO` and `CryptoRateDTO` - needed to map data from COINAPI
- `CryptoRatesFeignClient` - client interface which use Feign Client to help connect with another API
- `CryptoRatesService` - service responsible for downloading and returning a list of cryptocurrencies 

## calculator:
- `AbstractCryptocurrencyCalculatorService` - an abstract class that contains the methods needed to calculate cryptocurrencies
- `AbstractCryptocurrencyResponseDTO` - an abstract class that returns the result of a calculation
- `CryptocurrencyDTO` - class used in response for forecast cryptocurrency
- `CryptocurrencyForecastCalculatorService` - subclass of `AbstractCryptocurrencyCalculatorService` which calculate cryptocurrency forecast
- `CryptocurrencyForecastResponseDTO` - result of `CryptocurrencyForecastCalculatorService` calculation
- `CryptocurrencyInputValidation` - validation class for incoming input in `CryptocurrencyRestController`
- `CryptocurrencyQuotesCalculatorService` - subclass of `AbstractCryptocurrencyCalculatorService` which return cryptocurrency for given currency
- `CryptocurrencyQuotesResponseDTO` - result of `CryptocurrencyQuotesCalculatorService` calculation
- `CryptocurrencyRequestDTO` - request class used in `CryptocurrencyRestController`  
- `CryptocurrencyRestController` - rest controller with two endpoints

## Responses 

the application always returns the following response:

```json
{
  "SpecifyContract": {
    
  },
  "ValidationError": [
    {
      "ErrorDescription": "string",
      "FieldName": "string",
      "Value": "string"
    }
  ]
}
```

The contract was created for this reason, to return the appropriate details related to errors that may occur.
SpecifyContract: response body (only if everything is ok, status = 200)
ValidationError: validation error list (if something goes wrong, status != 200)


## Open Endpoints

* Get crypto quotes for given currency : `GET /currencies/{currency}` 
 
 **URL** : `/currencies/{currency}`

**Method** : `GET`

**Auth required** : NO

**Data constraints:**
```
- currency: "valid currency, like BTC, USD" - Parameter Type: path, DataType: string,

- filters: (optional) "[valid filters currency USD]" - Parameter Type: query, DataType: Array[string],
```

**Data example**

```
localhost:8080/currencies/BTC
localhost:8080/currencies/BTC?filter=USD
localhost:8080/currencies/BTC?filter=ETH&filter=USD
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
   {
    "SpecifyContract": {
        "source": "BTC",
        "rates": {
            "USD": 16512.807872532059990189761653
        }
    },
    "ValidationError": []
}
}
```

## Error Response

**Condition** : If 'currency' not exist

**Code** : `404 NOT FOUND`

**Content** :

```json
{
    "SpecifyContract": null,
    "ValidationError": [
        {
            "FieldName": null,
            "ErrorDescription": "Error during get cryptocurrency list for currency: BT",
            "Value": null
        }
    ]
}
```



* Get crypto forecast : `POST /currencies/{exchange}`

 **URL** : `/currencies/{exchange}`

**Method** : `POST`

**Auth required** : NO

**Data constraints:**
Parameter Type: body
```json
{
  "amount": "valid amount greater than zero", (DataType: number)
  "from": "valid cryptocurrency to exchange", (DataType: string)
  "to": [
    "valid cryptocurrency filter"             (DataType: Array[string])
  ]
}

```

**Data example**

```json
{
  "amount": 10, 
  "from": "USD", 
  "to": [
    "BTC", "ETH"
  ]
}
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
    "SpecifyContract": {
        "source": "USD",
        "rates": {
            "BTC": {
                "rate": 0.0000606400140792245447071337,
                "amount": 30,
                "result": 0.001837392426600503704626151110,
                "fee": 0.01
            },
            "ETH": {
                "rate": 0.0008182474139303567930978728,
                "amount": 30,
                "result": 0.024792896642089810830865545840,
                "fee": 0.01
            }
        }
    },
    "ValidationError": []
}
```

## Error Response

**Condition** : If 'amount' and 'from' and 'to' combination is wrong.

**Code** : `400 BAD REQUEST`

**Content** :

```json
{
    "SpecifyContract": null,
    "ValidationError": [
        {
            "FieldName": "from",
            "ErrorDescription": "Invalid currency",
            "Value": null
        }
    ]
}
```

