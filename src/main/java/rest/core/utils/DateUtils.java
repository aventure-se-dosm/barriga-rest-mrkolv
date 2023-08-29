package rest.core.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
	
	private final DateTimeFormatter DEFAULT_DATE_FORMAT =  DateTimeFormatter.ofPattern("dd/MM/YYYY");
	
	public String getFormattedDate(LocalDateTime date, DateTimeFormatter formatter) {
		return date.format(formatter);
	} 
	public String getFormattedDate(LocalDateTime date) {
		return getFormattedDate(date, DEFAULT_DATE_FORMAT);
	} 
	
	public LocalDateTime getNowInstant() {
		return LocalDateTime.now();
	}
	
	public LocalDateTime howManyTimeAgo(Long value, ChronoUnit unit){
		return getNowInstant().minus(value, unit);
	}
	public String howMuchTimeAgoString(Long value, ChronoUnit unit){
		return howManyTimeAgoString(value, unit, DEFAULT_DATE_FORMAT);
	}
	public String howManyTimeAgoString(Long value, ChronoUnit unit, DateTimeFormatter formatter){
		return getFormattedDate(howManyTimeAgo(value, unit), formatter);
	}
	public String howMuchTimeAheadString(long value, ChronoUnit unit) {
		return  getFormattedDate(getNowInstant().plus(value, unit));
	}
	

}
