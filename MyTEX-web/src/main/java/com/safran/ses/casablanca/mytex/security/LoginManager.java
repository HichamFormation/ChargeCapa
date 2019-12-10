package com.safran.ses.casablanca.mytex.security;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@ManagedBean
@SessionScoped
public class LoginManager implements Serializable{

	public String doLogin() throws IOException, ServletException{
		
	ExternalContext context=FacesContext.getCurrentInstance().getExternalContext();
		
	RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
		
	dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
		
    FacesContext.getCurrentInstance().responseComplete();
        
        return null;
	}
}
