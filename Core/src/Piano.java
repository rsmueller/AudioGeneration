import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;

public class Piano extends InstrumentHandler{

    private int velocity = 100;

    private int key = 0; //0 is key of C, adding and subtracting by one move the key center chromatically

    Piano() {
        name = "Piano";
    }

/*
    public void resetBend(){
        synth.resetBend(this);
    }
*/
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
        //TODO: Have loadouts designate a mouse dedicated instrument that then instantiates it and gives it the synth.
        //synth.setBend(this, (int) ((maxY - minY -(e.getY() - minY)) / (double) (maxY - minY) * 16383));
        int maxX = 492;
        int minX = 7;
        velocity = ((int) ((e.getX()- minX) /(double) (maxX - minX) * 117)) + 10;
        //System.out.println(((int) ((e.getX()-minX) /(double) (maxX - minX) * 90)) + 10);
    }
}
