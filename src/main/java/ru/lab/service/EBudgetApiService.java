package ru.lab.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EBudgetApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EBudgetApiService.class.getName());

    private static final String BASE_URL = "https://budget.gov.ru/epbs/registry/ubpandnubp/data";
    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final CloseableHttpClient httpClient;

    public EBudgetApiService() {
        this.httpClient = HttpClients.createDefault();
    }

    public String getPage(
            LocalDate from,
            LocalDate to,
            int pageNumber
    ) {
        return getPage(from, to, pageNumber, 100);
    }

    // Если pageSize слишком большой - большая нагрузка на ОЗУ
    // pageSize = 1000 -> на странице 1000 объектов -> около 100 Мб
    @Deprecated
    public String getPage(
            LocalDate from,
            LocalDate to,
            int pageNumber,
            int pageSize) {

        try {
            final URI uri = buildUri(from, to, pageNumber, pageSize);
            LOGGER.info("Запрос {}", uri);
            final HttpGet request = new HttpGet(uri);


            try (CloseableHttpResponse response = httpClient.execute(request)) {

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new RuntimeException("Request failed with status code: " + statusCode);
                }

                final HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static URI buildUri(LocalDate from, LocalDate to, int page, int pageSize) throws URISyntaxException {

        return new URIBuilder(BASE_URL)
                .addParameter("filtermindateupdate", from.format(API_DATE_FORMATTER))
                .addParameter("filtermaxdateupdate", to.format(API_DATE_FORMATTER))
                .addParameter("pageSize", String.valueOf(pageSize))
                .addParameter("page", String.valueOf(page))
                .build();
    }

}
