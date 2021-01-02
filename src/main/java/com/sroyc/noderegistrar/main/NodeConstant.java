package com.sroyc.noderegistrar.main;

import java.util.UUID;

public class NodeConstant {

	private NodeConstant() {
	}

	public static final String NODE_ID = UUID.randomUUID().toString();
	public static final Integer LOCK_ID = 1;

}
