package networking.requestHandler;

public interface RequestHandler {
    boolean canHandle(Object request);
    Object handle(Object request);
}
