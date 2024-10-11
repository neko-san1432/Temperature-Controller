package Prototype2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class TemperatureManagement extends JFrame {
  static String un = " F";
  static JPanel ts = new SetTime(95, 100);
  static JTextField textField = new JTextField();
  static HashMap<String, Integer> getTemp = new HashMap<>();
  static HashMap<String,Integer[]>getSchedTime = new HashMap<>();
  static String day = "", month = "", year = "";
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  int width = 800, height = 600, tick = 0;
  JLabel[] rooms = {new JLabel("Room"), new JLabel("Kitchen"), new JLabel("Living Room")}, roomTem = {new JLabel(), new JLabel(), new JLabel()};
  JLabel outsideTem = new JLabel(),
  timeLabel = new JLabel("--:--:-- AM, --");
  JButton[] fan = {new JButton(), new JButton(), new JButton()};
  JLabel[] process = {new JLabel(), new JLabel(), new JLabel()};
  JButton unit = new JButton("F");
  String[] roomFan = {"Off", "Off", "Off"}, fanMode = {"--Standby--", "--Standby--", "--Standby--"};
  String lab = "Outside Temperature: ";
  float[] roomTemp = {96.8f, 96.8f, 96.8F};
  float outTemp = 96.8f;
  JSlider[] adjustTemperature = {new JSlider(33, 113, 96), new JSlider(33, 113, 96), new JSlider(33, 113, 96)};
  Timer[] temperatureTask = {new Timer(0, null), new Timer(0, null), new Timer(0, null)};
  Timer s, u;
  float[] preferredTemperatures = {96.8f, 96.8f, 96.8f};
  int[][] ticks = {{0, 0}, {0, 0}, {0, 0}};
  JLayeredPane jl = new JLayeredPane();
  JPanel p = new calendar(350, 300);
  //to be edited
  static JButton set = new JButton("set");
  String formattedDateTime = "";
  DecimalFormat tempTemperature = new DecimalFormat("##. #");
  String[] animation = {"Cooling", "Cooling.", "Cooling..", "Cooling...", "Heating", "Heating.", "Heating..", "Heating...", "Cooled", "Heated"};
  Timer[] temp = {new Timer(0, null), new Timer(0, null), new Timer(0, null)};
  JLabel[] as = {new JLabel("--"), new JLabel("--"), new JLabel("--")};
  static JLabel uni = new JLabel("F");
  JLabel errorMsg = new JLabel();
  Timer t;
  int tick1 = 0;

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
    float x = cel * 9;
    float y = x / 5;
    return y + 32;
  }

  private float convertToCel(float fah) {
    float x = fah - 32;
    float y = x * 5;
    return y / 9;
  }

  private void setCel() {
    un = " C";
    for (int i = 0; i < 3; i++) {
      roomTemp[i] = convertToCel(roomTemp[i]);
      roomTem[i].setText(tempTemperature.format(roomTemp[i]) + " C");
    }
    outTemp = convertToCel(outTemp);
    outsideTem.setText("Outside Temperature: " + tempTemperature.format(outTemp) + " C");
    for (int i = 0; i < adjustTemperature.length; i++) {
      if (preferredTemperatures[i] > adjustTemperature[i].getValue()) {
        preferredTemperatures[i] = convertToCel(preferredTemperatures[i]);
      } else {
        preferredTemperatures[i] = convertToCel(adjustTemperature[i].getValue());
      }
    }

    for (int i = 0; i < adjustTemperature.length; i++) {
      adjustTemperature[i].setValue((int) preferredTemperatures[i]);
    }
    for (int i = 0; i < adjustTemperature.length; i++) {
      adjustTemperature[i].setMinimum(0);
      adjustTemperature[i].setMaximum(46);
    }

  }

  private void setFar() {
    un = " F";
    for (int i = 0; i < 3; i++) {
      roomTemp[i] = convertToFar(roomTemp[i]);
      roomTem[i].setText(tempTemperature.format(roomTemp[i]) + " F");
    }
    outTemp = convertToFar(outTemp);
    outsideTem.setText("Outside Temperature: " + tempTemperature.format(outTemp) + " F");
    for (int i = 0; i < adjustTemperature.length; i++) {
      adjustTemperature[i].setMaximum(113);
      adjustTemperature[i].setMinimum(33);
    }
    for (int i = 0; i < adjustTemperature.length; i++) {
      if (preferredTemperatures[i] > adjustTemperature[i].getValue()) {
        preferredTemperatures[i] = convertToFar(preferredTemperatures[i]);
      } else {
        preferredTemperatures[i] = convertToFar(adjustTemperature[i].getValue());
      }
    }
    for (int i = 0; i < adjustTemperature.length; i++) {
      int t = (int) preferredTemperatures[i];
      adjustTemperature[i].setValue(t);
    }

  }

  private void startModifyTemperature() {
    s = new Timer(300000, _ -> {
      float x = weather.getWeather();
      if (x > -1) {
        if (un.equals(" F")) {
          outsideTem.setText(lab + tempTemperature.format(convertToCel(x)) + un);
        } else {
          outsideTem.setText(lab + tempTemperature.format(x) + un);
        }
      } else {
        outsideTem.setText(lab + "No internet connection");
      }
    });
    u = new Timer(1000, _ -> updateTime());
    s.start();
    u.start();
    for (int i = 0; i < rooms.length; i++) {
      adjustTemperature[i].setMajorTickSpacing(13);
      adjustTemperature[i].setPaintLabels(true);
      adjustTemperature[i].setPaintTicks(true);
    }
  }

  private void temperatureManipulation(int index) {
    if (un.equals(" F")) {
      if ((int) roomTemp[index] > adjustTemperature[index].getValue()) {
        roomTemp[index] -= 1;
        ticks[index][1] = 1;
      }
      if ((int) roomTemp[index] < adjustTemperature[index].getValue()) {
        roomTemp[index] += 1;
        ticks[index][1] = 2;
      }
    } else {
      if ((int) roomTemp[index] > adjustTemperature[index].getValue()) {
        roomTemp[index] -= 0.03f;
        ticks[index][1] = 1;
      }
      if ((int) roomTemp[index] < adjustTemperature[index].getValue()) {
        roomTemp[index] += 0.03f;
        ticks[index][1] = 2;
      }
    }
    if ((int) roomTemp[index] == adjustTemperature[index].getValue()) {
      System.out.println("asd");
      temperatureTask[index].stop();
      if (process[index].getText().equals("Heating") || process[index].getText().equals("Heating.") || process[index].getText().equals("Heating..") || process[index].getText().equals("Heating...")) {
        process[index].setText(animation[9]);
      } else {
        process[index].setText(animation[8]);
      }
    }
    roomTem[index].setText(tempTemperature.format(roomTemp[index]) + un);
  }

  private void modifyProcessStatus() {
    for (int i = 0; i < rooms.length; i++) {
      if (ticks[i][1] == 0) {
        process[i].setText("--Standby--");
        process[i].setBackground(Color.gray);
        process[i].setForeground(Color.white);
      } else if (ticks[i][1] == 1) {
        int f = i;
        int[] idx = {0};
        process[f].setText(animation[idx[0]]);
        if (temp[f].isRunning()) {
          temp[f].stop();
        }
        temp[f] = new Timer(1000, _ -> {
          if (idx[0] != 4) {
            process[f].setText(animation[idx[0]]);
            idx[0]++;
          } else {
            idx[0] = 0;
            process[f].setText(animation[idx[0]]);
          }
        });
        if (!temp[f].isRunning()) {
          temp[f].start();
        }
        process[i].setBackground(Color.cyan);
        process[i].setForeground(Color.black);
      } else {
        int f = i;
        int[] idx = {4};
        process[f].setText(animation[idx[0]]);
        if (temp[f].isRunning()) {
          temp[f].stop();
        }
        temp[f] = new Timer(1000, _ -> {
          if (idx[0] != 8) {
            process[f].setText(animation[idx[0]]);
            idx[0]++;
          } else {
            idx[0] = 4;
            process[f].setText(animation[idx[0]]);
          }
        });
        if (!temp[f].isRunning()) {
          temp[f].start();
        }
        process[i].setBackground(Color.red);
        process[i].setForeground(Color.white);
      }
      fanMode[i] = process[i].getText();
    }
  }

  private void set() {
    errorMsg.setSize(120,80);
    errorMsg.setLocation(525,520);
    add(errorMsg);
    jl.setSize(width, height);
    jl.setLayout(null);
    jl.setOpaque(true);
    jl.setBackground(Color.white);
    ts.setLocation(525, 420);
    ts.setVisible(false);
    p.setLocation(398, 100);
    p.setOpaque(true);
    p.setBackground(Color.white);
    jl.add(ts);
    jl.add(p);
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
      roomTem[i].setHorizontalAlignment(SwingConstants.CENTER);
      roomTem[i].setLocation((100 * i) + 10, 90);
      roomTem[i].setFont(new Font("Arial", Font.PLAIN, 9));
    }

    outsideTem.setSize(800, 50);
    outsideTem.setLocation(10, 0);
    outsideTem.setFont(new Font("Arial", Font.PLAIN, 20));
    timeLabel.setSize(400, 200);
    timeLabel.setLocation(10, 450);
    timeLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
    for (int i = 0; i < rooms.length; i++) {
      fan[i].setSize(100, 30);
      fan[i].setLocation((100 * i) + 10, 120);
      fan[i].setOpaque(true);
      fan[i].setBorderPainted(false);
      fan[i].setFocusable(false);
      fan[i].setBackground(Color.gray);
      process[i].setSize(100, 30);
      process[i].setLocation((100 * i) + 10, 160);
      process[i].setOpaque(true);
      process[i].setFocusable(false);
      process[i].setBackground(Color.gray);
      process[i].setHorizontalAlignment(SwingConstants.CENTER);
      process[i].setBorder(new LineBorder(Color.black, 1));
    }
    for (int i = 0; i < rooms.length; i++) {
      adjustTemperature[i].setOrientation(SwingConstants.VERTICAL);
      adjustTemperature[i].setSize(100, 300);
      adjustTemperature[i].setLocation((100 * i) + 10, 190);
      adjustTemperature[i].setFont(new Font("Arial", Font.PLAIN, 12));
      adjustTemperature[i].setOpaque(false);
      adjustTemperature[i].setBackground(Color.white);
      as[i].setSize(100, 10);
      as[i].setLocation((100 * i) + 10, 490);
      int finalI = i;
      adjustTemperature[i].addChangeListener(e -> {
        if (adjustTemperature[finalI].getValue() > (int) roomTemp[finalI]) {
          ticks[finalI][1] = 2;
          temperatureTask[finalI] = new Timer(10000, _ -> {
            temperatureManipulation(finalI);
          });
        }
        if (adjustTemperature[finalI].getValue() < (int) roomTemp[finalI]) {
          ticks[finalI][1] = 1;
          temperatureTask[finalI] = new Timer(5000, _ -> {
            temperatureManipulation(finalI);
          });
        }
        if (adjustTemperature[finalI].getValue() == (int) roomTemp[finalI]) {
          ticks[finalI][1] = 0;
          temperatureManipulation(finalI);
        }
        as[finalI].setText(String.valueOf(adjustTemperature[finalI].getValue()));
        modifyProcessStatus();
        temperatureTask[finalI].restart();
      });
      temperatureTask[i].start();
      jl.add(as[i]);
      adjustTemperature[i].addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
          preferredTemperatures[finalI] = adjustTemperature[finalI].getValue();
        }
      });
    }
    unit.setSize(100, 100);
    unit.setFocusable(false);
    unit.setLocation(getWidth() - 100, getHeight() - 100);
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
    textField.setSize(35, 20);
    uni.setSize(20,30);
    uni.setLocation(485, 410);
    textField.setLocation(450, 420);
    set.setSize(60, 40);
    set.setLocation(440, 445);
    textField.setVisible(false);
    uni.setVisible(false);
    set.setVisible(false);
    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar()) || textField.getText().length() > 2) {
          e.consume();
        }
      }
    });

    set.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (un.equals(" F")) {
          if (Integer.parseInt(textField.getText()) > 113 || Integer.parseInt(textField.getText()) < 33) {
            //error
            System.out.println("1");
            showError();
          } else {
            System.out.println("2");
            String[] t = SetTime.time.getText().split(":");
            getSchedTime.put((month + " " + day + " " + year),new Integer[]{ Integer.parseInt(t[0]),Integer.parseInt(t[1])});
            getTemp.put((month + " " + day + " " + year), Integer.parseInt(textField.getText()));
            textField.setVisible(false);
            set.setVisible(false);
            uni.setVisible(false);
            ts.setVisible(false);
          }
        } else {
          if (Integer.parseInt(textField.getText()) > 46 || Integer.parseInt(textField.getText()) < 0) {
            //error
            System.out.println("3");
            showError();
          } else {
            //ok
            System.out.println("4");
            String[] t = SetTime.time.getText().split(":");
            getSchedTime.put((month + " " + day + " " + year),new Integer[]{ Integer.parseInt(t[0]),Integer.parseInt(t[1])});
            getTemp.put((month + " " + day + " " + year), Integer.parseInt(textField.getText()));
            textField.setVisible(false);
            set.setVisible(false);
            uni.setVisible(false);
            ts.setVisible(false);
          }
        }
      }
    });
    jl.add(uni);
    jl.add(unit);
    jl.add(textField);
    jl.add(set);
    add(jl);
  }

  Color getColor1(String hex) {
    return new Color(Integer.valueOf(hex.substring(1, 3), 16),
    Integer.valueOf(hex.substring(3, 5), 16),
    Integer.valueOf(hex.substring(5, 7), 16));
  }

  private void showError() {
    String x;
    if (un.equals(" F")) {
      x = "<html>Temperature must be equal or<br/>greater than to 33 or less than<br/>or equal to 113</html>";
    } else {
      x = "<html>Temperature must be equal or<br/>greater than to 16 or less than<br/>or equal to 46</html>";
    }
    errorMsg.setText(x);
    showErr();
  }

  private void showErr() {
    if(t.isRunning()){
      t.restart();
      return;
    }
    t = new Timer(3000, _ -> {
      if (tick1 == 0) {
        tick1++;
        errorMsg.setVisible(true);
      } else {
        tick1--;
        errorMsg.setVisible(false);
        t.stop();
      }
    });
    t.start();
  }

  private void setTemps() {
    for (int i = 0; i < rooms.length; i++) {
      roomTem[i].setText(tempTemperature.format(roomTemp[i]) + " F");
    }
    outsideTem.setText(lab + tempTemperature.format(outTemp) + " F");
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

  private void updateTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a, EEEE");
    updateDateTime();
    timeLabel.setText("<html>" + sdf.format(Calendar.getInstance().getTime()) + "<br/>" + formattedDateTime + "</html>");
  }

  private void updateDateTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    formattedDateTime = now.format(formatter);
  }

}
