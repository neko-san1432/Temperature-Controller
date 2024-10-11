package Prototype2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomLabel extends JLabel implements MouseListener {
    String focusedDay = "";
    public CustomLabel(String label){
        setText(label);
        setOpaque(true);
        setBackground(Color.white);
        addMouseListener(this);
        setFont(new Font("Calibre",Font.PLAIN,9));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        TemperatureManagement.day= getText();
        TemperatureManagement.month = calendar.getMonth();
        TemperatureManagement.year = calendar.getYear();
        focusedDay = calendar.getMonth()+getText()+calendar.getYear();
        if(TemperatureManagement.getSchedTime.containsKey(focusedDay)){
            SetTime.time.setText(TemperatureManagement.getSchedTime.get(focusedDay)[0] + ":" + TemperatureManagement.getSchedTime.get(focusedDay)[1]);
            SetTime.hour = TemperatureManagement.getSchedTime.get(focusedDay)[0];
            SetTime.minute = TemperatureManagement.getSchedTime.get(focusedDay)[1];
        }
        if(TemperatureManagement.getTemp.containsKey(focusedDay)){
            TemperatureManagement.textField.setText(String.valueOf(TemperatureManagement.getTemp.get(focusedDay)));
            SetTime.time.setText(TemperatureManagement.getSchedTime.get(focusedDay)[0] + ":" + TemperatureManagement.getSchedTime.get(focusedDay)[1]);
            SetTime.hour = TemperatureManagement.getSchedTime.get(focusedDay)[0];
            SetTime.minute = TemperatureManagement.getSchedTime.get(focusedDay)[1];
        }else {
            SetTime.hour = 0;
            SetTime.minute = 0;
            SetTime.time.setText("00:00");
            TemperatureManagement.textField.setText("");
        }
        TemperatureManagement.textField.setVisible(true);
        TemperatureManagement.set.setVisible(true);
        TemperatureManagement.uni.setVisible(true);
        TemperatureManagement.ts.setVisible(true);
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
