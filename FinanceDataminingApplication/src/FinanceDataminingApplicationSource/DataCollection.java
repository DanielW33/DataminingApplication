package FinanceDataminingApplicationSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.*;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

public class DataCollection {
	private Map<Integer, String> Tickers;
	private String BasePath = "C:\\Users\\dwtun\\eclipse-workspace\\Datafiles\\companyfacts\\";
	private int currentYear = 0;
	
	public DataCollection() throws InterruptedException, IOException {
		try {
			Tickers = new TreeMap<>();
			Date date = new Date();
			currentYear = Calendar.getInstance().get(Calendar.YEAR);
			MapTickers();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

	/*
	 *  1. Adequate Size of Enterprise
	 */
	public boolean hasAdequateMarketCap(String ticker, JSONObject dei, double price) throws IOException {
		double marketcap = getMarketCap(ticker, dei, price);
		double Min = 2000000000.00;
		if(marketcap < Min) {
			return false;
		}
		return true;
	}
	/*
	 * 2. A Sufficiently Strong Financial Condition
	 * 2 to 1 asset to liability value
	 */
	public boolean isFinanciallySound(JSONObject financials, String ticker, String FilePath) {
		long Assets = mostRecentAssets(financials), Liabilites = mostRecentLiabilities(financials);

		if(Assets >= Liabilites * 2) {
			return true;
		}
		else return false;
	}
	/*
	 * 3. Earnings Stability
	 */
	public boolean hasEarningsStability(JSONObject financials) {

		JSONArray EarningsArr = getEarnings(financials);
		
		int EarningYearCount = CountWholeYearsinArray(EarningsArr);
		
		if(EarningYearCount < 10) return false;
		
	
		if(currentYear - EarningsArr.getJSONObject(0).getInt("fy") >= 10){
			for(int i = 0; i < EarningsArr.length(); i++) {
				JSONObject Earnings = EarningsArr.getJSONObject(i);
			
				if(!Earnings.toString().contains("\"fp\":null")) {
				if(Earnings.getString("fp").equals("FY")) {
					double EarningVal = Earnings.getBigDecimal("val").doubleValue();
					if(EarningVal <= 0) {
						return false;
					}
					
				}}
			}
		}
	
		return true;
	}

	
	/*
	 * 4. Dividend Yield
	 * Min 10 year dividend record 20 year preferred. 
	 */
	public boolean hasdividendRecord(JSONObject financials) {

		JSONArray DividendArr = getDividends(financials);
		int DividendYearCount = CountWholeYearsinArray(DividendArr);
		
		if(DividendYearCount < 11) return false;
		
		int minYear = currentYear - 11;
		
		List<Integer> Years = new ArrayList<>();
		for(int i = currentYear - 1; i >= minYear; i--) {
			Years.add(i);
		}
		
		for(int i = 0; i < DividendArr.length(); i++) {
			JSONObject obj = (JSONObject) DividendArr.get(i);
			if(!obj.toString().contains("\"fy\":null")) {
				if(obj.getInt("fy") < currentYear && Years.contains(obj.getInt("fy"))){
					Years.remove((Integer)obj.getInt("fy"));
				}
		}
		}
			if(Years.size() == 0) {
				return true;
			}
	
		return false;
	}
	
	/*
	 * 5. Earnings growth
	 * A minimum increase of at least one third in per share value over the past 10 years using the average of the first and last 3 years
	 */
	public boolean hasEarningsGrowth(JSONObject financials) {
		JSONArray EarningsArr = getEarnings(financials);
		int EarningYearCount = CountWholeYearsinArray(EarningsArr);
		
		if(EarningYearCount < 10) return false;
		
		double averageLast3 = getAverageEarningsofLast3Years( EarningsArr);
		double averageFirst3 = getAverageEarningsofFirst3Years(EarningsArr, currentYear - 11);
		
		double thirdoffirst3 = averageFirst3 * .33333;
		//////System.out.println("Average last 3: " + averageLast3 + " Average First 3: " + averageFirst3 + " OneThirdoffirst3: " + thirdoffirst3);
		if(averageLast3 >= averageFirst3 + thirdoffirst3) {
			return true;
		}
		else return false;
	}
	/*
	 * 6. ModeratePricetoEarnings
	 * Current price should not be more than 15 times average earnings of the past 3 years
	 */
	public boolean hasModeratePricetoEarningsRatio(JSONObject financials, String ticker, double price) throws IOException {
		JSONArray EarningsArr = getEarnings(financials);
		
		int EarningYearCount = CountWholeYearsinArray(EarningsArr);
		
		if(EarningYearCount < 5) return false;
		
		double averageLast3 = getAverageEarningsofLast3Years(EarningsArr);
		double MarketVal = price;

		//////System.out.println("averageFirst3: " + averageFirst3 + " MarketVal: " + MarketVal + " MaxfairValue: " + (averageFirst3 * 15));
		if(averageLast3 * 15 >= MarketVal) {
			return true;
		}
		else return false;
	}
	/*
	 * 7. Moderate price to assets
	 */
	public boolean hasModeratePricetoAssets(JSONObject financials, JSONObject dei, String ticker) throws IOException {
		double bookValPerShare = getBookvaluePerShare(financials, dei);
		double price = getCurrentMarketValue(ticker);
		JSONArray EarningsArr = getEarnings(financials);

		int EarningYearCount = CountWholeYearsinArray(EarningsArr);
		
		if(EarningYearCount < 5) return false;
		
		double averageLast3 = getAverageEarningsofLast3Years(EarningsArr);
		
		double PricetoEarnings = (price / averageLast3);
		double PricetoBookVal = price / bookValPerShare;
		double PricetoEarningsxPricetoBookVal = PricetoEarnings * PricetoBookVal;
		if(PricetoEarningsxPricetoBookVal >= 0 && PricetoEarningsxPricetoBookVal <= 22.5) {
			return true;
		}
		else return false;
	}
	public double getPricetoBookVal(JSONObject financials, JSONObject dei, String ticker) {
		double bookValPerShare = getBookvaluePerShare(financials, dei);
		double price = getCurrentMarketValue(ticker);
		double CalculatedPrice;
		JSONArray EarningsArr = getEarnings(financials);

		int EarningYearCount = CountWholeYearsinArray(EarningsArr);
		
		if(EarningYearCount < 5) return -1;
				
		CalculatedPrice = price / bookValPerShare;
		
		return CalculatedPrice;
	}
	
	public double getPricetoEarnings(JSONObject financials, JSONObject dei, String ticker) {
		double price = getCurrentMarketValue(ticker);
		JSONArray EarningsArr = getEarnings(financials);
		int EarningYearCount = CountWholeYearsinArray(EarningsArr);
		double productOfMultiplier;
		double averageLast3;
		
		if(EarningYearCount < 5) return -1;
		
		averageLast3 = getAverageEarningsofLast3Years(EarningsArr);
		
		productOfMultiplier = (price / averageLast3);
		return productOfMultiplier;
	}
	
	public double getMarketCap(String ticker, JSONObject dei, double price) throws IOException {
		if(dei.toString().contains("\"EntityCommonStockSharesOutstanding\"")) {
		JSONObject EntityCommonStockSharesOutstanding = dei.getJSONObject("EntityCommonStockSharesOutstanding");
		JSONObject shares =  ((JSONObject)(EntityCommonStockSharesOutstanding).getJSONObject("units"));
		JSONArray SharesOutstandingArr = shares.getJSONArray("shares");
		
		JSONObject MostRecent = ((JSONObject) SharesOutstandingArr.getJSONObject(SharesOutstandingArr.length() - 1));
		double marketCap = MostRecent.getInt("fy") == currentYear ? MostRecent.getInt("val") : 0;
			marketCap =  (marketCap * price);
		return marketCap;
		}
		return -1;
	}
	
	public int CountWholeYearsinArray(JSONArray Array) {
		int counter = 0;
		if(Array == null) return -1;
		
		for(int i = 0; i < Array.length(); i++) {
			JSONObject EObject = Array.getJSONObject(i);
			if(EObject.toString().contains("frame")) {
				String frame = EObject.getString("frame");
				if(frame.matches("CY[0-9][0-9][0-9][0-9]")) {
					counter++;
				}
			}
		}
		return counter;
	}
	/*
	 * Getting the average of 3 years after the start year
	 */
	private double getAverageEarningsofFirst3Years(JSONArray EarningsArr, int StartYear) {
		double[] first3Years = new double[3]; 
		List<Integer> first = new ArrayList<>();
		int counter = 0;
		
		first.add(StartYear); first.add(StartYear + 1); first.add(StartYear + 2); 
		
		for(int i = 0; i < EarningsArr.length(); i++) {
			JSONObject Earnings = EarningsArr.getJSONObject(i);
			if(Earnings.getString("fp").equals("FY") && Earnings.toString().contains("frame")) {
				for(int j = 0; j < first.size(); j++) {
					if(Earnings.getString("frame").equals("CY" + first.get(j))) {
						
						first.remove(first.get(j));
						first3Years[counter] = Earnings.getDouble("val");
						counter++;
						break;
					}
				}
				if(counter == 3) break;
			}
			
		}
		return (first3Years[0] + first3Years[1] + first3Years[2]) / 3;
	}
	/*
	 * Getting the average of the 3 most recent full years
	 */
	public double getAverageEarningsofLast3Years(JSONArray EarningsArr) {
		int counter = 0;
		int EndYears = currentYear  - 3;
		double[] last3Years = new double[3];
		double averageLast3;
		List<Integer> last = new ArrayList<>();
		last.add(EndYears); last.add(EndYears + 1); last.add(EndYears + 2);	
		
		if(EarningsArr == null) return -1;
		
		for(int i = EarningsArr.length() - 1; i > 0; i--) {
			JSONObject Earnings = EarningsArr.getJSONObject(i);
			if(Earnings.toString().contains("\"fy\":null") || Earnings.toString().contains("\"fp\":null")) {
				return -1;
			}
			if(Earnings.getString("fp").equals("FY") && Earnings.toString().contains("frame")) {
				for(int j = 0; j < last.size(); j++) {
					if(Earnings.getString("frame").equals("CY" + last.get(j))) {

						last.remove(last.get(j));
						last3Years[counter] = Earnings.getDouble("val");
						counter++;
						break;
						
					}
				}
				if(counter == 3) break;
			}
			
		}
		averageLast3 = (last3Years[0] + last3Years[1] + last3Years[2]) / 3;

		return averageLast3;
	}
	/*
	 * Book value per share is book value / number of shares outstanding
	 */
	private double getBookvaluePerShare(JSONObject financials, JSONObject dei) {
		double bookVal = getBookValue(financials);
		int stockOutstanding = mostRecentStockOutstanding(dei);
		double BookvalperShare = bookVal/stockOutstanding;
		
		return BookvalperShare;
	}
	/*
	 * Book value is assets minus liabilities
	 */
	public double getBookValue(JSONObject financials) {
		return mostRecentAssets(financials) - mostRecentLiabilities(financials);
	}
	/*
	 * Assets from the last available record
	 */
	private long mostRecentAssets(JSONObject financials) {
		JSONArray AssetArr = getAssets(financials);
		
		if(AssetArr != null) {
		JSONObject MostRecent = AssetArr.getJSONObject(AssetArr.length() - 1);
		
		return MostRecent.getLong("val");
		}
		return -1;
	}
	/*
	 * Liabilities from the last available record
	 */
	private long mostRecentLiabilities(JSONObject financials) {
		JSONArray LiabilitiesArr = getLiabilities(financials);
		if(LiabilitiesArr != null) {
			JSONObject MostRecent =LiabilitiesArr.getJSONObject(LiabilitiesArr.length() - 1);
			
			return MostRecent.getLong("val");
		}
		return -1;
	}
	/*
	 * Most recent stock outstanding
	 */
	private int mostRecentStockOutstanding(JSONObject dei) {
		JSONArray Shares = getStockOutstanding(dei);
		if(Shares != null) {
		JSONObject mostRecent = Shares.getJSONObject(Shares.length() - 1);
		
		return mostRecent.getInt("val");
		}
		return -1;
	}
	/*
	 * All Stock outstanding record
	 */
	private JSONArray getStockOutstanding(JSONObject dei) {
		if(dei.toString().contains("\"EntityCommonStockSharesOutstanding\"")) {
		JSONObject EntityCommonStockSharesOutstanding = dei.getJSONObject("EntityCommonStockSharesOutstanding");
		JSONObject shares =  ((JSONObject)(EntityCommonStockSharesOutstanding).getJSONObject("units"));
		return shares.getJSONArray("shares");
		}
		return null;
		
	}
	/*
	 * All dividends record
	 */
	private JSONArray getDividends(JSONObject financials) {
		JSONObject CommonStockDividendsPerShareDeclared = (JSONObject)financials.get("CommonStockDividendsPerShareDeclared");
		if(((JSONObject)CommonStockDividendsPerShareDeclared.get("units")).toString().contains("\"USD/shares\"")) {
			return ((JSONObject)CommonStockDividendsPerShareDeclared.get("units")).getJSONArray("USD/shares");
		}
		return null;
	}
	/*
	 * All earnings record
	 */
	public JSONArray getEarnings(JSONObject financials) {
		JSONObject EarningsPerShare = ((JSONObject)financials.get("EarningsPerShareDiluted"));
		if(((JSONObject)EarningsPerShare.get("units")).toString().contains("\"USD/shares\"")) {
		return ((JSONObject)EarningsPerShare.get("units")).getJSONArray("USD/shares");
		}
		return null;
	}
	/*
	 * All assets record
	 */
	private JSONArray getAssets(JSONObject financials) {
		//Getting Asset Information
		JSONObject Assets = (JSONObject)financials.get("Assets"); 
		if(((JSONObject)Assets.get("units")).toString().contains("\"USD\"")) {
		return ((JSONObject)Assets.get("units")).getJSONArray("USD");
		}
		return null;
	}
	/*
	 * All liabilities record
	 */
	private JSONArray getLiabilities(JSONObject financials) {
		
		//Getting Liability Information
		JSONObject Liabilities = (JSONObject)financials.get("Liabilities"); 
		if(((JSONObject)Liabilities.get("units")).toString().contains("\"USD\"")) {
			return ((JSONObject)Liabilities.get("units")).getJSONArray("USD");
		}
		return null;
		
	}
	/*
	 * Current Market value of one share
	 */
	public double getCurrentMarketValue(String ticker) {
		//System.out.println(ticker);
		if(ticker == null) {
			return -1;
		}
		Stock stock = null;
		try {
			stock = YahooFinance.get(ticker);
		} catch (IOException e) {
			return -1;
		}
		if(stock == null) {
			return -1;
		}
		StockQuote quote = stock.getQuote();
		if(quote == null) {
			return -1;
		}
		BigDecimal bValue = quote.getPrice();
		if(bValue == null) {
			return -1;
		}
		double value = bValue.doubleValue();
		//System.out.println(value);
		return  value;
	}
	/*
	 * Current dividend yield for one share per year.
	 */
	private double getDividendYield(String ticker) throws IOException {
		return YahooFinance.get(ticker).getDividend().getAnnualYield().doubleValue();
	}
	
	private void MapTickers() throws NumberFormatException, IOException {
		
		BufferedReader reader = getTickerReader();
		
		String line = "";
		while((line = reader.readLine()) != null) {

			line = line.replace("\t", " ");
			line = line.replace("\\s", " ");
			line = line.trim();
			
			String[] TickerCIK = line.split(" ");
			
			Tickers.put(Integer.valueOf(TickerCIK[1]), TickerCIK[0]);
			
		}
	
	
		reader.close();
	}
	
	/*
	 * Getting the mapping between ticker and CIK
	 */
	private BufferedReader getTickerReader()  {
	
		URL url;
		try {
			url = new URL("https://www.sec.gov/include/ticker.txt");
			URLConnection connection =  url.openConnection();
			connection.setConnectTimeout(5000); connection.setReadTimeout(5000); 

			return new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
		} catch (IOException e) {
			////System.out.println("[Warning]: Failed to connect to https://www.sec.gov/include/ticker.txt. Using tickers on file.");
			File file = new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\ticker.txt");
			try {
				return new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e1) {
				
				e1.printStackTrace();
				return null;
			}
		} 
	}
	
	public void print() {
		
		Tickers.forEach((k,v) ->{
			////System.out.println("Key: " + k + " Value: " + v);
		});
	}
	
	public void toFile() throws IOException {
		File file = new File("C:\\Users\\dwtun\\eclipse-workspace\\FinanceDataminingApplication\\Output.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		Tickers.forEach((k,v) ->{
			try {
				writer.write("Key: " + k + " Value: " + v + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		writer.close();
	}

	public String getBasePath() {
		return BasePath;
	}
	public void writeResultToFile(BufferedWriter writer, String Ticker, boolean hasAdequateMarketCap, boolean isFinanciallySound,
			boolean hasEarningsStability, boolean hasdividendRecord, boolean hasEarningsGrowth, boolean hasModeratePricetoEarningsRatio, boolean hasModeratePricetoAssets) throws IOException {
		
		writer.write("------------------------------\n");
		writer.write("[Completed] Does not meet Criteria: " + Ticker + "\n");
		writer.write("[" + Ticker + " Output]:\n");
		writer.write("1. hasAdequateMarketCap: " + hasAdequateMarketCap + "\n");
		writer.write("2. isFinanciallySound: " + isFinanciallySound + "\n");
		writer.write("3. hasEarningsStability: " + hasEarningsStability + "\n");
		writer.write("4. dividendRecord: " + hasdividendRecord + "\n");
		writer.write("5. hasEarningsGrowth: " + hasEarningsGrowth + "\n");
		writer.write("6. hasModeratePricetoEarningsRatio: " + hasModeratePricetoEarningsRatio + "\n");
		writer.write("7. hasModeratePricetoAssets: " + hasModeratePricetoAssets + "\n");
		writer.write("------------------------------\n");
		
	}

	public void setBasePath(String basePath) {
		BasePath = basePath;
	}
	public Map<Integer, String> getTickers(){
		return Tickers;
	}
}
