package ar.itba.edu.ar.pdc.logger;

import java.io.IOException;

import org.apache.log4j.Logger;


public class XMPPLogger {
	
	private final static Logger logger = Logger.getLogger(XMPPLogger.class);
	private static XMPPLogger self = new XMPPLogger();
	
	private XMPPLogger(){
		Logger.getLogger(XMPPLogger.class);
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
	
	public void fatal(String string){
		logger.fatal(string);
	}
	
	public void debug(String string){
		if(logger.isDebugEnabled()){
			logger.debug(string);
		}
	}
	

}
