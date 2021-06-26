package com.vgo.treasury.bankerqueue.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vgo.treasury.bankerqueue.controller.BankerQueueHandler;

@Configuration
public class BankerQueueRouter {

	@Bean
	public RouterFunction<ServerResponse> bankerQueueEndpoint(BankerQueueHandler bankerQueueHandler) {
		
		return RouterFunctions
                .route(GET("/bankerqueue/{username}/userdetails").and(accept(MediaType.APPLICATION_JSON))
                        , bankerQueueHandler::getBankerByUsername)
                .andRoute(GET("/bankerqueue/{currency}/{transferAmount}/banker-details").and(accept(MediaType.APPLICATION_JSON))
                        , bankerQueueHandler::getBankerByCurrencyAndTransferAmount)
                .andRoute(POST("/bankerqueue").and(accept(MediaType.APPLICATION_JSON))
                        , bankerQueueHandler::saveBankerQueue)
				.andRoute(PUT("/bankerqueue/{username}").and(accept(MediaType.APPLICATION_JSON))
						, bankerQueueHandler::updateBankerQueue);
	}
}
