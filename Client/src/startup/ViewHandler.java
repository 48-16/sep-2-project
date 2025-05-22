package startup;

import dtos.user.UserDataDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.apointment.AppointmentClient;
import networking.product.ProductClient;
import networking.revenue.RevenueClient;
import networking.user.UserClient;
import state.AppState;
import ui.login.LoginViewControler;
import ui.login.LoginViewModel;
import ui.main.MainViewUserController;
import ui.main.MainViewUserModel;
import ui.main.MainAdminViewController;
import ui.main.MainViewAdminModel;
import ui.register.SignUpViewController;
import ui.register.SignUpViewModel;
import utils.ErrorPopUp;

import java.io.IOException;

/**
 * Manages view navigation and loading throughout the application.
 */
public class ViewHandler {

  private static Stage primaryStage;
  private static Scene currentScene;
  private static ViewHandler instance;

  private final ServiceFactory serviceFactory;
  private final ModelFactory modelFactory;
  private final ErrorPopUp errorPopUp;

  private ViewHandler(Stage primaryStage) {
    ViewHandler.primaryStage = primaryStage;
    this.serviceFactory = ServiceFactory.getInstance();
    this.modelFactory = ModelFactory.getInstance();
    this.errorPopUp = new ErrorPopUp();

    primaryStage.setTitle("Barbershop Management System");
    primaryStage.setOnCloseRequest(e -> System.exit(0));
  }

  public static ViewHandler init(Stage primaryStage) {
    if (instance == null) {
      instance = new ViewHandler(primaryStage);
    }
    return instance;
  }

  public static ViewHandler getInstance() {
    if (instance == null) {
      throw new RuntimeException("ViewHandler not initialized. Call init() first.");
    }
    return instance;
  }

  public static void show(ViewType viewToShow) {
    getInstance().openView(viewToShow);
  }

  public void openView(ViewType viewType) {
    try {
      Parent root = null;
      switch (viewType) {
        case LOGIN:
          root = loadLoginView();
          break;
        case REGISTER:
          root = loadRegisterView();
          break;
        case USER:
          root = loadUserView();
          break;
        case ADMIN:
          root = loadAdminView();
          break;
        default:
          throw new IllegalArgumentException("Unknown view: " + viewType);
      }

      if (currentScene == null) {
        currentScene = new Scene(root);
      } else {
        currentScene.setRoot(root);
      }

      primaryStage.setScene(currentScene);
      primaryStage.sizeToScene();
      primaryStage.show();
    } catch (IOException e) {
      errorPopUp.show("Error", "Could not load view: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private Parent loadLoginView() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/loginViewFXML.fxml"));
    LoginViewModel viewModel = modelFactory.getLoginViewModel();
    loader.setControllerFactory(param -> new LoginViewControler(viewModel, this));
    return loader.load();
  }

  private Parent loadRegisterView() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/register/signUpViewFXML.fxml"));
    SignUpViewModel viewModel = modelFactory.getSignUpViewModel();
    loader.setControllerFactory(param -> new SignUpViewController(viewModel, this));
    return loader.load();
  }

  private Parent loadUserView() throws IOException {
    UserDataDto currentUser = AppState.getCurrentUser();
    if (currentUser == null) {
      errorPopUp.show("Error", "You must be logged in to access this view");
      openView(ViewType.LOGIN);
      throw new IOException("User not logged in");
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/mainViewUserFXML.fxml"));

    AppointmentClient appointmentClient = serviceFactory.getAppointmentClient();
    ProductClient productClient = serviceFactory.getProductClient();
    RevenueClient revenueClient = serviceFactory.getRevenueClient();

    MainViewUserModel viewModel = new MainViewUserModel(
            appointmentClient,
            productClient,
            revenueClient,
            currentUser
    );
    loader.setControllerFactory(param -> new MainViewUserController(viewModel));
    return loader.load();
  }

  private Parent loadAdminView() throws IOException {
    UserDataDto currentUser = AppState.getCurrentUser();
    if (currentUser == null) {
      errorPopUp.show("Error", "You must be logged in to access this view");
      openView(ViewType.LOGIN);
      throw new IOException("User not logged in");
    }

    if (!"admin".equals(currentUser.getUserType())) {
      errorPopUp.show("Error", "You do not have permission to access the admin view");
      openView(ViewType.USER);
      throw new IOException("User is not an admin");
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/mainAdminViewFXML.fxml"));

    AppointmentClient appointmentClient = serviceFactory.getAppointmentClient();
    ProductClient productClient = serviceFactory.getProductClient();

    MainViewAdminModel viewModel = new MainViewAdminModel(
            appointmentClient,
            productClient
    );
    loader.setControllerFactory(param -> new MainAdminViewController(viewModel));
    return loader.load();
  }
}