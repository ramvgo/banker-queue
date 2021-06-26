package com.vgo.treasury.bankerqueue.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.vgo.treasury.bankerqueue.entity.BankerQueueEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BankerQueueRepository extends ReactiveMongoRepository<BankerQueueEntity, String> {

	Mono<BankerQueueEntity> findByWalletNo(String walletNo);
	
	Mono<BankerQueueEntity> findFirstByCurrencyOrderBySerialNumberDesc(String currency);
	
	Flux<BankerQueueEntity> findByCurrencyAndBalanceTrxPointsGreaterThanEqualOrderBySerialNumberAsc(String currency, Float balanceTrxPoints);
	
	Flux<BankerQueueEntity> findByCurrencyAndBalanceVGoPointsGreaterThanEqualOrderBySerialNumberAsc(String currency, Float balanceVGoPoints);
}