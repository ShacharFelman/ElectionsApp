import javafx.application.Application;
import javafx.stage.Stage;
import model.ElectionManager;
import view.ElectionView;

import java.io.IOException;
import java.time.*;

import controller.Controller;

public class MainElectionProgram extends Application {

	private Controller theController;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ElectionView theView = new ElectionView(primaryStage);
		ElectionManager theModel = new ElectionManager(LocalDate.now().getYear(), LocalDate.now().getMonth());
		theController = new Controller(theModel, theView);
	}
	
	@Override
	public void stop(){
		try {
			theController.saveToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
