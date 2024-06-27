package com.observer.demo.domain.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    @Override
    public String convert(final LocalDateTime time) {
        return time.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public LocalDateTime unconvert(final String stringValue) {
        return LocalDateTime.parse(stringValue, DateTimeFormatter.ISO_DATE_TIME);
    }
}
