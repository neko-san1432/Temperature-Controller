package Prototype2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetTime extends JFrame {
  JButton w = new JButton("w"), x = new JButton("x"), y = new JButton("y"), z = new JButton("z");
  JLabel time = new JLabel("00:00");
  String t = "";
  int hour = 0, minute = 0;

  public SetTime(int width, int height) {
//    setOpaque(true);
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
    w.setSize(200, 100);
    w.setLocation(50, 0);
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
    x.setSize(200, 100);
    x.setLocation(200, 0);
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
    y.setSize(200, 100);
    y.setLocation(50, 300);
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
    z.setSize(200, 100);
    z.setLocation(200, 300);
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
    time.setSize(300,200);
    time.setLocation(150,0);
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
  public static void main(String[] args) {
    SwingUtilities.invokeLater(()->new SetTime(100,100).setVisible(true));
  }
}
