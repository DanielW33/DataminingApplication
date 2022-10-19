package FinanceDataminingApplicationSource;

public class StockData {
	private boolean adequateSizeofEnterprise;
	private int SizeofEnterprise;
	private String SizeofEnterpriseDescription;
	
	private boolean strongFinancials;
	private double FinancialsRatio;
	private String FinancialsRatioDescription;
	
	private boolean earningsStability;
	private double EarningsOverDecade;
	private String earningsStabilityDescription;
	
	private boolean dividendRecord;
	private int DividendRecordAvailable;
	private String dividendRecordDescription;
	
	private boolean earningsGrowth;
	private double EarningsAvgDifference;
	private String EarningsAvgDifferenceDescription;
	
	private boolean ModeratePriceToEarnings;
	private int PriceToEarningsRatio;
	private String PriceToEarningsRatioDescription;
	
	private boolean ModeratePricetoAssets;
	private int PricetoAssetsRatio;
	private String PricetoAssetsRatioDescription;
	
	@Override
	public String toString() {
		return "StockData [adequateSizeofEnterprise=" + adequateSizeofEnterprise + ", dividendRecord=" + dividendRecord
				+ ", DividendRecordAvailable=" + DividendRecordAvailable + ", dividendRecordDescription="
				+ dividendRecordDescription + ", EarningsAvgDifference=" + EarningsAvgDifference
				+ ", EarningsAvgDifferenceDescription=" + EarningsAvgDifferenceDescription + ", earningsGrowth="
				+ earningsGrowth + ", EarningsOverDecade=" + EarningsOverDecade + ", earningsStability="
				+ earningsStability + ", earningsStabilityDescription=" + earningsStabilityDescription
				+ ", FinancialsRatio=" + FinancialsRatio + ", FinancialsRatioDescription=" + FinancialsRatioDescription
				+ ", ModeratePricetoAssets=" + ModeratePricetoAssets + ", ModeratePriceToEarnings="
				+ ModeratePriceToEarnings + ", PricetoAssetsRatio=" + PricetoAssetsRatio
				+ ", PricetoAssetsRatioDescription=" + PricetoAssetsRatioDescription + ", PriceToEarningsRatio="
				+ PriceToEarningsRatio + ", PriceToEarningsRatioDescription=" + PriceToEarningsRatioDescription
				+ ", SizeofEnterprise=" + SizeofEnterprise + ", SizeofEnterpriseDescription="
				+ SizeofEnterpriseDescription + ", strongFinancials=" + strongFinancials + "]";
	}
	
	public boolean isAdequateSizeofEnterprise() {
		return adequateSizeofEnterprise;
	}
	public void setAdequateSizeofEnterprise(boolean adequateSizeofEnterprise) {
		this.adequateSizeofEnterprise = adequateSizeofEnterprise;
	}
	public int getSizeofEnterprise() {
		return SizeofEnterprise;
	}
	public void setSizeofEnterprise(int sizeofEnterprise) {
		SizeofEnterprise = sizeofEnterprise;
	}

	public String getSizeofEnterpriseDescription() {
		return SizeofEnterpriseDescription;
	}

	public void setSizeofEnterpriseDescription(String sizeofEnterpriseDescription) {
		SizeofEnterpriseDescription = sizeofEnterpriseDescription;
	}

	public boolean isStrongFinancials() {
		return strongFinancials;
	}

	public void setStrongFinancials(boolean strongFinancials) {
		this.strongFinancials = strongFinancials;
	}

	public double getFinancialsRatio() {
		return FinancialsRatio;
	}

	public void setFinancialsRatio(double financialsRatio) {
		FinancialsRatio = financialsRatio;
	}

	public String getFinancialsRatioDescription() {
		return FinancialsRatioDescription;
	}

	public void setFinancialsRatioDescription(String financialsRatioDescription) {
		FinancialsRatioDescription = financialsRatioDescription;
	}

	public boolean isEarningsStability() {
		return earningsStability;
	}

	public void setEarningsStability(boolean earningsStability) {
		this.earningsStability = earningsStability;
	}

	public double getEarningsOverDecade() {
		return EarningsOverDecade;
	}

	public void setEarningsOverDecade(double earningsOverDecade) {
		EarningsOverDecade = earningsOverDecade;
	}

	public String getEarningsStabilityDescription() {
		return earningsStabilityDescription;
	}

	public void setEarningsStabilityDescription(String earningsStabilityDescription) {
		this.earningsStabilityDescription = earningsStabilityDescription;
	}

	public boolean isDividendRecord() {
		return dividendRecord;
	}

	public void setDividendRecord(boolean dividendRecord) {
		this.dividendRecord = dividendRecord;
	}

	public int getDividendRecordAvailable() {
		return DividendRecordAvailable;
	}

	public void setDividendRecordAvailable(int dividendRecordAvailable) {
		DividendRecordAvailable = dividendRecordAvailable;
	}

	public String getDividendRecordDescription() {
		return dividendRecordDescription;
	}

	public void setDividendRecordDescription(String dividendRecordDescription) {
		this.dividendRecordDescription = dividendRecordDescription;
	}

	public boolean isEarningsGrowth() {
		return earningsGrowth;
	}

	public void setEarningsGrowth(boolean earningsGrowth) {
		this.earningsGrowth = earningsGrowth;
	}

	public double getEarningsAvgDifference() {
		return EarningsAvgDifference;
	}

	public void setEarningsAvgDifference(double earningsAvgDifference) {
		EarningsAvgDifference = earningsAvgDifference;
	}

	public String getEarningsAvgDifferenceDescription() {
		return EarningsAvgDifferenceDescription;
	}

	public void setEarningsAvgDifferenceDescription(String earningsAvgDifferenceDescription) {
		EarningsAvgDifferenceDescription = earningsAvgDifferenceDescription;
	}

	public boolean isModeratePriceToEarnings() {
		return ModeratePriceToEarnings;
	}

	public void setModeratePriceToEarnings(boolean moderatePriceToEarnings) {
		ModeratePriceToEarnings = moderatePriceToEarnings;
	}

	public int getPriceToEarningsRatio() {
		return PriceToEarningsRatio;
	}

	public void setPriceToEarningsRatio(int priceToEarningsRatio) {
		PriceToEarningsRatio = priceToEarningsRatio;
	}

	public String getPriceToEarningsRatioDescription() {
		return PriceToEarningsRatioDescription;
	}

	public void setPriceToEarningsRatioDescription(String priceToEarningsRatioDescription) {
		PriceToEarningsRatioDescription = priceToEarningsRatioDescription;
	}

	public boolean isModeratePricetoAssets() {
		return ModeratePricetoAssets;
	}

	public void setModeratePricetoAssets(boolean moderatePricetoAssets) {
		ModeratePricetoAssets = moderatePricetoAssets;
	}

	public int getPricetoAssetsRatio() {
		return PricetoAssetsRatio;
	}

	public void setPricetoAssetsRatio(int pricetoAssetsRatio) {
		PricetoAssetsRatio = pricetoAssetsRatio;
	}

	public String getPricetoAssetsRatioDescription() {
		return PricetoAssetsRatioDescription;
	}

	public void setPricetoAssetsRatioDescription(String pricetoAssetsRatioDescription) {
		PricetoAssetsRatioDescription = pricetoAssetsRatioDescription;
	}
	
}
