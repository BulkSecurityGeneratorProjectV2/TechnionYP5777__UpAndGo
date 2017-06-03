package upandgo.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;

import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.inject.Inject;

import upandgo.client.CoursesServiceAsync;
import upandgo.client.event.SelectCourseEvent;
import upandgo.client.event.UnselectCourseEvent;
import com.allen_sauer.gwt.log.client.Log;

import upandgo.shared.entities.course.Course;
import upandgo.shared.entities.course.CourseId;

/**
 * 
 * @author Nikita Dizhur
 * @since 06-04-17
 * 
 *        A concrete presenter for {@link CourseListView}.
 * 
 */

// TODO: add History management

public class CourseListPresenter implements Presenter {

	public interface Display {
		CellTable<CourseId> getSelectedCoursesList();

		CellTable<CourseId> getNotSelectedCoursesList();

		HasChangeHandlers getFacultyDropList();

		HasKeyUpHandlers getCourseSearch();

		void setSelectedCourses(List<CourseId> courses);

		void setNotSelectedCourses(List<CourseId> courses);

		void setFaculties(List<String> faculties);
		
		void setHoveredRow(int row);
		
		void setHoveredCourseDetail(String detail);

		CourseId getSelectedCourse(int row);

		CourseId getUnselectedCourse(int row);

		int getHoveredSelectedCourseRow(CellPreviewEvent<CourseId> event); // pass -1 if none

		int getHoveredNotSelectedCourseRow(CellPreviewEvent<CourseId> event); // pass -1 if none

		int getSelectedFacultyRow(ChangeEvent event); // pass -1 if there is no selectedFaculty chosen

		String getCourseQuery(KeyUpEvent event);

		Widget getExamsBar();
		
		Widget getAsWidget();
	}

	@Inject
	public CourseListPresenter(CoursesServiceAsync rpc, EventBus eventBus, Display display) {
		this.rpcService = rpc;
		this.display = display;
		this.eventBus = eventBus;
	}

	CoursesServiceAsync rpcService;
	Display display;
	EventBus eventBus;
	
	List<CourseId> selectedCourses;
	List<CourseId> notSelectedCourses;
	List<String> faculties;
	String courseQuery = "";

	String selectedFaculty = "";
	CourseId hoveredCourse = null;
	int hoveredRow = -1;
	
	int selectedClickedRow = -1;
	int unselectedClickedRow = -1;


	@Override
	public void bind() {

		// define faculty list functionality
		display.getFacultyDropList().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int $ = display.getSelectedFacultyRow(event);
				if ($ < 0)
					return;
				if($ == 0)	// if it's "all faculties" option
					selectedFaculty = "";
				else
					selectedFaculty = faculties.get($);

				rpcService.getNotSelectedCourses(courseQuery, selectedFaculty,
						new FetchNotSelectedCoursesAsyncCallback());

			}
		});

		// define selected courses list functionality
		display.getSelectedCoursesList().addDomHandler(new DoubleClickHandler() {

	        @Override
	        public void onDoubleClick(@SuppressWarnings("unused") final DoubleClickEvent event) {
				final CourseId $ = display.getSelectedCourse(selectedClickedRow);
				if ($ != null) {
					rpcService.unselectCourse($, new AsyncCallback<Void>() {
						@Override
						public void onFailure(@SuppressWarnings("unused") Throwable caught) {
							Window.alert("Error while unselecting course.");
							Log.error("Error while unselecting course.");
						}

						@Override
						public void onSuccess(@SuppressWarnings("unused") Void result) {
							rpcService.getSelectedCourses(new FetchSelectedCoursesAsyncCallback());
							Log.info("444444444444444444444444444444444444444444444444");
							rpcService.getNotSelectedCourses(courseQuery, selectedFaculty,
									new FetchNotSelectedCoursesAsyncCallback());

							eventBus.fireEvent(new UnselectCourseEvent($));
						}
					});
				}
	        }
	    }, DoubleClickEvent.getType());
		
		display.getSelectedCoursesList().addCellPreviewHandler(new CellPreviewEvent.Handler<CourseId>() {

			@Override
			public void onCellPreview(CellPreviewEvent<CourseId> event) {
				boolean isMouseOver = BrowserEvents.MOUSEOVER.equals(event.getNativeEvent().getType());
				boolean isMouseOut = BrowserEvents.MOUSEOUT.equals(event.getNativeEvent().getType());
				boolean isClick = BrowserEvents.CLICK.equals(event.getNativeEvent().getType());
				
				if (isMouseOver) {
					hoveredRow = display.getHoveredSelectedCourseRow(event);
					if (hoveredRow < 0) {
						return;
					}

					CourseId newCourseId = selectedCourses.get(hoveredRow);
					if(!newCourseId.equals(hoveredCourse)) {
						hoveredCourse = newCourseId;
						Log.info("1hoveres is: " + hoveredCourse.getTitle());
						rpcService.getCourseDetails(hoveredCourse, new GetCourseDetailsCallback());
					}
				}

				if (isMouseOut) {
					hoveredCourse = null;
					hoveredRow = -1;
					display.setHoveredRow(-1);
					display.setHoveredCourseDetail("");
				}
				if(isClick){
					selectedClickedRow = event.getIndex();
					event.setCanceled(true);
				}
			}
		});

		// define not selected courses list functionality
		display.getNotSelectedCoursesList().addDomHandler(new DoubleClickHandler() {

	        @Override
	        public void onDoubleClick(@SuppressWarnings("unused") final DoubleClickEvent event) {
				final CourseId $ = display.getUnselectedCourse(unselectedClickedRow);
				if ($ != null) {
					rpcService.selectCourse($, new AsyncCallback<Void>() {
						@Override
						public void onFailure(@SuppressWarnings("unused") Throwable caught) {
							Window.alert("Error while selecting course.");
							Log.error("Error while selecting course.");
						}

						@Override
						public void onSuccess(@SuppressWarnings("unused") Void result) {
							rpcService.getSelectedCourses(new FetchSelectedCoursesAsyncCallback());
							Log.info("333333333333333333333333333333333333333333333");
							rpcService.getNotSelectedCourses(courseQuery, selectedFaculty,
									new FetchNotSelectedCoursesAsyncCallback());

							eventBus.fireEvent(new SelectCourseEvent($));
						}
					});
				}
	        }
	    }, DoubleClickEvent.getType());
		
		
		display.getNotSelectedCoursesList().addCellPreviewHandler(new CellPreviewEvent.Handler<CourseId>() {

			@Override
			public void onCellPreview(CellPreviewEvent<CourseId> event) {
				boolean isMouseOver = BrowserEvents.MOUSEOVER.equals(event.getNativeEvent().getType());
				boolean isMouseOut = BrowserEvents.MOUSEOUT.equals(event.getNativeEvent().getType());
				boolean isClick = BrowserEvents.CLICK.equals(event.getNativeEvent().getType());

				if (isMouseOver) {
					hoveredRow = display.getHoveredNotSelectedCourseRow(event);
					if (hoveredRow < 0) {
						return;
					}

					CourseId newCourseId = notSelectedCourses.get(hoveredRow);
					if(!newCourseId.equals(hoveredCourse)) {
						hoveredCourse = newCourseId;
						Log.info("2hoveres is: " + hoveredCourse.getTitle());
						rpcService.getCourseDetails(hoveredCourse, new GetCourseDetailsCallback());
					}
				}

				if (isMouseOut) {
					hoveredCourse = null;
					hoveredRow = -1;
					display.setHoveredRow(-1);
					display.setHoveredCourseDetail("");
				}
				if(isClick){
					unselectedClickedRow = event.getIndex();
					event.setCanceled(true);
				}

			}
		});

		display.getCourseSearch().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				courseQuery = display.getCourseQuery(event);
				Log.info("22222222222222222222222222222222222222222");
				rpcService.getNotSelectedCourses(courseQuery, selectedFaculty,
						new FetchNotSelectedCoursesAsyncCallback());
			}
		});
		
	}

	@Override
	public void unbind() {
		// TODO
	}

	@Override
	public void go(LayoutPanel panel) {
		bind();

		//panel.clear();
		panel.add(display.getAsWidget());
		panel.setWidgetRightWidth(display.getAsWidget(), 1, Unit.EM, 20, Unit.PCT);
		panel.setWidgetTopBottom(display.getAsWidget(), 4.5, Unit.EM, 1, Unit.EM);
		
//		rpcService.getSomeString(new GetSomeStringAsyncCallback());
		
		Widget examsBar= display.getExamsBar();
		panel.add(examsBar);
		panel.setWidgetLeftWidth(examsBar, 1, Unit.EM, 77, Unit.PCT);
		panel.setWidgetTopBottom(examsBar, 90, Unit.PCT, 1, Unit.EM);
		
		rpcService.getFaculties(new FetchFacultiesAsyncCallback());
		rpcService.getSelectedCourses(new FetchSelectedCoursesAsyncCallback());
		Log.info("11111111111111111111111111111111111111111111");
		rpcService.getNotSelectedCourses(courseQuery, selectedFaculty, new FetchNotSelectedCoursesAsyncCallback());
	}

	class FetchSelectedCoursesAsyncCallback implements AsyncCallback<ArrayList<CourseId>> {
		@Override
		public void onSuccess(ArrayList<CourseId> result) {
			selectedCourses = result;
			display.setSelectedCourses(selectedCourses);
		}

		@Override
		public void onFailure(@SuppressWarnings("unused") Throwable caught) {
			Window.alert("Error fetching selected courses.");
			Log.error("Error fetching selected courses.");
		}
	}

	class FetchNotSelectedCoursesAsyncCallback implements AsyncCallback<ArrayList<CourseId>> {
		@Override
		public void onSuccess(ArrayList<CourseId> result) {
			notSelectedCourses = result;
			display.setNotSelectedCourses(notSelectedCourses);
		}

		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("FetchNotSelectedCoursesAsyncCallback got: " + caught.getLocalizedMessage()+"*"+caught.getMessage()+"&&"+
								(caught.getCause() != null ? caught.getCause().getLocalizedMessage() : "")+"*"+
								(caught.getCause() != null ? caught.getCause().getMessage(): "*")+"end");
			Window.alert("Error fetching not selected courses.");
			Log.error("Error fetching not selected courses.");
		}
	}

	class FetchFacultiesAsyncCallback implements AsyncCallback<ArrayList<String>> {
		@Override
		public void onSuccess(ArrayList<String> result) {
			faculties = result;
			display.setFaculties(faculties);
		}

		@Override
		public void onFailure(@SuppressWarnings("unused") Throwable caught) {
			Window.alert("Error fetching faculties.");
			Log.error("Error fetching faculties.");
			
		}
	}
	
	class GetCourseDetailsCallback implements AsyncCallback<Course> {
		@Override
		public void onFailure(@SuppressWarnings("unused") Throwable caught) {
			Window.alert("Error while getting information about course.");
			Log.error("Error while getting information about selecting course.");
		}

		@Override
		public void onSuccess(Course result) {
			display.setHoveredRow(hoveredRow);
			display.setHoveredCourseDetail(result.toString());
		}
	}
	
	class GetSomeStringAsyncCallback implements AsyncCallback<String> {
		@Override
		public void onSuccess(String result) {
			//Window.alert(result);
			Log.info(result);
		}

		@Override
		public void onFailure(@SuppressWarnings("unused") Throwable caught) {
			Window.alert("Cthulhu has awoken!!!!!!!!!");
			Log.error("Cthulhu has awoken!!!!!!!!");
			Log.error("**+++++++++++"+caught.getLocalizedMessage()+"**+++++++++++"+caught.getMessage());
			
		}
	}
	
}
