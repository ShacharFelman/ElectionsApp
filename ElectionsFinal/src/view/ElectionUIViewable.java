package view;

import java.io.*;

import exceptions.*;
import listeners.*;
import model.*;

public interface ElectionUIViewable {

	public void showView();
	public void registerListener(ElectionUIListenable l);
	public void removeVoter(String theVoter);
	public void updateElectionStarted();
	public void updateElectionEnded();
	
	
	
}
