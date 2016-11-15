package ar.edu.itba.pdc.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;



public class XMPPLogger {
	
	private final static Logger logger = LoggerFactory.getLogger(XMPPLogger.class);
	private static XMPPLogger self = new XMPPLogger();
	
	private XMPPLogger(){

	}

	public static XMPPLogger getInstance() {
		return self;
	}

	public void error(String string, IOException e) {
		logger.error(string, e);
	}
	
	public void error(String string){
		logger.error(string);
	}
	
	public void info(String string){
		if(logger.isInfoEnabled()){
			logger.info(string);
		}
	}
	
	public void warn(String string){
		logger.warn(string);
	}
	
	public void debug(String string){
		if(logger.isDebugEnabled()){
			logger.debug(string);
		}
	}
	

}
