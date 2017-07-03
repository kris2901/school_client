package controllers;

import interfaces.IController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.Main;
import application.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.UserWindow;

/**
 * The Class TeacherDownloadSolutionController - this class download solution to teacher
 */
public class TeacherDownloadSolutionController implements IController {

	/** The resources . */
    @FXML
    private ResourceBundle resources;

    /** The location . */
    @FXML
    private URL location;

    /** The Back To Menu Button . */    
    @FXML
    private Button BackToMenuButton;

    /** The choose Course Combo box . */      
    @FXML
    private AnchorPane chooseCourseCB;

    /** The check assignment . */    
    @FXML
    private Label CheckPupilAssignment;

    /** The Choose Assignment Combo Box . */    
    @FXML
    private ComboBox<String> ChooseAssignmentCB;

    /** The Choose Pupil Combo Box . */  
    @FXML
    private ComboBox<String> ChoosePupilCB;

    /** The Choose course Combo Box . */  
    @FXML
    private ComboBox<String> ChooseCourseCB;

    /** The download button . */  
    @FXML
    private Button DownloadButton;

    /** The combo box choice . */  
    private String CBchoice;
    
    /** The assignment name . */  
    private String AssName;
    
    /** The pupil choice . */  
    private String pupilChoice;
    
    /**
     * choose course
     *
     * @param event - choose course
     */
    @FXML
    void chooseCourse(ActionEvent event) {
       	
    }

    /**
     * choose course
     *
     * @param event - choose course
     */
    @FXML
    void ChooseCourse(ActionEvent event) {
    	CBchoice=ChooseCourseCB.getSelectionModel().getSelectedItem();
    	ArrayList<String> data = new ArrayList<String>();
 		data.add("Load Assignment in course");
 		data.add("select");
 		data.add("assignment");
 		data.add("courseId");
 		data.add(CBchoice);

 		try
 		{
 			Main.client.sendToServer(data);
 		}
 		catch (IOException e)
 		{
 			e.printStackTrace();
 		}
    }

    /**
     * choose assignment
     *
     * @param event - choose assignment
     */
    @FXML
    void ChooseAssignment(ActionEvent event) {
    	AssName=ChooseAssignmentCB.getSelectionModel().getSelectedItem();
    }
    
    public void loadCourses(){
    	ArrayList<String> data = new ArrayList<String>();
		data.add("Load Courses for Teacher");
		data.add("select");
		data.add("course_in_class");
		data.add("teacherId");
		data.add(UserController.CurrentUserID);

		try
		{
			Main.client.sendToServer(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    }

    /**
     * download solution
     *
     * @param event - download solution
     */
    @FXML
    void DownloadSolution(ActionEvent event) {
    	if(CBchoice.equals(""))
    	{
    		new Alert(AlertType.ERROR, "Choose Course From List First.", ButtonType.OK).showAndWait();
    	}
    	else if (AssName.equals(""))
    	{
    		new Alert(AlertType.ERROR, "Choose Assignment From List First.", ButtonType.OK).showAndWait();
    	}
    }

    /**
     * back to menu
     *
     * @param event - back to menu
     */
    @FXML
    void BackToMenu(ActionEvent event) {
    	UserWindow.closeUserWindow(getClass(), (Stage) BackToMenuButton.getScene().getWindow());
    }

    /**
     * choose pupil
     *
     * @param event - choose pupil
     */
    @FXML
    void ChoosePupil(ActionEvent event) {
    	pupilChoice=ChoosePupilCB.getSelectionModel().getSelectedItem();
    }

    /**
     * initialize
     */
    @FXML
    void initialize() {
        assert BackToMenuButton != null : "fx:id=\"BackToMenuButton\" was not injected: check your FXML file 'CheckAssignment.fxml'.";
        assert chooseCourseCB != null : "fx:id=\"chooseCourseCB\" was not injected: check your FXML file 'CheckAssignment.fxml'.";
        assert CheckPupilAssignment != null : "fx:id=\"CheckPupilAssignment\" was not injected: check your FXML file 'CheckAssignment.fxml'.";
        assert ChooseAssignmentCB != null : "fx:id=\"ChooseAssignmentCB\" was not injected: check your FXML file 'CheckAssignment.fxml'.";
        assert ChoosePupilCB != null : "fx:id=\"ChoosePupilCB\" was not injected: check your FXML file 'CheckAssignment.fxml'.";
        assert ChooseCourseCB != null : "fx:id=\"ChooseCourseCB\" was not injected: check your FXML file 'CheckAssignment.fxml'.";
        assert DownloadButton != null : "fx:id=\"DownloadButton\" was not injected: check your FXML file 'CheckAssignment.fxml'.";

        Main.client.controller = this;
        //Main.stack.push("TeacherCheckAssignment");
        loadCourses();
    }

    /**
     * Handles the answer from the server according to the type of answer.
     */
	@Override
	public void handleAnswer(Object msg) {
		if (msg == null)
		{
			// error
			new Alert(AlertType.ERROR, "Item has not found.", ButtonType.OK).showAndWait();
			return;
		}
		ArrayList<String> arr = (ArrayList<String>) msg;
		String type = arr.remove(0);
		
		if(type.equals("Load Courses for Teacher"))
		{
			for (String row : arr)
			{
				String[] cols = row.split(";");
				HashMap<String, String> map = new HashMap<>();
				for (String col : cols)
				{
					String[] field = col.split("=");
					map.put(field[0], field[1]);
				}
				String CourseID = map.get("courseId");
				ChooseCourseCB.getItems().add(CourseID);
			}
		}
		
		if(type.equals("Load Pupils"))
		{
			for (String row : arr)
			{
				String[] cols = row.split(";");
				HashMap<String, String> map = new HashMap<>();
				for (String col : cols)
				{
					String[] field = col.split("=");
					map.put(field[0], field[1]);
				}
				String userID = map.get("userId");
				String UserName=map.get("userFirstName");
				String UserLName=map.get("userLastName");
				ChoosePupilCB.getItems().add(userID + ": " + UserName + " " + UserLName);
			}
		}
		
		if(type.equals("Load Assignment in course"))
		{
			for (String row : arr)
			{
				String[] cols = row.split(";");
				HashMap<String, String> map = new HashMap<>();
				for (String col : cols)
				{
					String[] field = col.split("=");
					map.put(field[0], field[1]);
				}
				String assignmentName = map.get("assignmentName");
				ChooseAssignmentCB.getItems().add(assignmentName);
			}
		}
	}

}