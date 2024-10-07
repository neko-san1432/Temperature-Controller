package Prototype2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TemperatureManagement extends JFrame {
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  int width = 800, height = 600, tick = 0;
  JLabel[] rooms = {new JLabel("Room"), new JLabel("Kitchen"), new JLabel("Living Room")}, roomTem = {new JLabel(), new JLabel(), new JLabel()};
  JLabel outsideTem = new JLabel(), timeLabel = new JLabel("--:--:-- AM, --");
  JButton[] fan = {new JButton(), new JButton(), new JButton()}, process = {new JButton(), new JButton(), new JButton()};
  JButton unit = new JButton("F");
  String[] roomFan = {"Off", "Off", "Off"}, fanMode = {"--Standby--", "--Standby--", "--Standby--"};
  String lab = "Outside Temperature: ";
  float[] roomTempFar = {96.8f, 96.8f, 96.8F};
  float[] roomcelcius = {36, 36, 36};
  String un = " F";
  float outTemp = 96.8f;
  JSlider[] adjustTemperature = {new JSlider(33, 113, 96), new JSlider(33, 113, 96), new JSlider(33, 113, 96)};
  Timer r1, r2, r3, s, u;
  float[] preferredTemperatures = {96.8f, 96.8f, 96.8f};
  float[] preferredTemperaturescel = {36, 36, 36};
  int[][] ticks = {{0, 0}, {0, 0}, {0, 0}};
  JLayeredPane jl = new JLayeredPane();
  public TemperatureManagement() {
    setSize(width, height);
    setUndecorated(true);
    setResizable(false);
    setLocation((int) ((screenSize.getWidth() / 2) - 400), (int) ((screenSize.getHeight() / 2) - 300));
    setLayout(null);
    set();
    setTemps();
    setTickSettings();
    modifyFanStatus();
    modifyProcessStatus();
    startModifyTemperature();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new TemperatureManagement().setVisible(true));
  }

  private float convertToFar(float cel) {
    return ((cel * ((float) 9 / 5)) + 32);
  }

  private float convertToCel(float fah) {
    return (fah - 32) * ((float) 5 / 9);
  }

  private void setCel() {
    un = " C";
    for (int i = 0; i < 3; i++) {
      roomcelcius[i] = convertToCel(roomTempFar[i]);
      preferredTemperaturescel[i] = convertToCel(preferredTemperatures[i]);
      System.out.println(i+" "+preferredTemperaturescel[i]);
      roomTem[i].setText((int) roomcelcius[i] + " C");
    }
    outTemp = convertToCel(outTemp);
    outsideTem.setText("Outside Temperature: " + (int) outTemp + " C");
    for (int i = 0; i < adjustTemperature.length; i++) {
      adjustTemperature[i].setValue((int) preferredTemperaturescel[i]);
      adjustTemperature[i].setMaximum(46);
      adjustTemperature[i].setMinimum(0);
    }

  }

  private void setFar() {
    un = " F";
    for (int i = 0; i < 3; i++) {
      roomTempFar[i] = convertToFar(roomcelcius[i]);
      preferredTemperatures[i] = convertToFar(preferredTemperaturescel[i]);
      roomTem[i].setText((int) roomTempFar[i] + " F");
    }
    outTemp = convertToFar(outTemp);
    outsideTem.setText("Outside Temperature: " + (int) outTemp + " F");
    for (int i = 0; i < adjustTemperature.length; i++) {
      adjustTemperature[i].setValue((int) preferredTemperatures[i]);
      adjustTemperature[i].setMaximum(113);
      adjustTemperature[i].setMinimum(33);
    }

  }

  private void startModifyTemperature() {
    r1 = new Timer(60000, _ -> {
      float tmp = 0;
      if (un.equals(" F")) {
        if ((int) roomTempFar[0] > (int) preferredTemperatures[0]) {
          tmp = roomTempFar[0] -= 33.8f;
          ticks[0][1] = 1;
        }
        if ((int) roomTempFar[0] < (int) preferredTemperatures[0]) {
          tmp = roomTempFar[0] += 33.8f;
          ticks[0][1] = 2;
        }
        if ((int) roomTempFar[0] == (int) preferredTemperatures[0]) {
          ticks[0][1] = 0;
        }
      } else {
        if ((int) roomcelcius[0] > (int) preferredTemperaturescel[0]) {
          tmp = roomcelcius[0] -= 1;
          ticks[0][1] = 1;
        }
        if ((int) roomcelcius[0] < (int) preferredTemperaturescel[0]) {
          tmp = roomcelcius[0] += 1;
          ticks[0][1] = 2;
        }
        if ((int) roomcelcius[0] == (int) preferredTemperaturescel[0]) {
          ticks[0][1] = 0;
        }
      }
      roomTem[0].setText((int) tmp + un);
      modifyProcessStatus();
    });
    r2 = new Timer(60000, _ -> {
      float tmp = 0;
      if (un.equals(" F")) {
        if ((int) roomTempFar[1] > (int) preferredTemperatures[1]) {
          tmp = roomTempFar[1] -= 33.8f;
          ticks[1][1] = 1;
        }
        if ((int) roomTempFar[1] < (int) preferredTemperatures[1]) {
          tmp = roomTempFar[1] += 33.8f;
          ticks[1][1] = 2;
        }
        if ((int) roomTempFar[1] == (int) preferredTemperatures[1]) {
          ticks[1][1] = 0;
        }
      } else {
        if ((int) roomcelcius[1] > (int) preferredTemperaturescel[1]) {
          tmp = roomcelcius[1] -= 1;
          ticks[1][1] = 1;
        }
        if ((int) roomcelcius[1] < (int) preferredTemperaturescel[1]) {
          tmp = roomcelcius[1] += 1;
          ticks[1][1] = 2;
        }
        if ((int) roomcelcius[1] == (int) preferredTemperaturescel[1]) {
          ticks[1][1] = 0;
        }
      }
      roomTem[1].setText((int) tmp + un);
      modifyProcessStatus();
    });
    r3 = new Timer(60000, _ -> {
      float tmp = 0;
      if (un.equals(" F")) {
        if ((int) roomTempFar[2] > (int) preferredTemperatures[2]) {
          tmp = roomTempFar[2] -= 33.8f;
          ticks[2][1] = 1;
        }
        if ((int) roomTempFar[2] < (int) preferredTemperatures[2]) {
          tmp = roomTempFar[2] += 33.8f;
          ticks[2][1] = 2;
        }
        if ((int) roomTempFar[2] == (int) preferredTemperatures[2]) {
          ticks[2][1] = 0;
        }
      } else {
        if ((int) roomcelcius[2] > (int) preferredTemperaturescel[2]) {
          tmp = roomcelcius[2] -= 1;
          ticks[2][1] = 1;
        }
        if ((int) roomcelcius[2] < (int) preferredTemperaturescel[2]) {
          tmp = roomcelcius[2] += 1;
          ticks[2][1] = 2;
        }
        if ((int) roomcelcius[2] == (int) preferredTemperaturescel[2]) {
          ticks[2][1] = 0;
        }
      }
      roomTem[2].setText((int) tmp + un);
      modifyProcessStatus();
    });
    s = new Timer(300000, _ -> {
      int rand = (int) ((Math.random() * 9999) % 2);
      if (Objects.equals(un, " F")) {
        if (rand == 0) {
          outTemp += 33.8f;
        } else {
          outTemp -= 33.8f;
        }
      } else {
        if (rand == 0) {
          outTemp += 1;
        } else {
          outTemp -= 1;
        }
      }
      outsideTem.setText(lab + (int) outTemp + un);
    });
    u = new Timer(1000, _ -> updateTime());
    s.start();
    u.start();
    for (int i = 0; i < rooms.length; i++) {
      int finalI = i;
      adjustTemperature[i].addChangeListener(_ -> {
        if (Objects.equals(un, " F")) {
          preferredTemperatures[finalI] =convertToFar(adjustTemperature[finalI].getValue());
          if ((int) preferredTemperatures[finalI] > (int) roomTempFar[finalI]) {
            ticks[finalI][1] = 2;
          } else if ((int) preferredTemperatures[finalI] < (int) roomTempFar[finalI]) {
            ticks[finalI][1] = 1;
          } else if ((int) preferredTemperatures[finalI] == (int) roomTempFar[finalI]) {
            ticks[finalI][1] = 0;
          }
          modifyProcessStatus();
        } else {
          preferredTemperaturescel[finalI] = convertToCel(adjustTemperature[finalI].getValue());
          if ((int)preferredTemperaturescel[finalI] > (int)roomcelcius[finalI]) {
            ticks[finalI][1] = 2;
          } else if ((int)preferredTemperaturescel[finalI] < (int)roomcelcius[finalI]) {
            ticks[finalI][1] = 1;
          } else if ((int)preferredTemperaturescel[finalI] == (int)roomcelcius[finalI]) {
            ticks[finalI][1] = 0;
          }

        }
      });
      adjustTemperature[i].setMajorTickSpacing(13);
      adjustTemperature[i].setPaintLabels(true);
      adjustTemperature[i].setPaintTicks(true);
    }
  }

  private void set() {
    jl.setSize(width, height);
    jl.setLayout(null);
    jl.setOpaque(true);
    jl.setBackground(Color.white);
    for (int i = 0; i < rooms.length; i++) {
      JLabel room = rooms[i];
      room.setSize(100, 20);
      room.setLocation((100 * i) + 10, 80);
      room.setFont(new Font("Arial", Font.PLAIN, 13));
      room.setOpaque(true);
      rooms[i].setHorizontalTextPosition(JLabel.CENTER);
      rooms[i].setBackground(Color.white);
      room.setHorizontalAlignment(JLabel.CENTER);
      jl.add(room);
      roomTem[i].setSize(100, 30);
      roomTem[i].setHorizontalTextPosition(SwingConstants.CENTER);
      roomTem[i].setHorizontalAlignment(SwingConstants.CENTER);
      roomTem[i].setLocation((100 * i) + 10, 90);
      roomTem[i].setFont(new Font("Arial", Font.PLAIN, 9));

    }
    outsideTem.setSize(800, 50);
    outsideTem.setLocation(10, 0);
    outsideTem.setFont(new Font("Arial", Font.PLAIN, 20));
    timeLabel.setSize(500, 200);
    timeLabel.setLocation(350, 450);
    timeLabel.setHorizontalTextPosition(JLabel.RIGHT);
    timeLabel.setFont(new Font("Calibri", Font.PLAIN, 40));
    for (int i = 0; i < rooms.length; i++) {
      fan[i].setSize(100, 30);
      fan[i].setLocation((100 * i) + 10, 120);
      fan[i].setOpaque(true);
      fan[i].setBorderPainted(false);
      fan[i].setFocusable(false);
      fan[i].setBackground(Color.gray);
      fan[i].setBorder(new LineBorder(Color.black, 1));
      process[i].setSize(100, 30);
      process[i].setLocation((100 * i) + 10, 160);
      process[i].setOpaque(true);
      process[i].setBorderPainted(false);
      process[i].setFocusable(false);
      process[i].setBackground(Color.gray);
      process[i].setBorder(new LineBorder(Color.black, 1));
    }
    for (int i = 0; i < rooms.length; i++) {
      adjustTemperature[i].setOrientation(SwingConstants.VERTICAL);
      adjustTemperature[i].setSize(100, 300);
      adjustTemperature[i].setLocation((100 * i) + 10, 190);
      adjustTemperature[i].setFont(new Font("Arial", Font.PLAIN, 12));
      adjustTemperature[i].setOpaque(false);
      adjustTemperature[i].setBackground(Color.white);
    }
    unit.setSize(100, 100);
    unit.setFocusable(false);
    unit.setLocation(0, getHeight() - 100);
    unit.addActionListener(_ -> {
      if (tick == 0) {
        tick++;
        setCel();
        unit.setText("C");
      } else if (tick == 1) {
        tick--;
        unit.setText("F");
        setFar();
      }
    });
    for (int i = 0; i < rooms.length; i++) {
      jl.add(roomTem[i], JLayeredPane.DRAG_LAYER);
    }
    jl.add(outsideTem);
    jl.add(timeLabel);
    for (int i = 0; i < rooms.length; i++) {
      jl.add(fan[i]);
      jl.add(process[i]);
    }
    for (int i = 0; i < rooms.length; i++) {
      jl.add(adjustTemperature[i]);
    }
    jl.add(unit);
    add(jl);
  }

  Color getColor1() {
    return new Color(Integer.valueOf("#E0384A".substring(1, 3), 16),
    Integer.valueOf("#E0384A".substring(3, 5), 16),
    Integer.valueOf("#E0384A".substring(5, 7), 16));
  }

  private void setTemps() {
    for (int i = 0; i < rooms.length; i++) {
      roomTem[i].setText((int) roomTempFar[i] + " F");
    }
    outsideTem.setText(lab + (int) outTemp + " F");
  }


  void setTickSettings() {
    for (int i = 0; i < rooms.length; i++) {
      int finalI = i;
      fan[i].addActionListener(_ -> {
        ticks[finalI][0]++;
        if (ticks[finalI][0] == 3) {
          ticks[finalI][0] = 0;
        }
        modifyFanStatus();
      });
    }
  }

  private void modifyFanStatus() {
    for (int i = 0; i < rooms.length; i++) {
      if (ticks[i][0] == 0) {
        fan[i].setText("Off");
        fan[i].setBackground(Color.red);
        fan[i].setForeground(Color.white);
      } else if (ticks[i][0] == 1) {
        fan[i].setText("Automatic");
        fan[i].setBackground(Color.BLUE);
        fan[i].setForeground(Color.white);
      } else {
        fan[i].setText("On");
        fan[i].setBackground(Color.green);
        fan[i].setForeground(Color.black);
      }
      roomFan[i] = fan[i].getText();
    }
  }

  private void modifyProcessStatus() {
    for (int i = 0; i < rooms.length; i++) {
      if (ticks[i][1] == 0) {
        process[i].setText("--Standby--");
        process[i].setBackground(Color.gray);
        process[i].setForeground(Color.white);
      } else if (ticks[i][1] == 1) {
        process[i].setText("Cooling...");
        process[i].setBackground(Color.cyan);
        process[i].setForeground(Color.black);
      } else {
        process[i].setText("Heating...");
        process[i].setBackground(Color.red);
        process[i].setForeground(Color.white);
      }
      fanMode[i] = process[i].getText();
    }
  }

  private void updateTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a, EEEE");
    timeLabel.setText(sdf.format(Calendar.getInstance().getTime()));
  }
}
