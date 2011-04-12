package com.plafond.jwiki;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Log {
	
	private static boolean logPerf = true;
	private static File LOG_FILE = new File("log.log");
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	
	static{
		try{
			fw = new FileWriter(LOG_FILE);
			bw = new BufferedWriter(fw);
		}
		catch(Exception ex)
		{
			System.err.println("failed to create filewriter/bufferedwriter for log!");
		}
	}
	
	public static void log(String output)
	{
		try
		{
			Scanner scanner = new Scanner(output);
			
			while(scanner.hasNextLine())
			{
				bw.write(scanner.nextLine() + "\n");
			}
		}
		catch(Exception ex)
		{
			Log.log("Failed writing to log file!");
		}
		finally
		{
			try{bw.flush();}
			catch(Exception ex){}
		}
	}
	
	public static void log(String output, Exception ex)
	{
		log(output + "\n"+ ex.getMessage() + "\n"+ getExceptionStackTrace(ex).toString());
	}
	
	public static void logPerf(String output, Long time)
	{
		if(logPerf)
		{
			log(output + time);
		}
	}
	
	private static StringBuilder getExceptionStackTrace(Exception ex)
	{
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] ste = ex.getStackTrace();
		for(StackTraceElement line : ste)
		{
			sb.append(line.toString() + "\n\t");
		}
		return sb;
	}
}
