package com.vgo.treasury.bankerqueue.service;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vgo.treasury.bankerqueue.entity.BankerQueueEntity;
import com.vgo.treasury.bankerqueue.model.BankerQueue;
import com.vgo.treasury.bankerqueue.model.BankerQueueResponse;
import com.vgo.treasury.bankerqueue.repository.BankerQueueRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BankerQueueService {

	private final static Logger logger = LoggerFactory.getLogger(BankerQueueService.class);
	
	@Autowired
	private BankerQueueRepository bankerQueueRepository;
	
	/**
	 * To create BankerQueue
	 * parameters BankerQueueEntity
	 * returns
	*/
	public Mono<Object> create(BankerQueue bankerQueue) {
		logger.info("Inserting BankerQueue data...");
		BankerQueueEntity newBankerQueue = inbound(bankerQueue);
		return bankerQueueRepository.findByWalletNo(bankerQueue.getWalletNo())
				.defaultIfEmpty(newBankerQueue)
				.flatMap(result -> {
					
					if (result.getWalletNo() == null) {
						result.setQueueCreatedDate(new Date());
						return bankerQueueRepository.save(result)
								.flatMap(saveBankerQueue -> {
									BankerQueueResponse response = new BankerQueueResponse();
									response.setId(saveBankerQueue.getWalletNo());
									response.setResponseMessage("Saved");
									response.setResponseStatusCode(101);
									return Mono.just(response);
								});
					} else {
						return Mono.error(new Exception("Already exists"));
					}
				});
	}
	
	/**
	 * To update BankerQueue
	 * parameters BankerQueueEntity
	 * returns
	*/
	public Mono<BankerQueueResponse> update(String walletNo, BankerQueue bankerQueue) {
		logger.info("Updating BankerQueue data...");
		return bankerQueueRepository.findByWalletNo(walletNo)
				.flatMap(bankerQueueEntity -> {
					return bankerQueueRepository.findFirstByCurrencyOrderBySerialNumberDesc(bankerQueueEntity.getCurrency())
							.flatMap(bQueue -> {
								//deduct transfer amount from balance???
								bankerQueueEntity.setSerialNumber(bQueue.getSerialNumber() + 1);
								bankerQueueEntity.setQueueLastModifiedDate(new Date());
								return bankerQueueRepository.save(bankerQueueEntity).flatMap(updateBankerQueue -> {
									BankerQueueResponse response = new BankerQueueResponse();
									response.setId(updateBankerQueue.getWalletNo());
									response.setResponseMessage("Saved");
									response.setResponseStatusCode(101);
									return Mono.just(response);
								});
						
							}).switchIfEmpty(Mono.error(new Exception("Banker queue not found!")));
			
				}).switchIfEmpty(Mono.error(new Exception("Banker queue not found!")));
	}
	
	/**
	 * To getBanker
	 * parameters user name
	 * returns
	*/
	public Mono<BankerQueue> getBankerByWalletNo(String walletNo) {
		logger.info("Fetching BankerQueue data for the wallet no {}", walletNo);
		return bankerQueueRepository.findByWalletNo(walletNo).map(banker -> outbound(banker));
	}
	
	/**
	 * To getBanker
	 * parameters currency
	 * returns
	*/
	public Flux<BankerQueue> getBankersByCurrencyAndTransferAmount(String currency, Float transferAmount) {
		logger.info("Fetching BankerQueue data for the currency {} and transfer amount{} ", currency, transferAmount);
		return bankerQueueRepository.findByCurrencyAndBalanceTrxPointsGreaterThanEqualOrderBySerialNumberAsc(currency, transferAmount).map(banker -> outbound(banker));
	}
	
	/**
	 * parameters BankerQueueEntity
	 * returns
	*/
	private BankerQueueEntity inbound(BankerQueue bankerQueue) {
		logger.info("Building BankerQueueEntity data...");
		return BankerQueueEntity.builder()
				.walletNo(bankerQueue.getWalletNo())
				.currency(bankerQueue.getCurrency())
				.balanceTrxPoints(bankerQueue.getBalanceTrxPoints())
				.balanceVGoPoints(bankerQueue.getBalanceVGoPoints())
				.serialNumber(bankerQueue.getSerialNumber())
				.queueCreatedDate(bankerQueue.getQueueCreatedDate())
				.queueLastModifiedDate(bankerQueue.getQueueLastModifiedDate())
				.build();
	}
	
	/**
	 * parameters BankerQueue
	 * returns
	*/
	private BankerQueue outbound(BankerQueueEntity bankerQueueEntity) {
		logger.info("Building BankerQueueEntity data..." + bankerQueueEntity);
		return BankerQueue.builder()
				.walletNo(bankerQueueEntity.getWalletNo())
				.currency(bankerQueueEntity.getCurrency())
				.balanceTrxPoints(bankerQueueEntity.getBalanceTrxPoints())
				.balanceVGoPoints(bankerQueueEntity.getBalanceVGoPoints())
				.serialNumber(bankerQueueEntity.getSerialNumber())
				.queueCreatedDate(bankerQueueEntity.getQueueCreatedDate())
				.queueLastModifiedDate(bankerQueueEntity.getQueueLastModifiedDate())
				.build();
	}
}
