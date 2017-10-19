package com.jonydog.refy.statesources;

import com.jonydog.refy.util.RefyErrors;

import javax.annotation.PostConstruct;


public abstract class StateSource {

	public abstract void refreshState(RefyErrors errors);

	public abstract void init();

	@PostConstruct
	public void initStateSource(){
		this.init();
	}

}
