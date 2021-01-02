package com.sroyc.noderegistrar.main;

import java.time.LocalDateTime;

@FunctionalInterface
public interface Heartbeat {

	void beat(LocalDateTime beatTime);

}
