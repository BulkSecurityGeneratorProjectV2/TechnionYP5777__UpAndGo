package model.logic;
/**
 * @author kobybs
 * @since 2-1-17
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import logic.Scheduler;
import model.course.Course;
import model.loader.CourseLoader;
import model.loader.XmlCourseLoader;
import model.schedule.Timetable;

@SuppressWarnings("static-method")
public class BlankSpaceRankTests {

CourseLoader cr;
	
	
	@After
	public void after(){
		System.out.println("***");
	}
	
	/*
	@Test
	public void test_a() {
		cr = new XmlCourseLoader("resources/testXML/schedulerTest.XML");
		
		List<Course> courses = new ArrayList<>(cr.loadAllCourses().values());
		System.out.println(courses);
		
		
		List<Timetable> tl = Scheduler.getTimetablesList(courses);
		for(Timetable ¢ : tl)
			System.out.println("rank: " + ¢.getRankOfDaysoff());
		Iterator<Timetable> it = Scheduler.sortedBy(tl, false, false);
		while (it.hasNext()) {
			System.out.println(it.next().getRankOfDaysoff());
		}
		

		//System.out.println(tl);
		
	}
	
	*/
	
	@Test
	public void test_b() {
		cr = new XmlCourseLoader("resources/testXML/schedulerTest9.XML");
		
		List<Course> courses = new ArrayList<>(cr.loadAllCourses().values());
		System.out.println(courses);
		
		
		for (Iterator<Timetable> it = Scheduler.sortedBy(Scheduler.getTimetablesList(courses), true, false); it
				.hasNext();) {
			Timetable currentTable = it.next();
			System.out.println("days of rank: " + currentTable.getRankOfDaysoff());
			System.out.println("blank space rank: " + currentTable.getRankOfBlankSpace());
			System.out.println("time table: " + currentTable);
		}
		
	}
	
	/*@Test
	public void test_a() {
		cr = new XmlCourseLoader("resources/testXML/schedulerTest8.XML");
		
		List<Course> courses = new ArrayList<>(cr.loadAllCourses().values());
		System.out.println(courses);
		
		List<Timetable> tablesList = Scheduler.getTimetablesList(courses);
		Iterator<Timetable> it = Scheduler.sortedBy(tablesList, true, false);
		assert it.next().getRankOfDaysoff() == 4;
		assert it.next().getRankOfDaysoff() == 3;
		
		it = Scheduler.sortedBy(tablesList, false, true);
		assert it.next().getRankOfBlankSpace() == 2.0;
		assert it.next().getRankOfBlankSpace() == 1.75;
		
		
	}*/
	
	
	

}
