package Prototype2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TemperatureManagement extends JFrame {
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  int width = 800, height = 600, tick =0 ;
  JLabel[] rooms = {new JLabel("Room"), new JLabel("Kitchen"), new JLabel("Living Room")};
  JLabel roomTem = new JLabel(), outsideTem = new JLabel(), timeLabel = new JLabel("--:--:-- AM, --");
  JButton fan = new JButton("--"), process = new JButton("--"),unit = new JButton("F");
  String[] roomFan = {"Off", "Off", "Off"}, fanMode = {"--Standby--", "--Standby--", "--Standby--"}, lab = {"Room Temperature: ", "Outside Temperature: "};
  JPanel jp = new JPanel();
  float[] roomTempFar = {96, 96, 96};
  float[] roomcelcius = {36,36,36};
  String un = " F";
  int index = 0, outTemp = 96;
  JSlider adjustTemperature = new JSlider(33, 113, 96);
  Timer r1,r2,r3, s, u;
  int[] preferredTemperatures = {96,96,96};
  int [][] ticks = {{0,0},{0,0},{0,0}};
  public TemperatureManagement() {
    setSize(width, height);
    setUndecorated(true);
    setResizable(false);
    setLocation((int) ((screenSize.getWidth() / 2) - 400), (int) ((screenSize.getHeight() / 2) - 300));
    setLayout(null);
    set();
    setTemps();
    setRooms();
    setTickSettings();
    startModifyTemperature();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new TemperatureManagement().setVisible(true));
  }

  private void startModifyTemperature() {
    r1 = new Timer(60000, _ -> {
      if (roomTempFar[0] > preferredTemperatures[0]) {
        roomTem.setText(lab[0] + roomTempFar[index]-- + un);
        roomTempFar[0] = roomTempFar[0]--;
      }
      if (roomTempFar[0] < preferredTemperatures[0]) {
        roomTem.setText(lab[0] + roomTempFar[0]++ + un);
        roomTempFar[0] = roomTempFar[0]++;
      }
    });
    r2 = new Timer(60000, _ -> {
      if (roomTempFar[1] > preferredTemperatures[1]) {
        roomTem.setText(lab[1] + roomTempFar[1]-- + un);
        roomTempFar[1] = roomTempFar[1]--;
      }
      if (roomTempFar[1] < preferredTemperatures[1]) {
        roomTem.setText(lab[1] + roomTempFar[1]++ + un);
        roomTempFar[1] = roomTempFar[1]++;
      }
    });
    r3 = new Timer(60000, _ -> {
      if (roomTempFar[2] > preferredTemperatures[2]) {
        roomTem.setText(lab[2] + roomTempFar[2]-- + un);
        roomTempFar[2] = roomTempFar[2]--;
      }
      if (roomTempFar[2] < preferredTemperatures[2]) {
        roomTem.setText(lab[2] + roomTempFar[2]++ + un);
        roomTempFar[2] = roomTempFar[2]++;
      }
    });

    s = new Timer(300000, _ -> {
      int rand = (int) ((Math.random() * 9999) % 2);
      if (rand == 0) {
        outTemp += 23;
      } else {
        outTemp -= 23;
      }
      outsideTem.setText(lab[1] + outTemp + un);
    });
    u = new Timer(1000, _ -> updateTime());
    adjustTemperature.addChangeListener(_ -> preferredTemperatures[index] = adjustTemperature.getValue());
    s.start();
    u.start();
    adjustTemperature.setMajorTickSpacing(13);
    adjustTemperature.setPaintLabels(true);
    adjustTemperature.setPaintTicks(true);
  }
  private float convertToFar(float cel){
    float x = (cel*(9/5));
    System.out.println(x);
    DecimalFormat s = new DecimalFormat("#.##");
    return  Float.parseFloat(s.format((x+32)));
  }
  private float convertToCel(float fah){
    DecimalFormat s = new DecimalFormat("#.##");
    return  Float.parseFloat(s.format((fah - 32) * 5 /9));
  }
  private void setCel(){
    for (int i = 0; i <3;i++){
      roomcelcius[i] = convertToCel( roomTempFar[i]);
    }
    roomTem.setText("Room Temperature: "+roomcelcius[index]+ " C");
    outsideTem.setText("Outside Temperature: " +convertToCel(outTemp) +" C");
    adjustTemperature.setMaximum(46);
    adjustTemperature.setMinimum(0);
    adjustTemperature.setValue((int) roomcelcius[index]);
    un = " C";
  }
  private void setFar(){
    for (int i = 0; i <3;i++){
      roomTempFar[i] = convertToFar( roomcelcius[i]);
    }
    roomTem.setText("Room Temperature: "+roomcelcius[index]+ " F");
    outsideTem.setText("Outside Temperature: " +convertToFar(outTemp) +" F");
    adjustTemperature.setMaximum(113);
    adjustTemperature.setMinimum(33);
    adjustTemperature.setValue((int) roomTempFar[index]);
    un = " F";
  }

  private void set() {
    jp.setSize(800, 600);
    jp.setOpaque(true);
    jp.setBackground(Color.getColor("#343434"));
    for (int i = 0; i < rooms.length; i++) {
      JLabel room = rooms[i];
      room.setSize(100, 20);
      room.setLocation((100*i)+250, 150);
      room.setFont(new Font("Arial",Font.PLAIN,13));
      room.setOpaque(true);
      rooms[i].setHorizontalTextPosition(JLabel.CENTER);
      room.setHorizontalAlignment(JLabel.CENTER);
      add(room);
    }
    roomTem.setSize(200, 30);
    roomTem.setLocation(330, 280);
    roomTem.setFont(new Font("Arial",Font.PLAIN,12));
    outsideTem.setSize(800, 50);
    outsideTem.setLocation(0  , 0);
    outsideTem.setFont(new Font("Arial",Font.PLAIN,20));
    timeLabel.setSize(500, 200);
    timeLabel.setLocation(350, 450);
    timeLabel.setHorizontalTextPosition(JLabel.RIGHT);
    timeLabel.setFont(new Font("Calibri", Font.PLAIN,40));
    fan.setSize(100, 30);
    fan.setLocation(250, 320);
    fan.setOpaque(true);
    fan.setBorderPainted(false);
    fan.setFocusable(false);
    fan.setBackground(Color.gray);
    process.setSize(100, 30);
    process.setLocation(450, 320);
    process.setOpaque(true);
    process.setBorderPainted(false);
    process.setFocusable(false);
    process.setBackground(Color.gray);
    fan.setEnabled(false);
    process.setEnabled(false);
    adjustTemperature.setEnabled(false);
    adjustTemperature.setSize(300, 100);
    adjustTemperature.setLocation(250 , 380);
    adjustTemperature.setFont(new Font("Arial",Font.PLAIN,12));
    unit.setSize(100,100);
    unit.setLocation(0,getHeight()-100);
    unit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(tick==0){
          tick++;
          setCel();
          unit.setText("C");
        }else if(tick ==1){
          tick--;
          unit.setText("F");
          setFar();
        }
      }
    });
    add(roomTem);
    add(outsideTem);
    add(timeLabel);
    add(fan);
    add(process);
    add(adjustTemperature);
    add(unit);
  }
  private void reEnableFunc(){
    fan.setEnabled(true);
    process.setEnabled(true);
    adjustTemperature.setEnabled(true);
  }
  private void setTemps() {
    roomTem.setText(lab[0] + roomTempFar[index] + " F");
    outsideTem.setText(lab[1] + outTemp + " F");
  }
  void clearFill(){
    for (JLabel room : rooms) {
      room.setBackground(null);
      room.setForeground(Color.black);
    }
  }
  private void setRooms() {
    rooms[0].addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        fan.setText(roomFan[0]);
        process.setText(fanMode[0]);
        index = 0;
        modifyFanStatus();
        modifyProcessStatus();
        clearFill();
        reEnableFunc();
        if(!r1.isRunning()){
        r1.start();}
        adjustTemperature.setValue(preferredTemperatures[index]);
      }
      @Override
      public void mouseEntered(MouseEvent e){
        rooms[0].setBackground(Color.black);
        rooms[0].setForeground(Color.white);
      }
      @Override
      public void mouseExited(MouseEvent e){
        rooms[0].setBackground(null);
        rooms[0].setForeground(Color.black);
      }
    });
    rooms[1].addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        fan.setText(roomFan[1]);
        process.setText(fanMode[1]);
        index = 1;
        modifyFanStatus();
        modifyProcessStatus();
        clearFill();
        reEnableFunc();
        if(!r2.isRunning()){
          r2.start();
        }
        adjustTemperature.setValue(preferredTemperatures[index]);
      }
      @Override
      public void mouseEntered(MouseEvent e){
        rooms[1].setBackground(Color.black);
        rooms[1].setForeground(Color.white);
      }
      @Override
      public void mouseExited(MouseEvent e){
        rooms[1].setBackground(null);
        rooms[1].setForeground(Color.black);
      }
    });
    rooms[2].addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        fan.setText(roomFan[2]);
        process.setText(fanMode[2]);
        index = 2;
        modifyFanStatus();
        modifyProcessStatus();
        clearFill();
        reEnableFunc();
        if(!r2.isRunning()){
        r3.start();}
        adjustTemperature.setValue(preferredTemperatures[index]);
      }
      @Override
      public void mouseEntered(MouseEvent e){
        rooms[2].setBackground(Color.black);
        rooms[2].setForeground(Color.white);
      }
      @Override
      public void mouseExited(MouseEvent e){
        rooms[2].setBackground(null);
        rooms[2].setForeground(Color.black);
      }
    });
  }

  void setTickSettings() {
    fan.addActionListener(_ -> {
      ticks[index][0]++;
      if (ticks[index][0] == 3) {
        ticks[index][0] = 0;
      }
      modifyFanStatus();
    });
    process.addActionListener(_ -> {
      ticks[index][1]++;
      if (ticks[index][1] == 3) {
        ticks[index][1] = 0;
      }
      modifyProcessStatus();
    });
  }

  private void modifyFanStatus() {
    if (ticks[index][0] == 0) {
      fan.setText("Off");
      fan.setBackground(Color.red);
      fan.setForeground(Color.white);
    } else if (ticks[index][0] == 1) {
      fan.setText("Automatic");
      fan.setBackground(Color.BLUE);
      fan.setForeground(Color.white);
    } else {
      fan.setText("On");
      fan.setBackground(Color.green);
      fan.setForeground(Color.black);
    }
    roomFan[index] = fan.getText();
  }

  private void modifyProcessStatus() {
    if (ticks[index][1] == 0) {
      process.setText("--Standby--");
      process.setBackground(Color.gray);
      process.setForeground(Color.white);
    } else if (ticks[index][1] == 1) {
      process.setText("Cooling...");
      process.setBackground(Color.cyan);
      process.setForeground(Color.black);
    } else {
      process.setText("Heating...");
      process.setBackground(Color.red);
      process.setForeground(Color.white);
    }
    fanMode[index] = process.getText();
  }

  private void updateTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a, EEEE");
    timeLabel.setText(sdf.format(Calendar.getInstance().getTime()));
  }
}
