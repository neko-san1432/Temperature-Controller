package Prototype2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetTime extends JPanel {
  JButton w = new JButton("w"), x = new JButton("x"), y = new JButton("y"), z = new JButton("z");
  static JLabel time = new JLabel("00:00");
  static int hour = 0, minute = 0;

  public SetTime(int width, int height) {
    setOpaque(true);
    setLayout(null);
    setBackground(Color.white);
    setSize(width, height);
    settime();
    setw();
    sety();
    setx();
    setz();
  }

  private void setw() {
    w.setSize(45, 30);
    w.setLocation(0, 0);
    w.setFocusable(false);
    w.setContentAreaFilled(false);
    w.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (hour != 23) {
          hour++;
          update();
        }
      }
    });
    add(w);
  }

  private void setx() {
    x.setSize(45, 30);
    x.setLocation(50, 0);
    x.setFocusable(false);
    x.setContentAreaFilled(false);
    x.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (minute!=59) {
          minute++;
          update();
        }
      }
    });
    add(x);
  }

  private void sety() {
    y.setSize(45, 30);
    y.setLocation(0, 70);
    y.setFocusable(false);
    y.setContentAreaFilled(false);
    y.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if ( hour != 0) {
          hour--;
          update();
        }
      }
    });
    add(y);
  }

  private void setz() {
    z.setSize(45, 30);
    z.setLocation(50, 70);
    z.setFocusable(false);
    z.setContentAreaFilled(false);
    z.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (minute != 0 ) {
          minute--;
          update();
        }
      }
    });
    add(z);
  }
  private void settime(){
    time.setSize(100,40);
    time.setLocation(0,35);
    time.setFont(new Font("Calibri", Font.PLAIN,40));
    add(time);
  }

  private void update(){
    String a = String.valueOf(hour),b= String.valueOf(minute);
    if(a.length()==1){
      a = "0"+hour;
    }
    if(b.length()==1){
      b = "0"+minute;
    }
    time.setText(a + ":" + b);
  }
}
