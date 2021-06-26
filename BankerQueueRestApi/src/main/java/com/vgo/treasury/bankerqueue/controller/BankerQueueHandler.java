package com.vgo.treasury.bankerqueue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vgo.treasury.bankerqueue.entity.BankerQueueEntity;
import com.vgo.treasury.bankerqueue.model.BankerQueue;
import com.vgo.treasury.bankerqueue.service.BankerQueueService;
import reactor.core.publisher.Mono;

@Component
public class BankerQueueHandler {

	@Autowired
	private BankerQueueService bankerQueueService;
	
	static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
	
	public Mono<ServerResponse> getBankerByUsername(ServerRequest serverRequest) {
		return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bankerQueueService.getBankerByWalletNo(serverRequest.pathVariable("walletNo")), BankerQueue.class)
                .switchIfEmpty(notFound);
	}
	
	public Mono<ServerResponse> getBankerByCurrencyAndTransferAmount(ServerRequest serverRequest) {
		return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bankerQueueService.getBankersByCurrencyAndTransferAmount(serverRequest.pathVariable("currency"), Float.parseFloat(serverRequest.pathVariable("transferAmount"))), BankerQueue.class)
                .switchIfEmpty(notFound);
	}
	
	public Mono<ServerResponse> saveBankerQueue(ServerRequest serverRequest) {
        Mono<BankerQueue> bankerQueueToSave = serverRequest.bodyToMono(BankerQueue.class);
        return bankerQueueToSave.flatMap(bankerQueue ->
          		ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(bankerQueueService.create(bankerQueue), BankerQueueEntity.class)
        );
    }
	
	public Mono<ServerResponse> updateBankerQueue(ServerRequest serverRequest) {
        Mono<BankerQueue> bankerQueueToUpdate = serverRequest.bodyToMono(BankerQueue.class);
        return bankerQueueToUpdate.flatMap(bankerQueue ->
          		ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(bankerQueueService.update(serverRequest.pathVariable("username"), bankerQueue), BankerQueueEntity.class)
        );
    }
}
