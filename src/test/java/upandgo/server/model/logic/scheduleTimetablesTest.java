package upandgo.server.model.logic;
/**
 * @author kobybs
 * @since 2-1-17
 */

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import upandgo.server.logic.Scheduler;
import upandgo.server.model.loader.CourseLoader;
import upandgo.server.model.loader.XmlCourseLoader;
import upandgo.server.model.schedule.Schedule;
import upandgo.server.model.schedule.Timetable;
import upandgo.shared.entities.Day;
import upandgo.shared.entities.WeekTime;
import upandgo.shared.entities.constraint.TimeConstraint;
import upandgo.shared.entities.course.Course;

@SuppressWarnings({"static-method","deprecation"})
public class scheduleTimetablesTest {

	CourseLoader cr;

	@After
	public void after() {
		System.out.println("***");
	}

	@Test
	public void test_a() {
		cr = new XmlCourseLoader("resources/testXML/schedulerTest.XML");

		final List<Course> courses = new ArrayList<>(cr.loadAllCoursesById().values());
		System.out.println(courses);

		final Schedule s = Scheduler.schedule(courses, new ArrayList<TimeConstraint>());
		System.out.println("Schedule: " + s);

		assert s.getLessonGroups().get(0).getLessons().get(0).getStartTime()
				.equals(new WeekTime(Day.SUNDAY, LocalTime.of(10, 00)));
		assert s.getLessonGroups().get(1).getLessons().get(0).getStartTime()
				.equals(new WeekTime(Day.SUNDAY, LocalTime.of(13, 00)));
		assert s.getLessonGroups().get(2).getLessons().get(0).getStartTime()
				.equals(new WeekTime(Day.TUESDAY, LocalTime.of(11, 00)));
		assert s.getLessonGroups().get(3).getLessons().get(0).getStartTime()
				.equals(new WeekTime(Day.WEDNESDAY, LocalTime.of(14, 00)));

	}

	@Test
	public void test_scheduleTimetable1() {
		cr = new XmlCourseLoader("resources/testXML/schedulerTest.XML");

		final List<Course> courses = new ArrayList<>(cr.loadAllCoursesById().values());
		System.out.println(courses);

		final List<Timetable> tl = Scheduler.getTimetablesList(courses, null);
		System.out.println(tl);

		assert tl.size() == 4;
		for (final Timetable xxx : tl)
			System.out.println("rank: " + xxx.getRankOfDaysoff());
	}

}
