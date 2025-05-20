//package ui.main;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.DatePicker;
//import utils.ErrorPopUp;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class mainAdminViewControler {
//    @FXML
//    private Button firstHour;
//    @FXML
//    private Button SecondHour;
//    @FXML
//    private Button ThirdHour;
//    @FXML
//    private Button FourthHour;
//    @FXML
//    private Button FifthHour;
//    @FXML
//    private Button SixthHour;
//    @FXML
//    private Button SeventhHour;
//    @FXML
//    private Button EightHour;
//    @FXML
//    private Button DeleteDay;
//    @FXML
//    private Button DeleteTime;
//    @FXML
//    private Button CancelApointment;
//    @FXML
//    private DatePicker datePicker;
//
//    private final mainViewAdminModel viewModel;
//    private Map<String, Button> timeButtons = new HashMap<>();
//    private String selectedTimeSlot = null;
//    private ErrorPopUp errorPopUp = new ErrorPopUp();
//
//    private static final String AVAILABLE_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white;";
//    private static final String BOOKED_STYLE = "-fx-background-color: #FFA500; -fx-text-fill: white;";
//    private static final String SELECTED_STYLE = "-fx-background-color: #2196F3; -fx-text-fill: white;";
//
//    public mainAdminViewControler(mainViewAdminModel viewModel) {
//        this.viewModel = viewModel;
//    }
//
//    public void initialize() {
//        timeButtons.put("8:00-9:00", firstHour);
//        timeButtons.put("9:00-10:00", SecondHour);
//        timeButtons.put("10:00-11:00", ThirdHour);
//        timeButtons.put("11:00-12:00", FourthHour);
//        timeButtons.put("12:00-13:00", FifthHour);
//        timeButtons.put("13:00-14:00", SixthHour);
//        timeButtons.put("14:00-15:00", SeventhHour);
//        timeButtons.put("15:00-16:00", EightHour);
//
//        datePicker.valueProperty().bindBidirectional(viewModel.getSelectedDateProperty());
//
//        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
//            if (newDate != null) {
//                updateTimeSlotButtons();
//                clearSelectedTimeSlot();
//            }
//        });
//
//        DeleteTime.setDisable(true);
//        CancelApointment.setDisable(true);
//    }
//
//    private void updateTimeSlotButtons() {
//        for (Map.Entry<String, Button> entry : timeButtons.entrySet()) {
//            String timeSlot = entry.getKey();
//            Button button = entry.getValue();
//
//            boolean isBooked = !viewModel.isTimeSlotAvailable(timeSlot);
//
//            if (isBooked) {
//                button.setStyle(BOOKED_STYLE);
//            } else {
//                button.setStyle(AVAILABLE_STYLE);
//            }
//
//            button.setDisable(false);
//        }
//    }
//
//    private void clearSelectedTimeSlot() {
//        selectedTimeSlot = null;
//        viewModel.setSelectedTimeSlot(null);
//        DeleteTime.setDisable(true);
//        CancelApointment.setDisable(true);
//
//        updateTimeSlotButtons();
//    }
//
//    private void selectTimeSlot(String timeSlot, Button button) {
//        if (selectedTimeSlot != null) {
//            // Reset previously selected button
//            Button prevButton = timeButtons.get(selectedTimeSlot);
//            if (!viewModel.isTimeSlotAvailable(selectedTimeSlot)) {
//                prevButton.setStyle(BOOKED_STYLE);
//            } else {
//                prevButton.setStyle(AVAILABLE_STYLE);
//            }
//        }
//
//        selectedTimeSlot = timeSlot;
//        viewModel.setSelectedTimeSlot(timeSlot);
//        button.setStyle(SELECTED_STYLE);
//
//        boolean isBooked = !viewModel.isTimeSlotAvailable(timeSlot);
//        DeleteTime.setDisable(!isBooked);
//        CancelApointment.setDisable(!isBooked);
//    }
//
//    @FXML
//    public void choosefirstHour() {
//        selectTimeSlot("8:00-9:00", firstHour);
//    }
//
//    @FXML
//    public void chooseSecondHour() {
//        selectTimeSlot("9:00-10:00", SecondHour);
//    }
//
//    @FXML
//    public void chooseThirdHour() {
//        selectTimeSlot("10:00-11:00", ThirdHour);
//    }
//
//    @FXML
//    public void chooseFourthHour() {
//        selectTimeSlot("11:00-12:00", FourthHour);
//    }
//
//    @FXML
//    public void chooseFifthHour() {
//        selectTimeSlot("12:00-13:00", FifthHour);
//    }
//
//    @FXML
//    public void chooseSixthHour() {
//        selectTimeSlot("13:00-14:00", SixthHour);
//    }
//
//    @FXML
//    public void chooseSeventhHour() {
//        selectTimeSlot("14:00-15:00", SeventhHour);
//    }
//
//    @FXML
//    public void chooseEightHour() {
//        selectTimeSlot("15:00-16:00", EightHour);
//    }
//
//    @FXML
//    public void DeleteDay() {
//        if (datePicker.getValue() == null) {
//            errorPopUp.show("Error", "Please select a date");
//            return;
//        }
//
//        boolean success = viewModel.deleteDay();
//        if (success) {
//            updateTimeSlotButtons();
//            clearSelectedTimeSlot();
//        }
//    }
//
//    @FXML
//    public void DeleteTime() {
//        if (selectedTimeSlot == null) {
//            errorPopUp.show("Error", "Please select a time slot");
//            return;
//        }
//
//        boolean success = viewModel.deleteTimeSlot();
//        if (success) {
//            updateTimeSlotButtons();
//            clearSelectedTimeSlot();
//        }
//    }
//
//    @FXML
//    public void DeleteApointment() {
//        if (selectedTimeSlot == null) {
//            errorPopUp.show("Error", "Please select a time slot");
//            return;
//        }
//
//        boolean success = viewModel.cancelAppointment();
//        if (success) {
//            updateTimeSlotButtons();
//            clearSelectedTimeSlot();
//        }
//    }
//}