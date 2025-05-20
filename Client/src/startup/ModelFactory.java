package startup;

import networking.authentication.AuthenticationClient;
import networking.authentication.SocketAuthenticationClient;
import ui.login.LoginViewModel;
import ui.register.SignUpViewModel;

/**
 * Factory for creating view models with their required dependencies.
 * This centralizes the creation of view models and ensures consistent dependency injection.
 */
public class ModelFactory {

    private static ModelFactory instance;
    private final ServiceFactory serviceFactory;

    // View models - lazily initialized
    private LoginViewModel loginViewModel;
    private SignUpViewModel signUpViewModel;

    private ModelFactory() {
        this.serviceFactory = ServiceFactory.getInstance();
    }

    public static ModelFactory getInstance() {
        if (instance == null) {
            instance = new ModelFactory();
        }
        return instance;
    }

    public LoginViewModel getLoginViewModel() {
        if (loginViewModel == null) {
            AuthenticationClient authClient = serviceFactory.getAuthenticationClient();
            loginViewModel = new LoginViewModel((SocketAuthenticationClient) authClient);
        }
        return loginViewModel;
    }

    public SignUpViewModel getSignUpViewModel() {
        if (signUpViewModel == null) {
            AuthenticationClient authClient = serviceFactory.getAuthenticationClient();
            signUpViewModel = new SignUpViewModel((SocketAuthenticationClient) authClient);
        }
        return signUpViewModel;
    }
}