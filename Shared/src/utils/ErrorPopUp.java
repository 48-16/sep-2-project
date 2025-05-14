package utils;

import javafx.scene.control.Alert;

public class ErrorPopUp
{

  public void show(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    alert.showAndWait();
  }
  public void show(Exception e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("error");
    alert.setHeaderText(null);
    alert.setContentText(e.toString());

    alert.showAndWait();
  }
  public void showSucces(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    alert.showAndWait();
  }


}
