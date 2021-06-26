package com.vgo.treasury.bankerqueue.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "bankerqueue")
public class BankerQueueEntity {

	@Id
	private String walletNo;
	
	@NotNull
	private String currency;
	
	@NotNull
	private Float balanceTrxPoints;
	
	private Float balanceVGoPoints;
	
	@NotNull
	private Long serialNumber;
	
	private Date queueCreatedDate;
	
	private Date queueLastModifiedDate;
}
