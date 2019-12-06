import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;

public class Piano extends InstrumentHandler {

    private int key = 0; //0 is key of C, adding and subtracting by one move the key center chromatically

    //Cant have anything passed to constructor.
    public Piano() {
        name = "Piano";
        velocity = 100;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /*int sign = e.getWheelRotation()/Math.abs(e.getWheelRotation());
        synth.bend(this, (int)(Math.pow(e.getWheelRotation(), 2)*-50) * sign); //this is where you can change the rate at which the bend is changed*/
    } // not a good way to do it, so its replaced with the mouse position method

    @Override
    public void mouseDragged(MouseEvent e) { //not necessary at the moment

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int maxY = 992;
        int minY = 30;
        synth.setBend((int) ((maxY - minY -(e.getY() - minY)) / (double) (maxY - minY) * 16383));
        int maxX = 492;
        int minX = 7;
        velocity = ((int) ((e.getX()- minX) /(double) (maxX - minX) * 117)) + 10;
        System.out.println(((int) ((e.getX()-minX) /(double) (maxX - minX) * 90)) + 10);
    }

}
