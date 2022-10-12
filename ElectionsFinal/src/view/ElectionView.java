package view;

import java.io.FileNotFoundException;

// Aviram Shabtay && Shahar felman progect 
// 206447377 && 206345282  

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.swing.JOptionPane;

import exceptions.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Callback;
import listeners.*;
import model.*;
import model.ElectionManager.eBallotType;
import model.Party.eWing;
import viewEvents.*;

public class ElectionView implements ElectionUIViewable {

	private static Color COLOR_STATUS_OK = Color.DARKGREEN;
	private static Color COLOR_STATUS_ERROR = Color.RED;
	private static Color COLOR_MENU_FILL = Color.ROYALBLUE;
	private static Color COLOR_MENU_FILL_SELECTED = Color.CORNFLOWERBLUE;
	private static Color COLOR_MENU_TEXT = Color.WHITE;

	private static Background BACKGOUND_MENU = new Background(
			new BackgroundFill(COLOR_MENU_FILL, CornerRadii.EMPTY, Insets.EMPTY));
	private static Background BACKGOUND_MENU_SELECTED = new Background(
			new BackgroundFill(COLOR_MENU_FILL_SELECTED, CornerRadii.EMPTY, Insets.EMPTY));

	private static Font fontMenu = Font.font("Tahoma", 16);
	private static Font fontHeadline = Font.font("Tahoma", FontWeight.BOLD, 18);

	private Stage primaryStage;

	private Vector<ElectionUIListenable> allListeners;

	private BorderPane bpRoot;

	private VBox vbMainMenu;
	private HBox hbHeadline;
	ListView<Label> lstLogMessages;
	private VBox vbLogMessages;
	private ScrollPane scrlpShowDetails = new ScrollPane();

	private GridPane gpCenter1;
	private GridPane gpCenter2;
	private GridPane gpCenter3;
	private GridPane gpCenter4;
	private FlowPane fpCenter5;
	private FlowPane fpCenter6;
	private FlowPane fpCenter7;
	private GridPane gpCenter8;
	private TabPane tbCenter9;

	Button btnMenu1;
	Button btnMenu2;
	Button btnMenu3;
	Button btnMenu4;
	Button btnMenu5;
	Button btnMenu6;
	Button btnMenu7;
	Button btnMenu8;
	Button btnMenu9;

	Label lblElectionHeadline;
	Label lblYear;
	Label lblMonth;
	ComboBox<String> cmbAllElectors;

	public ElectionView(Stage theStage) throws FileNotFoundException {
		primaryStage = theStage;
		primaryStage.setTitle("Elections");

		allListeners = new Vector<ElectionUIListenable>();

		btnMenu1 = new Button("Add ballot");
		btnMenu1 = new Button();
		btnMenu2 = new Button("Add citizen");
		btnMenu3 = new Button("Add party");
		btnMenu4 = new Button("Add candidate");
		btnMenu5 = new Button("Show ballots");
		btnMenu6 = new Button("Show citizens");
		btnMenu7 = new Button("Show parties");
		btnMenu8 = new Button("Start elections");
		btnMenu9 = new Button("Show results");

		setHeadLineDesign();
		setMainMenuDesign();
		setLogMessagesDesign();

		bpRoot = new BorderPane();
		bpRoot.setTop(hbHeadline);
		bpRoot.setLeft(vbMainMenu);
		bpRoot.setBottom(vbLogMessages);

		btnMenu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				getBallotFromUser();
				bpRoot.setCenter(gpCenter1);
				setMainMenuButtonsDesign();
				btnMenu1.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				getCitizenFromUser();
				bpRoot.setCenter(gpCenter2);
				setMainMenuButtonsDesign();
				btnMenu2.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				getPartyFromUser();
				bpRoot.setCenter(gpCenter3);
				setMainMenuButtonsDesign();
				btnMenu3.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				getCandidateFromUser();
				bpRoot.setCenter(gpCenter4);
				setMainMenuButtonsDesign();
				btnMenu4.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showBallotsData();
				scrlpShowDetails.setContent(fpCenter5);
				bpRoot.setCenter(scrlpShowDetails);
				setMainMenuButtonsDesign();
				btnMenu5.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu6.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showCitizensData();
				scrlpShowDetails.setContent(fpCenter6);
				bpRoot.setCenter(scrlpShowDetails);
				setMainMenuButtonsDesign();
				btnMenu6.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu7.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showPartiesData();
				scrlpShowDetails.setContent(fpCenter7);
				bpRoot.setCenter(scrlpShowDetails);
				setMainMenuButtonsDesign();
				btnMenu7.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu8.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				try {
					startElection();
				} catch (ElectionStartedException e) {
					e.printStackTrace();
				}
				bpRoot.setCenter(gpCenter8);
				btnMenu8.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});

		btnMenu9.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				ElectionResults();
				bpRoot.setCenter(tbCenter9);
				btnMenu9.setBackground(BACKGOUND_MENU_SELECTED);
			}
		});
	}

//===================================>
//<= Show view - primary stage		=>
//<= initialized from controller	=>
//===================================>
	public void showView() {
		setHeadLineElectionStatus();
		setMenuButtonsStatus();
		primaryStage.setScene(new Scene(bpRoot, 900, 500));
		primaryStage.show();
	}

//===============================>
//<= Register listener 			=>
//<= (controller) to this view	=>
//===============================>
	public void registerListener(ElectionUIListenable l) {
		allListeners.add(l);
	}

//====================>
//<= Build menu area =>
//====================>
	public void setMainMenuDesign() {
		vbMainMenu = new VBox();
		vbMainMenu.setSpacing(10);
		vbMainMenu.setPadding(new Insets(10));
		vbMainMenu.setAlignment(Pos.CENTER_LEFT);
		setMainMenuButtonsDesign();
		vbMainMenu.getChildren().addAll(btnMenu1, btnMenu2, btnMenu3, btnMenu4, btnMenu5, btnMenu6, btnMenu7, btnMenu8,
				btnMenu9);
		vbMainMenu.setBackground(BACKGOUND_MENU);
	}

//===========================>
//<= Build buttons and set =>
//<=  fonts and colors 		=>
//===========================>
	public void setMainMenuButtonsDesign() {
		btnMenu1.setMinWidth(150);
		btnMenu2.setMinWidth(150);
		btnMenu3.setMinWidth(150);
		btnMenu4.setMinWidth(150);
		btnMenu5.setMinWidth(150);
		btnMenu6.setMinWidth(150);
		btnMenu7.setMinWidth(150);
		btnMenu8.setMinWidth(150);
		btnMenu9.setMinWidth(150);
		btnMenu1.setBackground(BACKGOUND_MENU);
		btnMenu2.setBackground(BACKGOUND_MENU);
		btnMenu3.setBackground(BACKGOUND_MENU);
		btnMenu4.setBackground(BACKGOUND_MENU);
		btnMenu5.setBackground(BACKGOUND_MENU);
		btnMenu6.setBackground(BACKGOUND_MENU);
		btnMenu7.setBackground(BACKGOUND_MENU);
		btnMenu8.setBackground(BACKGOUND_MENU);
		btnMenu9.setBackground(BACKGOUND_MENU);
		btnMenu1.setTextFill(COLOR_MENU_TEXT);
		btnMenu2.setTextFill(COLOR_MENU_TEXT);
		btnMenu3.setTextFill(COLOR_MENU_TEXT);
		btnMenu4.setTextFill(COLOR_MENU_TEXT);
		btnMenu5.setTextFill(COLOR_MENU_TEXT);
		btnMenu6.setTextFill(COLOR_MENU_TEXT);
		btnMenu7.setTextFill(COLOR_MENU_TEXT);
		btnMenu8.setTextFill(COLOR_MENU_TEXT);
		btnMenu9.setTextFill(COLOR_MENU_TEXT);
		btnMenu1.setAlignment(Pos.CENTER_LEFT);
		btnMenu2.setAlignment(Pos.CENTER_LEFT);
		btnMenu3.setAlignment(Pos.CENTER_LEFT);
		btnMenu4.setAlignment(Pos.CENTER_LEFT);
		btnMenu5.setAlignment(Pos.CENTER_LEFT);
		btnMenu6.setAlignment(Pos.CENTER_LEFT);
		btnMenu7.setAlignment(Pos.CENTER_LEFT);
		btnMenu8.setAlignment(Pos.CENTER_LEFT);
		btnMenu9.setAlignment(Pos.CENTER_LEFT);
		btnMenu1.setFont(fontMenu);
		btnMenu2.setFont(fontMenu);
		btnMenu3.setFont(fontMenu);
		btnMenu4.setFont(fontMenu);
		btnMenu5.setFont(fontMenu);
		btnMenu6.setFont(fontMenu);
		btnMenu7.setFont(fontMenu);
		btnMenu8.setFont(fontMenu);
		btnMenu9.setFont(fontMenu);
		btnMenu1.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu2.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu3.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu4.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu5.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu6.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu7.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu8.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu9.setOnMouseEntered(new EventMouseEnteredMenuButton());
		btnMenu1.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu1.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu2.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu3.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu4.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu5.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu6.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu7.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu8.setOnMouseExited(new EventMouseExitedMenuButton());
		btnMenu9.setOnMouseExited(new EventMouseExitedMenuButton());
	}

//===================>
//<= Build headline =>
//===================>
	public void setHeadLineDesign() {
		hbHeadline = new HBox();
		lblElectionHeadline = new Label("ELECTIONS");
		lblElectionHeadline.setFont(fontHeadline);
		lblElectionHeadline.setPadding(new Insets(10));
		lblElectionHeadline.setTextFill(COLOR_MENU_TEXT);
		hbHeadline.setBackground(BACKGOUND_MENU);
		hbHeadline.getChildren().add(lblElectionHeadline);
		hbHeadline.setAlignment(Pos.CENTER);
		
		lblYear = new Label();
		lblMonth = new Label();
		lblYear.setPadding(new Insets(10));
		lblMonth.setPadding(new Insets(10));
		
		lblYear.setFont(fontHeadline);
		lblMonth.setFont(fontHeadline);
		lblYear.setTextFill(COLOR_MENU_TEXT);
		lblMonth.setTextFill(COLOR_MENU_TEXT);
		
		hbHeadline.getChildren().addAll(lblYear, lblMonth);
	}

//=========================================>
//<= Update headline with Election Status =>
//=========================================>
	public void setHeadLineElectionStatus() {
		lblYear.setText(allListeners.get(0).getElectionYear() + "");
		lblMonth.setText(allListeners.get(0).getElectionMonth().name());
		if (getIsElectionEnded()) {
			lblElectionHeadline.setText(lblElectionHeadline.getText() + " - Ended!");
		}
	}

//==================================>
//<= Build log messages scrollpane =>
//==================================>
	public void setLogMessagesDesign() {
		lstLogMessages = new ListView<Label>();
		lstLogMessages.setMaxHeight(80);
		lstLogMessages.setMinHeight(80);
		vbLogMessages = new VBox(lstLogMessages);
	}

//<==============================>
//<----- Case 1: Add ballot ----->
//<= Receive parameters from 	=>
//<= user input and send him	=>
//<= him to controller -> model =>
//===============================>
	public void getBallotFromUser() {

		gpCenter1 = new GridPane();
		gpCenter1.setPadding(new Insets(10));
		gpCenter1.setHgap(10);
		gpCenter1.setVgap(10);

		gpCenter1.add(new Label("Enter the address: "), 0, 0);
		TextField tfAddress = new TextField();
		gpCenter1.add(tfAddress, 1, 0);

		gpCenter1.add(new Label("Select The Type Of The Ballot: "), 0, 1);
		ComboBox<eBallotType> cmbBallotTypes = new ComboBox<eBallotType>();
		cmbBallotTypes.getItems().addAll(eBallotType.values());
		gpCenter1.add(cmbBallotTypes, 1, 1);

		Button btnConfirm = new Button("Add ballot");
		gpCenter1.add(btnConfirm, 0, 3);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfAddress.getText().isEmpty() || cmbBallotTypes.getValue() == null) {
					messageFailedOperation("Add ballot", "All fields must be filled/selected.");
				} else {
					for (ElectionUIListenable l : allListeners)
						l.addBallotFromUser(cmbBallotTypes.getValue(), tfAddress.getText());

					messageSuccessOperation("Add " + cmbBallotTypes.getValue().toString().toLowerCase() + " ballot");
					tfAddress.clear();
					cmbBallotTypes.setValue(null);
				}
			}
		});
	}

//<==================================>
//<------ Case 2: Add citizen ------->
//<= Receive parameters from user	=>
//<= input, create citizen and send => 
//<= him to controller -> model		=>
//===================================>
	public void getCitizenFromUser() {

		gpCenter2 = new GridPane();
		gpCenter2.setPadding(new Insets(10));
		gpCenter2.setHgap(10);
		gpCenter2.setVgap(10);

		TextField tfIDNum = new TextField();
		gpCenter2.add(new Label("Enter citizen's ID number (9 digits): "), 0, 0);
		gpCenter2.add(tfIDNum, 1, 0);

		TextField tfName = new TextField();
		gpCenter2.add(new Label("Enter citizen's name: "), 0, 1);
		gpCenter2.add(tfName, 1, 1);

		ComboBox<Integer> cmbYears = new ComboBox<Integer>();
		for (int i = LocalDate.now().getYear() - 18; i > LocalDate.now().getYear() - Citizen.AGE_LIMIT; i--) {
			cmbYears.getItems().add(i);
		}
		gpCenter2.add(new Label("Enter citizen's year of birth: "), 0, 2);
		gpCenter2.add(cmbYears, 1, 2);

		CheckBox chkIsSick = new CheckBox();
		gpCenter2.add(new Label("Is isolated/sick: "), 0, 3);
		gpCenter2.add(chkIsSick, 1, 3);

		Label lblNumOfSickDays = new Label("Number of days the citizen is isolated/sick: ");
		TextField tfNumOfSickDays = new TextField();
		gpCenter2.add(lblNumOfSickDays, 0, 4);
		lblNumOfSickDays.setVisible(false);
		tfNumOfSickDays.setVisible(false);
		gpCenter2.add(tfNumOfSickDays, 1, 4);

		chkIsSick.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lblNumOfSickDays.setVisible(chkIsSick.isSelected());
				tfNumOfSickDays.setVisible(chkIsSick.isSelected());
				if (!chkIsSick.isSelected()) {
					tfNumOfSickDays.setText("0");
				}
			}
		});

		Button btnConfirm = new Button("Add citizen");
		gpCenter2.add(btnConfirm, 0, 5);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				int numOfSickDays = 0;
				if (chkIsSick.isSelected() && !tfNumOfSickDays.getText().isEmpty()) {
					try {
						numOfSickDays = Integer.parseInt(tfNumOfSickDays.getText());
					} catch (NumberFormatException e) {
						messageFailedOperation("Add citizen", "Isolated/sick days must be a number");
					}
				}
				if (tfIDNum.getText().isEmpty() || tfName.getText().isEmpty() || cmbYears.getValue() == null) {
					messageFailedOperation("Add citizen", "All fields must be filled/selected.");
				} else {
					for (ElectionUIListenable l : allListeners)
						try {
							l.addCitizenFromUser(tfName.getText(), tfIDNum.getText(), cmbYears.getValue(),
									chkIsSick.isSelected(), numOfSickDays);
							messageSuccessOperation("Add citizen " + tfName.getText());
							tfName.setText("");
							tfIDNum.setText("");
							cmbYears.setValue(null);
							chkIsSick.setSelected(false);
							tfNumOfSickDays.setText("");
						} catch (IDException | ElectorAgeException | ElectorAlreadyExistsException
								| YearNotValidException | BallotNotExistsException | ElectorNotMatchBallot
								| NegativeSickDaysNumberExcepetion e) {
							messageFailedOperation("Add citizen", e.getMessage());
						}
				}
			}
		});
	}

//<==================================>
//<------- Case 3: Add party -------->
//<= Receive parameters from user	=>
//<= input, create party and send	=> 
//<= her to election manager 		=>
//===================================>
	public void getPartyFromUser() {
		gpCenter3 = new GridPane();
		gpCenter3.setPadding(new Insets(10));
		gpCenter3.setHgap(10);
		gpCenter3.setVgap(10);

		gpCenter3.add(new Label("Enter the party's name: "), 0, 0);
		TextField tfName = new TextField();
		gpCenter3.add(tfName, 1, 0);

		gpCenter3.add(new Label("Select the political wing: "), 0, 1);
		ComboBox<eWing> cmbWingTypes = new ComboBox<eWing>();
		cmbWingTypes.getItems().addAll(eWing.values());
		gpCenter3.add(cmbWingTypes, 1, 1);

		gpCenter3.add(new Label("Enter the party's creation date:"), 0, 2);
		DatePicker createDate = new DatePicker();
		gpCenter3.add(createDate, 1, 2);

		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();
						setDisable(empty || item.compareTo(today) > 0);
					}
				};
			}
		};
		createDate.setDayCellFactory(callB);

		Button btnConfirm = new Button("Add party");
		gpCenter3.add(btnConfirm, 0, 3);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfName.getText().isEmpty() || cmbWingTypes.getValue() == null || createDate.getValue() == null) {
					messageFailedOperation("Add party", "All fields must be filled/selected.");
				} else {
					for (ElectionUIListenable l : allListeners) {
						l.addPartyFromUser(tfName.getText(), createDate.getValue(), cmbWingTypes.getValue());
						messageSuccessOperation("Add part " + tfName.getText());
						tfName.setText("");
						cmbWingTypes.setValue(null);
						createDate.setValue(null);
					}

				}
			}
		});
	}

//<==================================>
//<----- Case 4: Add candidate ------>
//<= Receive parameters from user	=>
//<= input, create candidate and	=> 
//<= send him to election manager 	=>
//===================================>
	public void getCandidateFromUser() {
		gpCenter4 = new GridPane();
		gpCenter4.setPadding(new Insets(10));
		gpCenter4.setHgap(10);
		gpCenter4.setVgap(10);

		TextField tfIDNum = new TextField();
		gpCenter4.add(new Label("Enter candidate's ID number (9 digits): "), 0, 0);
		gpCenter4.add(tfIDNum, 1, 0);

		TextField tfName = new TextField();
		gpCenter4.add(new Label("Enter candidate's name: "), 0, 1);
		gpCenter4.add(tfName, 1, 1);

		ComboBox<Integer> cmbYears = new ComboBox<Integer>();
		for (int i = LocalDate.now().getYear() - 21; i > LocalDate.now().getYear() - Citizen.AGE_LIMIT; i--) {
			cmbYears.getItems().add(i);
		}
		gpCenter4.add(new Label("Enter citizen's year of birth: "), 0, 2);
		gpCenter4.add(cmbYears, 1, 2);

		gpCenter4.add(new Label("Select candidat's party: "), 0, 3);
		ComboBox<String> cmbParties = new ComboBox<String>();
		cmbParties.getItems().addAll(allListeners.get(0).getPartiesNameList());
		gpCenter4.add(cmbParties, 1, 3);

		CheckBox chkRankConfirm = new CheckBox("Confirm");
		Label lblRankConfirm = new Label();
		gpCenter4.add(lblRankConfirm, 0, 4);
		gpCenter4.add(chkRankConfirm, 1, 4);
		chkRankConfirm.setVisible(false);

		cmbParties.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				try {
					int rankNum = allListeners.get(0).getPartyNextAvailableRank(cmbParties.getValue()) + 1;
					lblRankConfirm.setText("Next available rank is: " + rankNum);
					chkRankConfirm.setVisible(true);
				} catch (PartyNotExistsException e1) {
					lblRankConfirm.setText("Selected party not found, please reopen the window and try again.");
					gpCenter4.add(lblRankConfirm, 0, 4);
					chkRankConfirm.setVisible(false);
				}
			}
		});

		Button btnConfirm = new Button("Add candidate");
		gpCenter4.add(btnConfirm, 0, 5);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfIDNum.getText().isEmpty() || tfName.getText().isEmpty() || cmbYears.getValue() == null
						|| cmbParties.getValue() == null || !(chkRankConfirm.isSelected())) {
					messageFailedOperation("Add candidate", "All fields must be filled/selected.");
				} else {
					for (ElectionUIListenable l : allListeners)
						try {
							l.addCandidateFromUser(tfName.getText(), tfIDNum.getText(), cmbYears.getValue(),
									cmbParties.getValue());
							messageSuccessOperation("Add candidate " + tfName.getText());
						} catch (NumberFormatException | IDException | ElectorAgeException
								| ElectorAlreadyExistsException | YearNotValidException | BallotNotExistsException
								| ElectorNotMatchBallot | CandidateWrongRankException | CandidateWrongAgeException
								| PartyNotExistsException e) {
							messageFailedOperation("Add candidate", e.getMessage());
							tfName.setText("");
							tfIDNum.setText("");
							cmbYears.setValue(null);
							cmbParties.setValue(null);
							chkRankConfirm.setSelected(false);
						}
				}
			}
		});
	}

//<==================================>
//<------- Case 5: Show ballots ----->
//<= Get all ballots data (string)	=>
//<= from controller and show		=>
//===================================>
	public void showBallotsData() {

		fpCenter5 = new FlowPane();
		fpCenter5.setPrefSize(650, 0);
		fpCenter5.setPadding(new Insets(5));
		fpCenter5.setHgap(10);
		fpCenter5.setVgap(10);

		Vector<Ballot<Citizen>> allRegularBallots = allListeners.get(0).getAllRegularBallots();
		Vector<Ballot<Sick>> allCoronaBallots = allListeners.get(0).getAllCoronaBallots();
		Vector<Ballot<Soldier>> allMilitaryBallots = allListeners.get(0).getAllMilitaryBallots();
		Vector<Ballot<SickSoldier>> allCoronaMilitaryBallots = allListeners.get(0).getAllCoronaMilitaryBallots();

		for (int i = 0; i < allRegularBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allRegularBallots.get(i).toStringShort());
			lblData.setText("Regular Ballot.\n" + allRegularBallots.get(i).toStringDetails());
			fpCenter5.getChildren().add(tpData);
			tpData.setContent(lblData);
		}
		for (int i = 0; i < allCoronaBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allCoronaBallots.get(i).toStringShort());
			lblData.setText("Corona Ballot.\n" + allCoronaBallots.get(i).toStringDetails());
			fpCenter5.getChildren().add(tpData);
			tpData.setContent(lblData);
		}
		for (int i = 0; i < allMilitaryBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allMilitaryBallots.get(i).toStringShort());
			lblData.setText("Military Ballot.\n" + allMilitaryBallots.get(i).toStringDetails());
			fpCenter5.getChildren().add(tpData);
			tpData.setContent(lblData);
		}
		for (int i = 0; i < allCoronaMilitaryBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allCoronaMilitaryBallots.get(i).toStringShort());
			lblData.setText("Corona-Military Ballot.\n" + allCoronaMilitaryBallots.get(i).toStringDetails());
			fpCenter5.getChildren().add(tpData);
			tpData.setContent(lblData);
		}

		scrlpShowDetails.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	}

//<==================================>
//<------ Case 6: Show citizens ----->
//<= Get all citizens data (string)	=>
//<= from controller and show 		=>
//===================================>
	public void showCitizensData() {
		fpCenter6 = new FlowPane();
		fpCenter6.setPrefSize(650, 0);
		fpCenter6.setPadding(new Insets(5));
		fpCenter6.setHgap(10);
		fpCenter6.setVgap(10);

		Set<Citizen> allCitizens = allListeners.get(0).getAllCitizens();

		for (int i = 0; i < allCitizens.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setExpanded(false);
			tpData.setMinSize(200, 30);
			Label lblData = new Label();
			tpData.setText(allCitizens.getElement(i).toStringShort());
			lblData.setText(allCitizens.getElement(i).toStringDetails());
			fpCenter6.getChildren().add(tpData);
			tpData.setContent(lblData);
		}

		scrlpShowDetails.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	}

//<==================================>
//<------ Case 7: Show parties ----->
//<= Get all parties data (string)	=>
//<= from controller and show 		=>
//===================================>
	public void showPartiesData() {
		fpCenter7 = new FlowPane();
		fpCenter7.setPrefSize(650, 0);
		fpCenter7.setPadding(new Insets(5));
		fpCenter7.setHgap(10);
		fpCenter7.setVgap(10);

		Vector<Party> allParties = allListeners.get(0).getAllParties();

		for (int i = 0; i < allParties.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setExpanded(false);
			tpData.setMinSize(700, 30);
			Label lblData = new Label();
			tpData.setText(allParties.get(i).toStringShort());
			lblData.setText(allParties.get(i).toStringDetails());
			fpCenter7.getChildren().add(tpData);
			tpData.setContent(lblData);
		}

		scrlpShowDetails.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	}

//<==================================>
//<----- Case 8: Start election ----->
//<= Ask all electors if they want	=>
//<= to vote, if yes ask for who	=> 
//<= and update the vote        	=>
//===================================>
	public void startElection() throws ElectionStartedException {
		if (!getIsElectionStarted()) {
			for (ElectionUIListenable l : allListeners) {
				l.startElection();
			}
		}

		gpCenter8 = new GridPane();
		gpCenter8.setPadding(new Insets(10));
		gpCenter8.setHgap(10);
		gpCenter8.setVgap(10);

		Label lblAllElectorsName = new Label("Please select elector:");
		gpCenter8.add(lblAllElectorsName, 0, 0);

		cmbAllElectors = new ComboBox<>();
		cmbAllElectors.getItems().addAll(allListeners.get(0).getAllElectorsNameID());
		gpCenter8.add(cmbAllElectors, 1, 0);

		Label lblVoteQ = new Label("Would you like to vote?");
		Button btnYes = new Button("Yes");
		Button btnNo = new Button("No");
		gpCenter8.add(lblVoteQ, 0, 1);
		gpCenter8.add(btnYes, 1, 1);
		gpCenter8.add(btnNo, 2, 1);
		lblVoteQ.setVisible(false);
		btnYes.setVisible(false);
		btnNo.setVisible(false);

		Label lblProtectedSuits = new Label("Do you have a protected suit?");
		Button btnHave = new Button("Yes");
		Button btnNotHave = new Button("No");
		gpCenter8.add(lblProtectedSuits, 0, 2);
		gpCenter8.add(btnHave, 1, 2);
		gpCenter8.add(btnNotHave, 2, 2);
		lblProtectedSuits.setVisible(false);
		btnHave.setVisible(false);
		btnNotHave.setVisible(false);

		Label lblSelectParty = new Label("Please select option: ");
		ComboBox<String> cmbAllParties = new ComboBox<>();
		cmbAllParties.getItems().addAll(allListeners.get(0).getPartiesNameList());
		gpCenter8.add(lblSelectParty, 0, 3);
		gpCenter8.add(cmbAllParties, 1, 3);
		lblSelectParty.setVisible(false);
		cmbAllParties.setVisible(false);

		Button btnSendVote = new Button("send your vote");
		gpCenter8.add(btnSendVote, 0, 4);
		btnSendVote.setVisible(false);

		cmbAllElectors.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (cmbAllElectors.getValue() != null) {
					lblVoteQ.setVisible(true);
					btnYes.setVisible(true);
					btnNo.setVisible(true);
				}
			}
		});

		btnYes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (allListeners.get(0).getIsInIsolationByID(cmbAllElectors.getValue())) {
					lblProtectedSuits.setVisible(true);
					btnHave.setVisible(true);
					btnNotHave.setVisible(true);
				} else {
					lblSelectParty.setVisible(true);
					cmbAllParties.setVisible(true);
				}
			}
		});

		btnNo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				messagePopup("Sorry to hear that you didnt find someone to vote for.");
				setElectorVote(cmbAllElectors.getValue(), false, null);
				lblVoteQ.setVisible(false);
				btnYes.setVisible(false);
				btnNo.setVisible(false);
				cmbAllElectors.setValue(null);
			}
		});

		btnHave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				lblSelectParty.setVisible(true);
				cmbAllParties.setVisible(true);
			}
		});

		btnNotHave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				messagePopup("Unfortunately you cant vote without a protected suit.");
				setElectorVote(cmbAllElectors.getValue(), false, null);

				lblVoteQ.setVisible(false);
				btnYes.setVisible(false);
				btnNo.setVisible(false);
				lblProtectedSuits.setVisible(false);
				btnHave.setVisible(false);
				btnNotHave.setVisible(false);
				cmbAllElectors.setValue(null);
			}
		});

		cmbAllParties.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (cmbAllParties.getValue() != null) {
					btnSendVote.setVisible(true);
				} else {
					btnSendVote.setVisible(false);
				}
			}
		});

		btnSendVote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				messagePopup("Your vote has been recorded in the system");
				setElectorVote(cmbAllElectors.getValue(), true, cmbAllParties.getValue());
				lblVoteQ.setVisible(false);
				btnYes.setVisible(false);
				btnNo.setVisible(false);
				lblProtectedSuits.setVisible(false);
				btnHave.setVisible(false);
				btnNotHave.setVisible(false);
				lblSelectParty.setVisible(false);
				cmbAllParties.setVisible(false);
				cmbAllElectors.setValue(null);
				cmbAllParties.setValue(null);
				btnSendVote.setVisible(false);
			}
		});
	}

//<==========================>
//<= Get election started	=>
//<= status from controller	=>
//===========================>
	public boolean getIsElectionStarted() {
		return allListeners.get(0).getIsElectionStarted();
	}

//<==========================>
//<= Get election ended		=>
//<= status from controller	=>
//===========================>
	public boolean getIsElectionEnded() {
		return allListeners.get(0).getIsElectionEnded();
	}

//===========================>
//<= Set buttons disable	=>
//<= by election status 	=>
//===========================>
	public void setMenuButtonsStatus() {
		if (getIsElectionStarted() && !getIsElectionEnded()) {
			btnMenu1.setDisable(true);
			btnMenu2.setDisable(true);
			btnMenu3.setDisable(true);
			btnMenu4.setDisable(true);
			btnMenu5.setDisable(true);
			btnMenu6.setDisable(true);
			btnMenu7.setDisable(true);
			btnMenu9.setDisable(true);
			bpRoot.setBottom(null);
		} else if (getIsElectionEnded()) {
			btnMenu1.setDisable(true);
			btnMenu2.setDisable(true);
			btnMenu3.setDisable(true);
			btnMenu4.setDisable(true);
			btnMenu5.setDisable(false);
			btnMenu6.setDisable(false);
			btnMenu7.setDisable(false);
			btnMenu8.setDisable(true);
			btnMenu9.setDisable(false);
			bpRoot.setBottom(null);
		}
		else {
			btnMenu9.setDisable(true);
		}
	}

//<======================================>
//<-------- Case 9: Print results ------->
//<= Get total results of votes			=>
//<= and results by ballots to display	=> 
//=======================================>
	public void ElectionResults() {

		Vector<String> partiesNames = allListeners.get(0).getPartiesNameList();
		Vector<Integer> results = allListeners.get(0).getResults();
		Vector<Ballot<Citizen>> allRegularBallots = allListeners.get(0).getAllRegularBallots();
		Vector<Ballot<Sick>> allCoronaBallots = allListeners.get(0).getAllCoronaBallots();
		Vector<Ballot<Soldier>> allMilitaryBallots = allListeners.get(0).getAllMilitaryBallots();
		Vector<Ballot<SickSoldier>> allCoronaMilitaryBallots = allListeners.get(0).getAllCoronaMilitaryBallots();

		tbCenter9 = new TabPane();

		HBox hbResultPerc = new HBox();
		GridPane gpResultPerc = new GridPane();
		hbResultPerc.getChildren().add(gpResultPerc);
		hbResultPerc.setAlignment(Pos.CENTER);
		gpResultPerc.setHgap(35);

		ObservableList<PieChart.Data> pcResultList = FXCollections.observableArrayList();

		String resultPerc;
		int numOfVotes = allListeners.get(0).getNumOfVotes();

		for (int i = 0; i < partiesNames.size(); i++) {
			pcResultList.add(new PieChart.Data(partiesNames.get(i), results.get(i)));
			Label lblPartyName = new Label(partiesNames.get(i));
			resultPerc = ((results.get(i) * 100) / numOfVotes) + "%";
			Label lblPartyResult = new Label(resultPerc);
			lblPartyName.setFont(Font.font("", 16));
			lblPartyResult.setFont(Font.font("", 16));
			gpResultPerc.add(lblPartyName, i * 2, 0);
			gpResultPerc.add(new Label("|"), (i * 2) + 1, 0);
			gpResultPerc.add(lblPartyResult, i * 2, 1);
			gpResultPerc.add(new Label("|"), (i * 2) + 1, 1);
		}

		PieChart pcResult = new PieChart(pcResultList);
		pcResult.setClockwise(true);
		pcResult.setLabelLineLength(45);
		pcResult.setLabelsVisible(true);
		pcResult.setStartAngle(180);
		pcResult.setMaxSize(650, 350);
		pcResult.setLabelLineLength(20);
		Group gpcResult = new Group(pcResult);

		GridPane gpTotalResults = new GridPane();
		gpTotalResults.setPadding(new Insets(10));
		gpTotalResults.setHgap(10);
		gpTotalResults.setVgap(10);
		gpTotalResults.add(hbResultPerc, 0, 0);
		gpTotalResults.add(gpcResult, 0, 1);

		FlowPane fpBallotsResults = new FlowPane();
		fpBallotsResults.setPrefSize(650, 0);
		fpBallotsResults.setPadding(new Insets(5));
		fpBallotsResults.setHgap(10);
		fpBallotsResults.setVgap(10);

		for (int i = 0; i < allRegularBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allRegularBallots.get(i).toStringShort());
			lblData.setText("Regular Ballot.\n" + allRegularBallots.get(i).toStringDetails());
			for (int j = 0; j < partiesNames.size(); j++) {
				lblData.setText(lblData.getText() + "\n" + partiesNames.get(j) + ": "
						+ allRegularBallots.get(i).getResults().get(j) + "/" + allRegularBallots.get(i).getNumOfVotes()
						+ " votes");
			}
			tpData.setContent(lblData);
			fpBallotsResults.getChildren().add(tpData);
		}
		for (int i = 0; i < allCoronaBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allCoronaBallots.get(i).toStringShort());
			lblData.setText("Corona Ballot.\n" + allCoronaBallots.get(i).toStringDetails());
			for (int j = 0; j < partiesNames.size(); j++) {
				lblData.setText(lblData.getText() + "\n" + partiesNames.get(j) + ": "
						+ allCoronaBallots.get(i).getResults().get(j) + "/" + allCoronaBallots.get(i).getNumOfVotes()
						+ " votes");
			}
			tpData.setContent(lblData);
			fpBallotsResults.getChildren().add(tpData);
		}
		for (int i = 0; i < allMilitaryBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allMilitaryBallots.get(i).toStringShort());
			lblData.setText("Military Ballot.\n" + allMilitaryBallots.get(i).toStringDetails());
			for (int j = 0; j < partiesNames.size(); j++) {
				lblData.setText(lblData.getText() + "\n" + partiesNames.get(j) + ": "
						+ allMilitaryBallots.get(i).getResults().get(j) + "/" + allMilitaryBallots.get(i).getNumOfVotes()
						+ " votes");
			}
			tpData.setContent(lblData);
			fpBallotsResults.getChildren().add(tpData);
		}
		for (int i = 0; i < allCoronaMilitaryBallots.size(); i++) {
			TitledPane tpData = new TitledPane();
			tpData.setMinSize(250, 30);
			tpData.setExpanded(false);
			Label lblData = new Label();
			tpData.setText(allCoronaMilitaryBallots.get(i).toStringShort());
			lblData.setText("Corona-Military Ballot.\n" + allCoronaMilitaryBallots.get(i).toStringDetails());
			for (int j = 0; j < partiesNames.size(); j++) {
				lblData.setText(lblData.getText() + "\n" + partiesNames.get(j) + ": "
						+ allCoronaMilitaryBallots.get(i).getResults().get(j) + "/" + allCoronaMilitaryBallots.get(i).getNumOfVotes()
						+ " votes");
			}
			tpData.setContent(lblData);
			fpBallotsResults.getChildren().add(tpData);
		}

		scrlpShowDetails.setContent(fpBallotsResults);
		scrlpShowDetails.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		Tab tab1 = new Tab("Total Results", gpTotalResults);
		Tab tab2 = new Tab("Ballots Results", scrlpShowDetails);
		tbCenter9.getTabs().add(tab1);
		tbCenter9.getTabs().add(tab2);
		tab1.setClosable(false);
		tab2.setClosable(false);
	}

//===========================================>
//<= Update controller election started	=>
//===========================================>	
	@Override
	public void updateElectionStarted() {
		setMenuButtonsStatus();
	}

//===========================================>
//<= Update controller about elector vote	=>
//===========================================>
	public void setElectorVote(String electorString, boolean fVote, String selectedParty) {
		for (ElectionUIListenable l : allListeners) {
			l.setElectorHadVote(electorString, fVote, selectedParty);
		}
	}

//=======================================>
//<= Update controller election ended	=>
//=======================================>	
	@Override
	public void updateElectionEnded() {
		setHeadLineDesign();
		setMenuButtonsStatus();
	}

//===============================================>
//<= Update from controller about elector vote	=>
//===============================================>
	public void updateElectorHadVote(String theVoter, boolean fVote, String nameOfParty) {
		for (ElectionUIListenable l : allListeners) {
			l.setElectorHadVote(theVoter, fVote, nameOfParty);
		}
	}

//===========================================>
//<= Remove elector from elections ComboBox	=>
//===========================================>
	public void removeVoter(String theVoter) {
		cmbAllElectors.getItems().remove(theVoter);
	}

//<======================>
//<--- Help Function ---->
//<= Show message: last	=>
//<= operation success	=> 
//=======================>
	public void messageSuccessOperation(String operation) {
		Label lblLastActionStatus = new Label(
				(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ": " + operation
						+ " has ended successfully."));
		lblLastActionStatus.setTextFill(COLOR_STATUS_OK);
		lstLogMessages.getItems().add(0, lblLastActionStatus);
	}

//<======================>
//<--- Help Function ---->
//<= Show message: last	=>
//<= operation failed	=> 
//=======================>
	public void messageFailedOperation(String operation, String errorMsg) {
		Label lblLastActionStatus = new Label(
				(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ": " + operation
						+ " has failed: " + errorMsg));
		lblLastActionStatus.setTextFill(COLOR_STATUS_ERROR);
		lstLogMessages.getItems().add(0, lblLastActionStatus);
	}

//<==================>
//<= Show message:	=>
//<= JOption Popup	=> 
//===================>
	public void messagePopup(String theText) {
		JOptionPane.showMessageDialog(null, theText);
	}
}