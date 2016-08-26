package com.robo.utils;

import java.net.SocketTimeoutException;

import org.springframework.web.client.HttpClientErrorException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyErrorHandler implements ErrorHandler {
	private Response r;
	
	  @Override 
	  public Throwable handleError(RetrofitError cause) {
		  
		if (cause.isNetworkError()) {
			if (cause.getCause() instanceof SocketTimeoutException) {
				
			}else{
				try {
					  r = cause.getResponse();
					  
				} catch (NullPointerException nullPointerException) {
					nullPointerException.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			return new Exception("Socket time out");
			
		}else if(cause.getCause() instanceof HttpClientErrorException){
			if (r != null && r.getStatus() == 400) {
			     try {
					return new Exception("HTTP STATUS BAD REQUEST");
			     } catch (Exception e) {
			    	 e.printStackTrace();
			     }
		}else if (r != null && r.getStatus() == 401) {
				     try {
				    	 return new Exception("HTTP STATUS Unauthorized");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 401) {
				     try {
				    	 return	new Exception("HTTP STATUS Unauthorized");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 404) {
				     try {
				    	 return new Exception("HTTP STATUS NOT FOUND");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 304) {
				     try {
				    	 return new Exception("HTTP STATUS NOT MODIFIED");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 307) {
				     try {
				    	 return new Exception("HTTP STATUS REDIRECT");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 500) {
				     try {
				    	 return new Exception("HTTP STATUS INTERNAL SERVER ERROR");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 598) {
				     try {
				    	 return new Exception("HTTP STATUS NETWORK READ TIMEOUT ERROR");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }else if (r != null && r.getStatus() == 599) {
				     try {
				    	 return new Exception("HTTP STATUS NETWORK CONNECT TIMEOUT ERROR");
				     } catch (Exception e) {
				    	 e.printStackTrace();
				     }
			    }
			 }
		return new Exception("No network");
		}
	    
}
	
