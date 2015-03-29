package newcontroller.handler;

public interface Handler {
    Rendered handleRequest(Request request, Response response);
}
