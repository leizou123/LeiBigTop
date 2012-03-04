package com.lei.bigtop.hadoop.movingaverage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class StockData {

	public String exchange;
	public String stock_symbol = "";
	public long date = 0;
	public String open = "";
	public String high = "";
	public String low = "";
	public String close = "";
	public String volume = "";
	public String adj_close = "";

	// public String segment = ""; // lookup

	private static final String DATE_FORMAT = "MM/dd/yy";

	private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

	private static final String DATE_FORMAT_YYMM = "yyyyMM";

	private static SimpleDateFormat sdf_ym = new SimpleDateFormat(
			DATE_FORMAT_YYMM);

	public String getYearMonth() {

		return sdf_ym.format(this.date);

	}

	public String getDate() {

		return sdf.format(this.date);

	}

	public float getClose() {
		// System.out.println( "close: " + this.close );
		return Float.parseFloat(this.close);

	}

	public float getAdjustedClose() {
		// System.out.println( "close: " + this.close );
		return Float.parseFloat(this.adj_close);

	}

	public static StockData parse(String csvRow) {

		StockData rec = new StockData();

		String[] values = csvRow.split(",");

		if (values.length != 9) {
			return null;
		}

		rec.exchange = values[0].trim();
		rec.stock_symbol = values[1].trim();

		String n_Date = values[2].trim();

		if (! ValidateDateFormat.isDateStringValid(n_Date)) {
			return null;
		}
		
		try {
			
			rec.date = sdf.parse(n_Date).getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		rec.open = values[3].trim();
		rec.high = values[4].trim();
		rec.low = values[5].trim();
		rec.close = values[6].trim();
		rec.volume = values[7].trim();
		rec.adj_close = values[8].trim();

		return rec;

	}

}


class ValidateDateFormat  {
	static private String DATE_STRING_PATTERN = "(\\d{2})/(\\d{2})/(\\d{2})";
	static private Pattern datePattern = Pattern.compile(DATE_STRING_PATTERN);
	
	public static boolean isDateStringValid(String strDate) {
		return strDate.matches("(\\d{2})/(\\d{2})/(\\d{2})");
	}
}