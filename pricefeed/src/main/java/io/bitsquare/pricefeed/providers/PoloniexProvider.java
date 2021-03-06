package io.bitsquare.pricefeed.providers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.bitsquare.http.HttpClient;
import io.bitsquare.http.HttpException;
import io.bitsquare.locale.CurrencyUtil;
import io.bitsquare.locale.TradeCurrency;
import io.bitsquare.pricefeed.PriceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;

public class PoloniexProvider {
    private static final Logger log = LoggerFactory.getLogger(PoloniexProvider.class);

    private final Set<String> supportedAltcoins;
    private final HttpClient httpClient;

    public PoloniexProvider() {
        this.httpClient = new HttpClient("https://poloniex.com/public");

        supportedAltcoins = CurrencyUtil.getAllSortedCryptoCurrencies().stream()
                .map(TradeCurrency::getCode)
                .collect(Collectors.toSet());
    }

    public Map<String, PriceData> request() throws IOException, HttpException {
        Map<String, PriceData> marketPriceMap = new HashMap<>();
        String response = httpClient.requestWithGET("?command=returnTicker", "User-Agent", "");
        LinkedTreeMap<String, Object> treeMap = new Gson().fromJson(response, LinkedTreeMap.class);
        treeMap.entrySet().stream().forEach(e -> {
            Object value = e.getValue();
            String invertedCurrencyPair = e.getKey();
            String altcoinCurrency = null;
            if (invertedCurrencyPair.startsWith("BTC")) {
                String[] tokens = invertedCurrencyPair.split("_");
                if (tokens.length == 2) {
                    altcoinCurrency = tokens[1];
                    if (supportedAltcoins.contains(altcoinCurrency)) {
                        if (value instanceof LinkedTreeMap) {
                            LinkedTreeMap<String, Object> data = (LinkedTreeMap) value;
                            marketPriceMap.put(altcoinCurrency,
                                    new PriceData(altcoinCurrency, parseDouble((String) data.get("lowestAsk")), parseDouble((String) data.get("highestBid")), parseDouble((String) data.get("last"))));
                        }
                    }
                } else {
                    log.error("invertedCurrencyPair has invalid format: invertedCurrencyPair=" + invertedCurrencyPair);
                }
            }
        });
        return marketPriceMap;
    }
}
