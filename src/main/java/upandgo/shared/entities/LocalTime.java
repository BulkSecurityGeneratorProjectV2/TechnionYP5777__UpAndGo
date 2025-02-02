package upandgo.shared.entities;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LocalTime implements Comparable<LocalTime>, IsSerializable {
	private int hour;
	private int minute;
	
	public LocalTime(){
		this.hour = 0;
		this.minute = 0;
	}
	
	private LocalTime(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
	}
	
	/**
	 * Obtains an instance of LocalTime from an hour and minute.
		This returns a LocalTime with the specified hour and minute. The second and nanosecond fields will be set to zero.
		Parameters:
		hour - the hour-of-day to represent, from 0 to 23
		minute - the minute-of-hour to represent, from 0 to 59
		Returns:
		the local time, not null
		Throws:
		DateTimeException - if the value of any field is out of range
	 */
	public static LocalTime of(int hour, int minute){
		return new LocalTime(hour, minute);
	}

	@Override
	public int compareTo(LocalTime t) {
		return this.hour != t.hour ? this.hour - t.hour : this.minute - t.minute;
	}
	
	public int getHour(){
		return hour;
	}
	
	public int getMinute(){
		return minute;
	}
	
	@Override
	public String toString(){
		return hour + ":" + (minute < 10 ? "0" : "") + minute;
	}

	public static LocalTime parse(String s) {
		String[] parts = s.split(":");
		return new LocalTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	}
}
