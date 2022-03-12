/**
 * 
 */
package com.synechron.event;

import java.util.Date;
import java.util.UUID;

/**
 * 
 * @author darshan
 * Common Event interface
 *
 */
public interface Event {
	UUID getEventId();
	Date getEventDate();
}
