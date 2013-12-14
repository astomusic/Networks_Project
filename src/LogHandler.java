


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {
	Logger logger;
	
	public static String FINE_LOG_FILE = "fine.txt";
	public static String INFO_LOG_FILE = "info.txt";
	public static String WARNING_LOG_FILE = "warning.txt";

	FileHandler finefHandler;
	FileHandler infofHandler;
	FileHandler warningfHandler;

	SimpleFormatter formatter = new SimpleFormatter();

	private static LogHandler logHandler = new LogHandler();
	
	private LogHandler() {
		logger = Logger.getLogger("logging");
		try {
			finefHandler = new FileHandler(FINE_LOG_FILE, true);
			infofHandler = new FileHandler(INFO_LOG_FILE, true);
			warningfHandler = new FileHandler(WARNING_LOG_FILE, true);
			
			finefHandler.setLevel(Level.FINE);
			infofHandler.setLevel(Level.INFO);
			warningfHandler.setLevel(Level.WARNING);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.setLevel(Level.ALL);
		logger.addHandler(finefHandler);
		logger.addHandler(infofHandler);
		logger.addHandler(warningfHandler);
		finefHandler.setFormatter(formatter);
	}

	public static LogHandler getHandler() {
		if(logHandler == null)
			logHandler =  new LogHandler();
		return logHandler;
	}
	
	public void fine(String log) {
		logger.fine(log);
	}
	
	public void info(String log) {
		logger.info(log);
	}
	
	public void warning(String log) {
		logger.warning(log);
	}

}
