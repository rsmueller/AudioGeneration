import java.awt.event.MouseWheelEvent;

public interface ScrollWheelListener {
    int scrollUp(MouseWheelEvent e);
    int scrollDown(MouseWheelEvent e);
}
