package com.vgo.treasury.bankerqueue.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BankerQueue {

	@NotNull
	private String walletNo;
	
	@NotNull
	private String currency;
	
	@NotNull
	private Float balanceTrxPoints;
	
	private Float balanceVGoPoints;
	
	@NotNull
	private Long serialNumber;
	
	@NotNull
	private Date queueCreatedDate;
	
	private Date queueLastModifiedDate;
}
