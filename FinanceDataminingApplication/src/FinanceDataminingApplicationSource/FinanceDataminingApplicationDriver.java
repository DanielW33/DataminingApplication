package FinanceDataminingApplicationSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.json.*;



public class FinanceDataminingApplicationDriver {
	public static void main(String[] args) throws IOException, InterruptedException {
		
	
		
		long Starttime = System.currentTimeMillis();
		
		DataCollection data = new DataCollection();
		Map<Integer, String> Tickers = data.getTickers();
		File basepathFolder = new File(data.getBasePath());
		File[] ListofFiles = basepathFolder.listFiles();
		
		List<File> Files = new ArrayList<>();
		Files.addAll(Arrays.asList(ListofFiles));
		String FilesString = Files.toString();
		
		BufferedWriter Writer = new BufferedWriter(new FileWriter("Output.txt"));;
		BufferedWriter logger = new BufferedWriter(new FileWriter("Log.txt"));
		File MakeOutputDirectory = new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output");
		if(!MakeOutputDirectory.exists()) {
			MakeOutputDirectory.mkdir();
		}
		
	   	 BufferedWriter[] outputWriter = 
    		 {
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Zero.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\One.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Two.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Three.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Four.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Five.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Six.txt"))),
    				 new BufferedWriter(new FileWriter(new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output\\Seven.txt")))
    			 };
	   	 
		Tickers.forEach((File, Ticker) -> {
			String FileName = File.toString();
			while(FileName.length() < 10) {
				FileName = "0" + FileName;
			}
			FileName = data.getBasePath() + "CIK" + FileName + ".json";
			
			if(FilesString.contains(FileName)) {
				
				BufferedReader Reader;
				
				try {
					Reader = new BufferedReader(new FileReader(FileName));
					
					String line = Reader.readLine();
					
					if(!line.contains("\"dei\"")) {
						logger.write("[Error]: " + Ticker + " Does not contain needed JSON, DEI\n");
					}
					else {
					JSONObject fileRead = new JSONObject(line);
					JSONObject facts = (JSONObject)fileRead.get("facts");
					JSONObject dei = ((JSONObject)facts.get("dei"));
					if(facts.toString().contains("\"us-gaap\"")) {
					JSONObject financials = (JSONObject)facts.get("us-gaap");
					
					if(!financials.toString().contains("\"Liabilities\"") || !financials.toString().contains("\"CommonStockDividendsPerShareDeclared\"")
							|| !financials.toString().contains("\"EarningsPerShareDiluted\"")){
						 logger.write("[Error]: Missing Needed JSON... " + Ticker + "\n");
					}
					else {
					
				
					double price = data.getCurrentMarketValue(Ticker);
					
					boolean 
					hasAdequateMarketCap = data.hasAdequateMarketCap(Ticker, dei, price), 
					isFinanciallySound = data.isFinanciallySound(financials, Ticker, FileName), 
					hasEarningsStability = data.hasEarningsStability(financials), 
					hasdividendRecord = data.hasdividendRecord(financials), 
					hasEarningsGrowth = data.hasEarningsGrowth(financials), 
					hasModeratePricetoEarningsRatio = data.hasModeratePricetoEarningsRatio(financials, Ticker, price),
					hasModeratePricetoAssets = data.hasModeratePricetoAssets(financials, dei, Ticker);

					double PricetoEarnings = data.getPricetoEarnings(financials, dei, Ticker);
			    	double PricetoBookVal = data.getPricetoBookVal(financials, dei, Ticker);
			    	 
				     if(hasAdequateMarketCap && isFinanciallySound && hasEarningsStability && 
				    		 hasdividendRecord && hasEarningsGrowth && hasModeratePricetoEarningsRatio && hasModeratePricetoAssets) {
							System.out.println("1. hasAdequateMarketCap: " + hasAdequateMarketCap);
							System.out.println("2. isFinanciallySound: " + isFinanciallySound);
							System.out.println("3. hasEarningsStability: " + hasEarningsStability);
							System.out.println("4. dividendRecord: " + hasdividendRecord);
							System.out.println("5. hasEarningsGrowth: " + hasEarningsGrowth);
							System.out.println("6. hasModeratePricetoEarningsRatio: " + hasModeratePricetoEarningsRatio);
							System.out.println("7. hasModeratePricetoAssets: " + hasModeratePricetoAssets);
							logger.write("[Completed] " + Ticker + " has met all criteria\n");
				    	 Writer.write(Ticker + " " + FileName + "\n");
				    	 data.writeResultToFile( 
			    				 outputWriter[7],  Ticker,  hasAdequateMarketCap,  isFinanciallySound,
			    					 hasEarningsStability,  hasdividendRecord,  hasEarningsGrowth, 
			    					 hasModeratePricetoEarningsRatio,  hasModeratePricetoAssets);
			    		 outputWriter[7].write("Price to Earnings: " + PricetoEarnings + 
			    				 " Price to BookValue: " + PricetoBookVal + "Price to earnings x Price to book: " + (PricetoEarnings * PricetoBookVal) + "\n");
				     }
				     else {
				    	 data.writeResultToFile( 
			    				 logger,  Ticker,  hasAdequateMarketCap,  isFinanciallySound,
			    					 hasEarningsStability,  hasdividendRecord,  hasEarningsGrowth, 
			    					 hasModeratePricetoEarningsRatio,  hasModeratePricetoAssets);
				    	 
				 
				    	 int Count = 
				    			 (hasAdequateMarketCap == true ? 1 : 0) + (isFinanciallySound  == true ? 1 : 0)+ 
				    			 (hasEarningsStability == true ? 1 : 0) + (hasdividendRecord == true ? 1 : 0) + 
				    			 (hasEarningsGrowth == true ? 1 : 0) + (hasModeratePricetoEarningsRatio == true ? 1 : 0) + (hasModeratePricetoAssets == true ? 1 : 0);
				    	 
				    	 
				    	
				    	 outputWriter[Count].write("[" + Ticker + "] Current Price per Share: " + price + " Max good value price: " + 15 * data.getAverageEarningsofLast3Years(data.getEarnings(financials)) + "\n");
				    	 data.writeResultToFile( 
			    				 outputWriter[Count],  Ticker,  hasAdequateMarketCap,  isFinanciallySound,
			    					 hasEarningsStability,  hasdividendRecord,  hasEarningsGrowth, 
			    					 hasModeratePricetoEarningsRatio,  hasModeratePricetoAssets);
			    		 outputWriter[Count].write("Bookvalue: " + data.getBookValue(financials) + "Price to Earnings: " + PricetoEarnings + 
			    				 " Price to BookValue: " + PricetoBookVal + "Price to earnings x Price to book: " + (PricetoEarnings * PricetoBookVal) + "\n");
				     }
				     
					}
					}
					}
				} catch (FileNotFoundException e) {
					System.out.println("File not found apparently");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("File not found apparently");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
				
			}
		});
		System.out.println("Completed Running through all available files.");
		Writer.close();
		
		for(int i = 0; i < outputWriter.length; i++) {
			outputWriter[i].close();
		}
		
		long EndTime = System.currentTimeMillis();
		double TimeMinutes = ((double)((EndTime - Starttime)/1000)/60);
		System.out.println( Math.floor(TimeMinutes) + " Minutes and " + Math.round(((TimeMinutes - Math.floor(TimeMinutes)) * 60)) + " Seconds");
	
		
	}
}

/*
	File file = new File("C:\\Users\\dwtun\\eclipse-workspace\\Datafiles\\companyfacts\\CIK0000038777.json");
		BufferedReader Reader = new BufferedReader(new FileReader(file));
		String line = Reader.readLine();
		JSONObject fileRead = new JSONObject(line);
		JSONObject facts = (JSONObject)fileRead.get("facts");
		JSONObject dei = ((JSONObject)facts.get("dei"));
		JSONObject financials = (JSONObject)facts.get("us-gaap");
		
		
		
		System.out.println("1. hasAdequateMarketCap: " + data.hasAdequateMarketCap("BEN", dei));
		System.out.println("2. isFinanciallySound: " + data.isFinanciallySound(financials));
		System.out.println("3. hasEarningsStability: " + data.hasEarningsStability(financials));
		System.out.println("4. dividendRecord: " + data.hasdividendRecord(financials));
		System.out.println("5. hasEarningsGrowth: " + data.hasEarningsGrowth(financials));
		System.out.println("6. hasModeratePricetoEarningsRatio: " + data.hasModeratePricetoEarningsRatio(financials, "BEN"));
		System.out.println("7. hasModeratePricetoAssets: " + data.hasModeratePricetoAssets(financials, dei, "BEN"));
*/