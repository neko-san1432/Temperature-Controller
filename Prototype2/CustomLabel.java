package Prototype2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomLabel extends JLabel implements MouseListener {
    String focusedDay = "";
    public CustomLabel(String label/*,int horizontalAlignment*/){
        setText(label);
        setOpaque(true);
        setBackground(Color.white);
        addMouseListener(this);
        setFont(new Font("Calibre",Font.PLAIN,9));
//        setHorizontalAlignment(horizontalAlignment);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TemperatureManagement.ts.setVisible(true);
        TemperatureManagement.day= getText();
        TemperatureManagement.month = calendar.getMonth();
        TemperatureManagement.year = calendar.getYear();
        focusedDay = calendar.getMonth()+getText()+calendar.getYear();
        if(TemperatureManagement.getSchedTime.containsKey(focusedDay)){
            SetTime.time.setText(String.valueOf(TemperatureManagement.getSchedTime.get(focusedDay)));
        }
        if(TemperatureManagement.getTemp.containsKey(focusedDay)){
            TemperatureManagement.textField.setText(String.valueOf(TemperatureManagement.getTemp.get(focusedDay)));
        }else {
            TemperatureManagement.textField.setVisible(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
