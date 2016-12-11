/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	private JTextField name;
	private JButton add;
	private JButton delete;
	private JButton lookup;
	private JTextField status;
	private JButton changeStatus;
	private JTextField picture;
	private JButton changePicture;
	private JTextField friend;
	private JButton addFriend;
	
	private FacePamphletDatabase database;
	private FacePamphletProfile currentProfile;
	private FacePamphletCanvas canvas;
	
	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		//NORTH interactors
		add(new JLabel("Name"), NORTH);
		name = new JTextField(TEXT_FIELD_SIZE);
		add(name, NORTH);
		add = new JButton("Add");
		add(add, NORTH);
		delete = new JButton("Delete");
		add(delete, NORTH);
		lookup = new JButton("Lookup");
		add(lookup, NORTH);
		
		//WEST interactors
		status = new JTextField(TEXT_FIELD_SIZE);
		add(status, WEST);
		status.addActionListener(this);
		changeStatus = new JButton("Change Status");
		add(changeStatus, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		
		picture = new JTextField(TEXT_FIELD_SIZE);
		add(picture, WEST);
		picture.addActionListener(this);
		changePicture = new JButton("Change Picture");
		add(changePicture, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		
		friend = new JTextField(TEXT_FIELD_SIZE);
		add(friend, WEST);
		friend.addActionListener(this);
		addFriend = new JButton("Add Friend");
		add(addFriend, WEST);
		
		//other initializations
		database = new FacePamphletDatabase();
		currentProfile = null;
		canvas = new FacePamphletCanvas();
		add(canvas);
		
		addActionListeners();
    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
		//add friend
    	if (e.getSource() == add && !name.getText().equals("")) {
			if (database.containsProfile(name.getText())) {
				currentProfile = database.getProfile(name.getText());
				canvas.displayProfile(currentProfile);
				canvas.showMessage("A profile with the name " + name.getText() +
						" already exists");
			} else {
				FacePamphletProfile newProfile = new FacePamphletProfile(name.getText());
				database.addProfile(newProfile);
				currentProfile = newProfile;
				canvas.displayProfile(currentProfile);
				canvas.showMessage("New profile created");
			}
		}
    	
    	//delete friend
    	if (e.getSource() == delete && !name.getText().equals("")) {
    		if (database.containsProfile(name.getText())) {
				database.deleteProfile(name.getText());
				canvas.removeAll();
				canvas.showMessage("Profile of " + name.getText() + " deleted");
			} else {
				canvas.showMessage("A profile with the name " + name.getText() +
								   " does not exist");
			}
    		currentProfile = null;
    	}
    	
    	//lookup friend
    	if (e.getSource() == lookup && !name.getText().equals("")) {
    		if (database.containsProfile(name.getText())) {
    			currentProfile = database.getProfile(name.getText());
    			canvas.displayProfile(currentProfile);
    			canvas.showMessage("Displaying " + currentProfile.getName());
    		} else {
    			canvas.removeAll();
				canvas.showMessage("A profile with the name " + name.getText() +
				   				   " does not exist");
				currentProfile = null;
    		}
    	}
    	
    	//change status
    	if ((e.getSource() == status || e.getSource() == changeStatus) &&
    		!status.getText().equals("")) {
    		if (currentProfile == null) {
    			canvas.showMessage("Please select a profile to change status");
    		} else {
    			currentProfile.setStatus(currentProfile.getName() + " is " + status.getText());
    			canvas.displayProfile(currentProfile);
    			canvas.showMessage("Status updated to " + status.getText());
    		}
    	}
    	
    	//change picture
    	if ((e.getSource() == picture || e.getSource() == changePicture) &&
        	!picture.getText().equals("")) {
        	if (currentProfile == null) {
        		canvas.showMessage("Please select a profile to change picture");
        	} else {
    			GImage image = null;
    			try {
    				image = new GImage(picture.getText());
    				currentProfile.setImage(image);
    				canvas.displayProfile(currentProfile);
    				canvas.showMessage("Picture updated");
    			} catch (ErrorException ex) {
    				canvas.showMessage("Unable to open image file: " + picture.getText());
    			}
        	}
        }
    	
    	//add friend
    	if ((e.getSource() == friend || e.getSource() == addFriend) &&
    		!friend.getText().equals("")) {
    		String friendName = friend.getText();
    		FacePamphletProfile friend = database.getProfile(friendName);
        	if (currentProfile == null) {
        		canvas.showMessage("Please select a profile to add friend");
    		} else if (!database.containsProfile(friendName)) {
    			canvas.displayProfile(currentProfile);
    			canvas.showMessage(friendName + " does not exist");
			} else if (currentProfile.addFriend(friendName)) {
				friend.addFriend(currentProfile.getName());
				canvas.displayProfile(currentProfile);
				canvas.showMessage(friendName + " added as a friend");
			} else {
				canvas.showMessage(currentProfile.getName() + " already has " +
								   friendName + " as a friend");
			}
        }
	}
}
