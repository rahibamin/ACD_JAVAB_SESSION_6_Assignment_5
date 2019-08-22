package Session6.Assignment5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

class WriteLogException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WriteLogException() {
		super("Error writing to file.");
	}
	
}

class FileTooLargeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileTooLargeException() {
		super("File is too large.");
	}
	
}
class LogWriter{
	File f;
	String filename = "log";
	BufferedWriter br;
	
	
	public LogWriter() throws WriteLogException {
		//see if file exists
		f = new File(filename + ".txt");
		try {
			if (!f.exists()) {
				f.createNewFile();
			} else {
				File n = new File(filename + "-" + timeStamp()+ ".txt");
				f.renameTo(n);
				f.createNewFile();
			}
			br = new BufferedWriter(new FileWriter(f,true));
		} catch (Exception e) {
			throw new WriteLogException();
		}
	}
	
	String timeStamp() {
		Calendar now = Calendar.getInstance();
		
		String s = String.format("%02d%02d%04d_%02d%02d%02d", now.get(Calendar.DATE), 
				now.get(Calendar.MONTH), now.get(Calendar.YEAR), 
				now.get(Calendar.HOUR), now.get(Calendar.MINUTE), 
				now.get(Calendar.SECOND))
				+ (now.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
		return s;
	}
	
	void close() {
		try {
			if (br != null) {
				br.flush();
				br.close();
				System.out.println("closed.");
			}
		} catch(IOException e) {
			
		}catch (Exception e) {
			
		}
	}
	
	void writeToFile(String textType, String text) throws FileTooLargeException, WriteLogException{
		if (br != null) {
			if ((f.length() /1048576) >= 5) {
				throw new FileTooLargeException();
			}
			if (!"LOG".equalsIgnoreCase(textType) && !"DEBUG".equalsIgnoreCase(textType) && !"ERROR".equalsIgnoreCase(textType)) {
				throw new WriteLogException();
			}
			try {
				br.write(String.format("[%s]:[%s]:", timeStamp(), textType) + text);
				br.newLine();
				br.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new WriteLogException();
			} catch (Exception e) {
				throw new WriteLogException(); 
			}
		}
	}
	
	void writeToFile(String text) throws FileTooLargeException, WriteLogException{
		if (br != null) {
			if ((f.length() /1048576) > 5) {
				throw new FileTooLargeException();
			}
			try {
				br.write(String.format("[%s]:[%s]:", timeStamp(), "TEXT") + text);
				br.newLine();
				br.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new WriteLogException();
			} 
		}
	}
}

public class WriteLog {
	public static void main(String[] args) {
		
		System.out.println("Openning Log...");
		LogWriter lw = null;
		int rand = 1;
		try {
			lw = new LogWriter();
			
			System.out.println("Demonstrate write to log error...");
			lw.writeToFile("BLAH", "This should not be written");
			
		} catch(WriteLogException e) {
			e.printStackTrace();
		} catch(FileTooLargeException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("Writing to logger...");
		
			for(int i=0; i < 100000; i++) {
				rand = (int)Math.ceil(Math.random() * 4);
				switch(rand) {
				case 1:
					lw.writeToFile("LOG", randomString((int)(Math.random()*1000+1)));
				case 2:
					lw.writeToFile("DEBUG", randomString((int)(Math.random()*1000+1)));
				case 3:
					lw.writeToFile("ERROR", randomString((int)(Math.random()*1000+1)));
				case 4:
					lw.writeToFile(randomString((int)(Math.random()*1000+1)));
				}
			}
			
		} catch (FileTooLargeException e) {
			e.printStackTrace();
		} catch (WriteLogException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		lw.close();
	}
	
	public static String randomString(int len) {
		  
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = len;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	 
	    return generatedString;
	}
}