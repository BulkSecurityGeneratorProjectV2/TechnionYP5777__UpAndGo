package upandgo.client.view;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.ListDropDown;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarBrand;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.NavbarText;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Text;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import upandgo.client.LoginDialog;
import upandgo.client.Resources;
import upandgo.client.Resources.NavBarStyle;

import upandgo.client.presenter.NavBarPresenter;
import upandgo.shared.entities.Semester;

public class NavBarView extends FlowPanel implements NavBarPresenter.Display {
	
	
	
	private NavBarStyle nvStyle = Resources.INSTANCE.navBarStyle();
	
	Navbar navbar = new Navbar();
	NavbarNav navbarNav = new NavbarNav();
	NavbarCollapse navbarCol = new NavbarCollapse();
	NavbarHeader header = new NavbarHeader();
	NavbarBrand brand = new NavbarBrand();
	NavbarText signInText = new NavbarText();
	NavbarText semesterText = new NavbarText();
    ListDropDown semesterList = new ListDropDown();
    AnchorButton semesterButton = new AnchorButton();
    DropDownMenu semesterMenu = new DropDownMenu();
    List<AnchorListItem> semesterListItems = new ArrayList<>();
    Modal semesterModal = new Modal();
	Button semesterModalAcceptButton = new Button("<i class=\"fa fa-check\" aria-hidden=\"true\"></i>&nbsp;&nbsp;אשר");
	org.gwtbootstrap3.client.ui.Button signInButton = new org.gwtbootstrap3.client.ui.Button("כניסה / הרשמה");
	
	public NavBarView(){
    	InitializePanel();
    	nvStyle.ensureInjected();
    }

	private void InitializePanel() {
		//this.addStyleName(nvStyle.NavBarPanel());
		//this.addStyleName("navbar navbar-toggleable-md navbar-light bg-faded");
		//appTitle.addStyleName("navbar-brand");
		//this.add(appTitle);
		
		//navbar.setPosition(NavbarPosition.FIXED_TOP);
		brand.setText("Up&Go");
		signInText.setPull(Pull.RIGHT);
		signInText.setPaddingRight(15);
		signInText.add(signInButton);
		signInButton.setSize(ButtonSize.EXTRA_SMALL);
		signInButton.setType(ButtonType.LINK);
		signInButton.setIcon(IconType.USER_CIRCLE);
		header.add(brand);
		navbar.add(header);
		navbarCol.add(signInText);
		
		
		semesterButton.setDataToggle(Toggle.DROPDOWN);
		semesterButton.setText("בחר סמסטר");
		semesterList.add(semesterButton);
		semesterList.setPull(Pull.LEFT);		
		for (Semester semester : Semester.values()){
			AnchorListItem semesterListItem = new AnchorListItem();
			semesterListItem.setId(semester.getId());
			semesterListItem.setText(semester.getName());
			semesterListItems.add(semesterListItem);
			semesterMenu.add(semesterListItem);
		}
		semesterList.add(semesterMenu);
		navbarNav.add(semesterList);
		navbarCol.add(navbarNav);

		semesterText.setPull(Pull.NONE);
		semesterText.setWidth("30%");
		semesterText.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		navbarCol.add(semesterText);

		
		navbar.add(navbarCol);

		this.add(navbar);
		//this.addStyleName(nvStyle.navBarPanel());
		
		
//		signInButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(@SuppressWarnings("unused") ClickEvent arg0) {
//
//				Modal loginBox = new Modal();
//				loginBox.setFade(true);
//				loginBox.setTitle("כניסה / הרשמה");
//				loginBox.setId("login");
//				loginBox.add(new LoginDialog());
//				loginBox.show();
//
///*				// TODO Auto-generated method stub
//				LoginDialog myDialog = new LoginDialog();
//
//	            int left = Window.getClientWidth()/ 3;
//	            int top = Window.getClientHeight()/ 3;
//	            myDialog.setPopupPosition(left, top);
//	            myDialog.show();*/
//			}
//		});
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends HasClickHandlers & HasText> T getSignInOutButton() {
		return (T) signInButton;
	}

	@Override
	public Widget getAsWidget() {
		return this.asWidget();
	}
	
	@Override
	public List<AnchorListItem> getSemesterListItems(){
		return this.semesterListItems;
	}
	
	@Override
	public AnchorButton getSemesterButton(){
		return this.semesterButton;
	}
	
	@Override
	public HasClickHandlers getSemesterModalAcceptButton(){
		return this.semesterModalAcceptButton;
	}
	
	@Override
	public void semesterModalAcceptButtonSetSpin(boolean spin){
		if (spin)
			semesterModalAcceptButton.setHTML("<i class=\"fa fa-spinner fa-spin\" aria-hidden=\"true\"></i>&nbsp;&nbsp;נא המתן");
		else
			semesterModalAcceptButton.setHTML("<i class=\"fa fa-check\" aria-hidden=\"true\"></i>&nbsp;&nbsp;אשר");
	}
		
	@Override
	public Modal initializeSemesterModal(Semester semester){
		semesterModal = new Modal();
		semesterModal.setFade(true);
		semesterModal.setTitle("אזהרה");
		
		ModalBody semesterModalBody = new ModalBody();
		semesterModalBody.add(new Heading(HeadingSize.H4,"שינוי סמסטר יוביל למחיקת כל השינויים שלא נשמרו."));
		semesterModalBody.add(new Heading(HeadingSize.H4,"האם אתה בטוח שברצונך לעבור לסמסטר " + semester.getName() + "?"));
		semesterModal.add(semesterModalBody);
		
		ModalFooter semesterModalFooter = new ModalFooter();
		Button semesterModalCancelButton = new Button("<i class=\"fa fa-times\" aria-hidden=\"true\"></i>&nbsp;&nbsp;בטל");
		semesterModalCancelButton.setStyleName("btn btn-success");
		semesterModalCancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				semesterModal.hide();
			}
		});
		semesterModalAcceptButton.setStyleName("btn btn-danger");
		
		semesterModalFooter.add(semesterModalCancelButton);
		semesterModalFooter.add(semesterModalAcceptButton);
		semesterModal.add(semesterModalFooter);
		return semesterModal;
	}
	
	@Override
	public NavbarText getSemesterText(){
		return this.semesterText;
	}
	
	@Override
	public void markChoosenSemesterEntry(Semester semester){
		for (final AnchorListItem semesterItem : semesterListItems){
			if (semesterItem.getId().equals(semester.getId())){
				semesterItem.setEnabled(false);
				semesterItem.setStyleName(nvStyle.choosenSemesterEntry());
			} else {
				semesterItem.setEnabled(true);
				semesterItem.removeStyleName(nvStyle.choosenSemesterEntry());
			}

		}
	}

}
