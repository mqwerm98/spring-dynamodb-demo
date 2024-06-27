package com.observer.demo.infra.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.dynamodb.region}")
    private String amazonDynamoDBRegion;

    @Value("${aws.credentials.access-key}")
    private String amazonAWSAccessKey;

    @Value("${aws.credentials.secret-key}")
    private String amazonAWSSecretKey;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }

    private AmazonDynamoDB buildAmazonDynamoDB() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);

        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                amazonDynamoDBEndpoint,
                                amazonDynamoDBRegion
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}

