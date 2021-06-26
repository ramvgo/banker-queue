package com.vgo.treasury.bankerqueue.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BankerQueueResponse {

	private String id;
	
	private String responseMessage;
	
	private Integer responseStatusCode;
}
