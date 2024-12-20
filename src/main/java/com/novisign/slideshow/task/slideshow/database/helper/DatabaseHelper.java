package com.novisign.slideshow.task.slideshow.database.helper;

import com.novisign.slideshow.task.slideshow.database.queryMapping.QueryMapping;
import com.novisign.slideshow.task.slideshow.exception.CustomDatabaseException;
import com.novisign.slideshow.task.slideshow.exception.DataMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DatabaseHelper {

    @Autowired
    public DatabaseHelper(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    private final DatabaseClient databaseClient;

    public <T> Mono<T> executeForOne(QueryMapping queryMapping,
                                     BindConfigurer bindConfigurer,
                                     String operationDescription) {
        return Mono.defer(() -> {
                    try {
                        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(queryMapping.getQuery());
                        spec = bindConfigurer.configure(spec);
                        return (Mono<T>) spec.map(queryMapping.getMapping()).one();
                    } catch (Exception e) {
                        return Mono.error(throwCustomDatabaseException(e, operationDescription));
                    }
                })
                .onErrorMap(e -> e instanceof DataMappingException,
                        e -> throwDataMappingException(e, operationDescription))
                .onErrorMap(e -> throwCustomDatabaseException(e, operationDescription));
    }

    public <T> Flux<T> executeForMany(QueryMapping imageQuery,
                                      BindConfigurer bindConfigurer,
                                      String operationDescription) {
        return Flux.defer(() -> {
                    try {
                        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(imageQuery.getQuery());
                        spec = bindConfigurer.configure(spec);
                        return (Flux<T>) spec.map(imageQuery.getMapping()).all();
                    } catch (Exception e) {
                        return Mono.error(throwCustomDatabaseException(e, operationDescription));
                    }
                })
                .onErrorMap(e -> e instanceof DataMappingException,
                        e -> throwDataMappingException(e, operationDescription))
                .onErrorMap(e -> throwCustomDatabaseException(e, operationDescription));
    }

    public <T> Mono<T> executeSaveOperation(QueryMapping queryAndMapping,
                                            BindConfigurer bindConfigurer,
                                            String operationDescription) {
        return Mono.defer(() -> {
                    try {
                        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(queryAndMapping.getQuery());
                        spec = bindConfigurer.configure(spec);
                        return (Mono<T>) spec.map(queryAndMapping.getMapping()).one();
                    } catch (Exception e) {
                        return Mono.error(throwCustomDatabaseException(e, operationDescription));
                    }
                })
                .onErrorMap(e -> e instanceof DataMappingException,
                        e -> throwDataMappingException(e, operationDescription))
                .onErrorMap(e -> throwCustomDatabaseException(e, operationDescription));
    }

    public <T> Mono<T> executeDeleteOperation(QueryMapping queryAndMapping,
                                              BindConfigurer bindConfigurer,
                                              String operationDescription) {
        return Mono.defer(() -> {
                    try {
                        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(queryAndMapping.getQuery());
                        spec = bindConfigurer.configure(spec);
                        return (Mono<T>) spec.fetch()
                                .rowsUpdated()
                                .map(rowsUpdated -> rowsUpdated > 0);
                    } catch (Exception e) {
                        return Mono.error(throwCustomDatabaseException(e, operationDescription));
                    }
                })
                .onErrorMap(e -> e instanceof DataMappingException,
                        e -> throwDataMappingException(e, operationDescription))
                .onErrorMap(e -> throwCustomDatabaseException(e, operationDescription));
    }

    private Throwable throwCustomDatabaseException(Throwable throwable, String operationDescription) {
        return new CustomDatabaseException("Error while " + operationDescription, throwable);
    }

    private Throwable throwDataMappingException(Throwable throwable, String operationDescription) {
        return new DataMappingException("Mapping error occurred while " + operationDescription, throwable);
    }


}
