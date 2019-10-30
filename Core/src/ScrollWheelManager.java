import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ScrollWheelManager implements MouseWheelListener{

    int change;

    public ScrollWheelManager(){

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //System.out.println(e.getWheelRotation());
    }
}
