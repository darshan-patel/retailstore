/**
 * 
 */
package com.synechron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * Token Validation response model
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
	// validating token and return user id
	private long userId;
}