package com.safran.ses.casablanca.mytex.security1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.safran.ses.casablanca.mytex.service.UserService;
import com.safran.ses.casablanca.mytex.service.model.User;


public class MyAuthenticationProvider implements AuthenticationProvider {
   
	
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException { 
				
		Authentication res = isValid(authentication);
		
		if (!res.isAuthenticated()) {
			
			throw new BadCredentialsException("Bad credentials");
		}
		return res;
	}

	private Authentication isValid(final Authentication authentication) {
		Authentication res = authentication;
		System.out.println("Selected method: " + ((MyAuthenticationDetails) authentication.getDetails()).getMethod());

		User user = userService.findUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
		if (null != user) {
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();

			
				authorities.add(new SimpleGrantedAuthority(user.getRole()));
			
			final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);

			result.setDetails(authentication.getDetails());

			res = result;

		}

		return res;
	}


	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	protected Authentication createSuccessAuthentication(final Authentication authentication) {

		final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
		result.setDetails(authentication.getDetails());

		return result;
	}
}