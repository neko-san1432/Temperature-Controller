package Prototype1;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ValveManagement extends JFrame {
  static JLabel[] statusLabel = {new MyLabel("Off"), new MyLabel("Slightly On"), new MyLabel("Mostly On"), new MyLabel("All the way On")};
  static JLabel preferredStatus = new JLabel("Set status: " + Valve.status);
  static JSlider j = new NumericSetUpStatus();
  static JTextField jt = new NumericSetUpStatus.ManualInput();
  static int valveNumber = 0;
  //Components
  static LinkedList<JLabel> x = new LinkedList<>(), valves = new LinkedList<>();
  static LinkedList<Point> valveLocation = new LinkedList<>();
  static HashMap<String, Integer> valveIndex = new HashMap<>();
  static LinkedList<String> valveStat = new LinkedList<>();
  static JLabel valveName = new JLabel("Valve # --");
  static Valve focusedValve = null;
  static HashMap<String, Point> origPosStat = new HashMap<>();
  static String stat = "Status: ", valvE = "Valve #";
  static JLabel status = new JLabel("Status: --");
  static JButton set = new JButton("Set");
  static JButton deleteValve = new JButton("Delete Valves");
  static JPanel statusShortcut = new JPanel();
  static JLabel dropdown2 = new JLabel("<<");
  static ArrayList<Integer> valveOpeningValue = new ArrayList<>();
  static KeyEvent kEy = null;
  static MouseEvent mEv = null;
  static boolean shiftOn = false;
  static int tick2 = 0;
  JButton close = new JButton("x"), minimize = new JButton("-"), addValve = new JButton("Add Valve");
  JPanel valveArea = new JPanel();
  JLayeredPane jlp = new JLayeredPane();
  JPanel controlPanel = new JPanel();
  JLabel dropdown1 = new JLabel("vv");
  //Data
  Dimension d = getToolkit().getScreenSize();
  Point center = new Point((int) (d.getWidth() / 2) - 350, (int) (d.getHeight() / 2) - 350);
  ArrayList<Point> statusOriginalPosition = new ArrayList<>();
  int mouseX, mouseY, tick1 = 0;

  public ValveManagement() {
    setFrame();
    setLayeredPane();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new ValveManagement().setVisible(true));
  }

  static void setDisableComponent(boolean enable) {
    for (JLabel jLabel : statusLabel) {
      jLabel.setVisible(enable);
      jLabel.setEnabled(enable);
    }
    dropdown2.setLocation(700 - 155, 100);
    statusShortcut.setVisible(enable);
    j.setEnabled(enable);
    jt.setEnabled(enable);
    jt.setText("--");
    set.setEnabled(enable);
    deleteValve.setVisible(enable);
    if (!enable) {
      valveName.setText("Valve # --");
      status.setText("Status: --");
      tick2 = 0;
      dropdown2.setText("<<");
      dropdown2.setLocation(700 - 33, 100);
    } else {
      tick2 = 1;
      dropdown2.setText(">>");
    }

  }

  static Color getColor1() {
    return new Color(Integer.valueOf("#E0384A".substring(1, 3), 16),
    Integer.valueOf("#E0384A".substring(3, 5), 16),
    Integer.valueOf("#E0384A".substring(5, 7), 16));
  }

  private void setFrame() {
    //add the layered panel
    setFocusable(true);
    setSize(700, 700);
    setLocation(center);
    setUndecorated(true);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
      }

    });

    addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        int x = e.getXOnScreen();
        int y = e.getYOnScreen();
        if ((mouseY <= 40 && mouseY >= 0) && (mouseX <= 700 && mouseX >= 0)) {
          setLocation(x - mouseX, y - mouseY);
        }
      }

    });
    add(jlp);
  }

  private void setLayeredPane() {
    //add all components
    jlp.setLayout(null);
    jlp.setOpaque(false);
    setCloseButton();
    setMinimizeButton();
    setStatuses();
    setDropdown2();
    setDropdown1();
    setAddValve();
    setValveName();
    setSlider();
    setDeleteValve();
    setJt();
    setValveStatus();
    setPreferredStatus();
    setSet();
    setControlPanel();
    setValveArea();
  }

  private void setCloseButton() {
    close.setSize(40, 40);
    close.setLocation((int) (getSize().getWidth() - 40), 0);
    close.setBorder(new LineBorder(Color.black, 1));
    close.setFocusable(false);
    close.addActionListener(_ -> System.exit(0));
    jlp.add(close, JLayeredPane.DRAG_LAYER);
  }

  private void setMinimizeButton() {
    minimize.setSize(40, 40);
    minimize.setLocation((int) (getSize().getWidth() - 80), 0);
    minimize.setBorder(new LineBorder(Color.black, 1));
    minimize.setFocusable(false);
    minimize.addActionListener(_ -> setState(Frame.ICONIFIED));
    jlp.add(minimize, JLayeredPane.DRAG_LAYER);
  }

  private void setStatuses() {
    statusShortcut.setSize(120, 200);
    statusShortcut.setLocation(getWidth() - 120, 50);
    statusShortcut.setOpaque(true);
    statusShortcut.setVisible(false);
    statusShortcut.setBackground(Color.white);
    statusShortcut.setBorder(new LineBorder(Color.black, 1));
    statusShortcut.setLayout(null);
    for (int i = 0, statusLabelLength = statusLabel.length; i < statusLabelLength; i++) {
      JLabel jLabel = statusLabel[i];
      jLabel.setSize(100, 30);
      jLabel.setOpaque(true);
      jLabel.setHorizontalAlignment(JLabel.CENTER);
      jLabel.setVerticalAlignment(JLabel.CENTER);
      jLabel.setLocation(getWidth() - 110, (50 * i) + 63);
      jLabel.setVisible(false);
      jLabel.setEnabled(false);
      statusOriginalPosition.add(jLabel.getLocation());
      origPosStat.put(jLabel.getText(), jLabel.getLocation());
      switch (i) {
        case 0:
          jLabel.setBackground(getColor("#E0384A"));
          break;
        case 1:
          jLabel.setBackground(getColor("#289AE0"));
          break;
        case 2:
          jLabel.setBackground(getColor("#E1D246"));
          break;
        case 3:
          jLabel.setBackground(getColor("#60EB62"));
          break;
      }
      jlp.add(jLabel, JLayeredPane.DRAG_LAYER);
    }
    jlp.add(statusShortcut, JLayeredPane.POPUP_LAYER);
  }

  Color getColor(String hexCode) {
    return new Color(Integer.valueOf(hexCode.substring(1, 3), 16),
    Integer.valueOf(hexCode.substring(3, 5), 16),
    Integer.valueOf(hexCode.substring(5, 7), 16));
  }

  private void setDropdown2() {
    dropdown2.setSize(40, 20);
    dropdown2.setLocation(getWidth() - 33, 100);
    dropdown2.setBorder(new RoundedBorder(10));
    dropdown2.setOpaque(true);
    dropdown2.setVerticalAlignment(JLabel.CENTER);
    dropdown2.setHorizontalAlignment(JLabel.CENTER);
    dropdown2.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (tick2 == 0) {
          statusShortcut.setVisible(true);
          for (JLabel jLabel : statusLabel) {
            jLabel.setVisible(true);
          }
          dropdown2.setText(">>");
          dropdown2.setLocation(getWidth() - 155, 100);
          tick2++;
        } else if (tick2 == 1) {
          statusShortcut.setVisible(false);
          for (JLabel jLabel : statusLabel) {
            jLabel.setVisible(false);
          }
          dropdown2.setText("<<");
          dropdown2.setLocation(getWidth() - 33, 100);
          tick2--;
        }
      }
    });
    jlp.add(dropdown2, JLayeredPane.MODAL_LAYER);
  }

  private void setValveArea() {
    valveArea.setSize(700, 700);
    valveArea.setLocation(0, 0);
    valveArea.setBackground(getColor("#c0c0c0"));
    valveArea.setLayout(null);
    valveArea.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        focusedValve = null;
        status.setText("Status: --");
        preferredStatus.setText("Set status: --");
        setDisableComponent(false);
      }
    });
    jlp.add(valveArea, JLayeredPane.DEFAULT_LAYER);
  }

  private void setAddValve() {
    addValve.setSize(100, 30);
    addValve.setLocation(10, 50);
    addValve.addActionListener(_ -> {
      x.add(new StatusIndicator());
      valves.add(new Valve());
      valveOpeningValue.add(0);
      valves.getLast().setLocation(350, 350);
      valveStat.add("Off");
      valveLocation.add(valves.getLast().getLocation());
      valveIndex.put(valves.getLast().getText(), valveNumber);
      valveArea.add(valves.getLast());
      valveNumber++;
      repaint();
    });
    jlp.add(addValve, JLayeredPane.DRAG_LAYER);
  }

  private void setDeleteValve() {
    deleteValve.setSize(100, 30);
    deleteValve.setLocation(10, 90);
    deleteValve.setVisible(false);
    deleteValve.addActionListener(_ -> valveNumber--);
    jlp.add(deleteValve, JLayeredPane.POPUP_LAYER);
  }

  private void setControlPanel() {
    controlPanel.setSize(700, 180);
    controlPanel.setLocation(0, getHeight() - 180);
    controlPanel.setOpaque(true);
    controlPanel.setBorder(new LineBorder(Color.black));
    controlPanel.setBackground(Color.white);
    controlPanel.setLayout(null);
    jlp.add(controlPanel, JLayeredPane.DRAG_LAYER);
  }

  private void setDropdown1() {
    dropdown1.setSize(40, 30);
    dropdown1.setLocation(60, getHeight() - 205);
    dropdown1.setOpaque(true);
    dropdown1.setBorder(new RoundedBorder(10));
    dropdown1.setBackground(Color.white);
    dropdown1.setVerticalAlignment(JLabel.CENTER);
    dropdown1.setHorizontalAlignment(JLabel.CENTER);
    dropdown1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (tick1 == 0) {
          dropdown1.setText("^^");
          dropdown1.setLocation(60, getHeight() - 20);
          controlPanel.setVisible(false);
          tick1++;
        } else if (tick1 == 1) {
          dropdown1.setText("vv");
          dropdown1.setLocation(60, getHeight() - 205);
          controlPanel.setVisible(true);
          tick1--;
        }
      }
    });
    jlp.add(dropdown1, JLayeredPane.MODAL_LAYER);
  }

  private void setValveName() {
    valveName.setSize(200, 50);
    valveName.setLocation((getWidth() / 2) - 100, 10);
    valveName.setHorizontalAlignment(JLabel.CENTER);
    valveName.setVerticalAlignment(JLabel.CENTER);
    valveName.setFont(new Font("Arial", Font.PLAIN, 30));
    controlPanel.add(valveName, JLayeredPane.DRAG_LAYER);
  }

  private void setSlider() {
    j.setLocation(getWidth() - 230, 30);
    j.setOpaque(true);
    j.setBackground(Color.white);
    j.setEnabled(false);
    controlPanel.add(j);
  }

  private void setJt() {
    jt.setLocation(getHeight() - 60, 40);
    jt.setText("--");
    jt.setEnabled(false);
    controlPanel.add(jt);
  }

  private void setSet() {
    set.setSize(60, 30);
    set.setLocation(preferredStatus.getX() + 35, preferredStatus.getY() + 45);
    set.addActionListener(_ -> {
      valveStat.set(valveIndex.get(focusedValve.getText()), getStatus());
      x.get(valveIndex.get(focusedValve.getText())).setBackground(getSliderVal());
      valveOpeningValue.set(valveIndex.get(focusedValve.getText()), j.getValue());
      status.setText("Status: " + valveStat.set(valveIndex.get(focusedValve.getText()), getStatus()));
    });
    set.setEnabled(false);
    controlPanel.add(set);
  }

  private String getStatus() {
    int value = j.getValue();
    if (value >= 0 && value <= 349) {
      return "Off";
    } else if (value >= 350 && value <= 649) {
      return "Slightly On";
    } else if (value >= 650 && value <= 999) {
      return "Mostly On";
    } else if (value == 1000) {
      return "All the way On";
    } else {
      return "";
    }
  }

  private Color getSliderVal() {
    int value = j.getValue();
    if (value >= 0 && value <= 349) {
      return getColor("#E0384A");
    } else if (value >= 350 && value <= 649) {
      return getColor("#289AE0");
    } else if (value >= 650 && value <= 999) {
      return getColor("#E1D246");
    } else if (value == 1000) {
      return getColor("#60EB62");
    } else {
      return null;
    }
  }

  private void setPreferredStatus() {
    preferredStatus.setSize(200, 50);
    preferredStatus.setLocation(getWidth() - 210, 70);
    controlPanel.add(preferredStatus);
  }

  private void setValveStatus() {
    status.setSize(200, 50);
    status.setLocation(20, 50);
    status.setOpaque(true);
    status.setFont(new Font("Arial", Font.PLAIN, 18));
    controlPanel.add(status);
  }

  private static class Valve extends JLabel implements MouseListener, MouseMotionListener, KeyListener {
    static String status = "";

    public Valve() {
      setSize(50, 30);
      setOpaque(true);
      setHorizontalTextPosition(JLabel.CENTER);
      setVerticalTextPosition(JLabel.CENTER);
      addMouseListener(this);
      addMouseMotionListener(this);
      addKeyListener(this);
      setText(String.valueOf(valveNumber + 1));
      setBorder(new LineBorder(Color.black, 1));
      setBackground(Color.white);
      setStatusIndicator();
    }
    static void setT(String t){
      status = t;
    }
    Color getColor(String hexCode) {
      return new Color(Integer.valueOf(hexCode.substring(1, 3), 16),
      Integer.valueOf(hexCode.substring(3, 5), 16),
      Integer.valueOf(hexCode.substring(5, 7), 16));
    }

    void func1(MouseEvent mE) {
      if (mE == null) {
        return;
      }
      if ((SwingUtilities.isRightMouseButton(mE) && !shiftOn) && statusLabel[0].isVisible()) {
        valveOpeningValue.set(valveIndex.get(getText()), 1000);
        valveStat.set(valveIndex.get(getText()), "All the way On");
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(getText())));
        status = valveStat.get(valveIndex.get(getText()));
        x.get(valveIndex.get(getText())).setBackground(getColor("#60EB62"));
      }
      kEy = null;
      mEv = null;
      repaint();
    }

    void func2(MouseEvent mE) {
      if (mE == null) {
        return;
      }
      if (SwingUtilities.isLeftMouseButton(mE) && !shiftOn) {
        if (!statusShortcut.isVisible()) {
          for (JLabel jLabel : statusLabel) {
            jLabel.setVisible(true);
          }
          statusShortcut.setVisible(true);
          dropdown2.setText(">>");
          dropdown2.setLocation(getWidth() - 155, 100);
        }
      }
      kEy = null;
      mEv = null;
    }

    void func3(MouseEvent mE) {
      if (mE == null) {
        return;
      }
      if ((SwingUtilities.isMiddleMouseButton(mE) && !shiftOn) && statusLabel[0].isVisible()) {
        valveOpeningValue.set(valveIndex.get(getText()), 0);
        x.get(valveIndex.get(getText())).setBackground(getColor("#E0384A"));
        valveStat.set(valveIndex.get(getText()), "Off");
        status = valveStat.get(valveIndex.get(getText()));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(getText())));
        j.setValue(0);
      }
      kEy = null;
      mEv = null;
    }

    void func4(KeyEvent kE, MouseEvent mE) {
      if (kE == null || mE == null) {
        return;
      }
      if (((kE.getKeyCode() == KeyEvent.VK_SHIFT && shiftOn) && SwingUtilities.isLeftMouseButton(mE)) && statusLabel[0].isVisible()) {
        valveOpeningValue.set(valveIndex.get(getText()), 650);
        x.get(valveIndex.get(getText())).setBackground(getColor("#289AE0"));
        valveStat.set(valveIndex.get(getText()), "Mostly On");
        status = valveStat.get(valveIndex.get(getText()));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(getText())));
        j.setValue(650);
      }
      kEy = null;
      mEv = null;
    }

    void func5(KeyEvent kE, MouseEvent mE) {
      if (kE == null || mE == null) {
        return;
      }
      if ((kE.getKeyCode() == KeyEvent.VK_SHIFT && shiftOn) && SwingUtilities.isMiddleMouseButton(mE) && statusLabel[0].isVisible()) {
        valveOpeningValue.set(valveIndex.get(getText()), 350);
        x.get(valveIndex.get(getText())).setBackground(getColor("#E1D246"));
        valveStat.set(valveIndex.get(getText()), "Slightly On");
        status = valveStat.get(valveIndex.get(getText()));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(getText())));
        j.setValue(350);
      }
      kEy = null;
      mEv = null;
    }

    void callFunc(KeyEvent k, MouseEvent m) {
      func1(m);
      func2(m);
      func3(m);
      func4(k, m);
      func5(k, m);
      System.out.println(valveIndex.get(getText()));
      x.get(valveNumber - 1).revalidate();
      this.revalidate();
      revalidate();
      x.get(valveNumber - 1).repaint();
      this.repaint();
      repaint();

    }

    private void setStatusIndicator() {
      System.out.println(valveNumber);
      add(x.get(valveNumber));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (SwingUtilities.isLeftMouseButton(e)) {
        setDisableComponent(true);
        j.setEnabled(true);
        jt.setText(String.valueOf(valveOpeningValue.get(valveIndex.get(getText()))));
        preferredStatus.setText("Set status: " + valveStat.get(valveIndex.get(getText())));
        valveName.setText(valvE + getText());
        ValveManagement.status.setText(stat + valveStat.get(valveIndex.get((getText()))));
        focusedValve = this;
        deleteValve.setEnabled(true);
        mEv = e;
        this.requestFocus();
        callFunc(kEy, mEv);
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      kEy = e;
      shiftOn = true;
      callFunc(kEy, mEv);
    }

    @Override
    public void keyReleased(KeyEvent e) {
      shiftOn = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      valveLocation.set(valveIndex.get(getText()), getLocation());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
      setLocation(this.getX() + e.getX() - this.getWidth() / 2, this.getY() + e.getY() - this.getHeight() / 2);
      repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

  }

  static class StatusIndicator extends JLabel {
    public StatusIndicator() {
      setSize(10, 10);
      setOpaque(true);
      setBackground(getColor1());
    }
  }

  static class MyLabel extends JLabel implements MouseMotionListener, MouseListener {
    public MyLabel(String text) {
      super.setText(text);
      addMouseListener(this);
      addMouseMotionListener(this);
    }

    public void mouseDragged(MouseEvent e) {
      setLocation(this.getX() + e.getX() - this.getWidth() / 2, this.getY() + e.getY() - this.getHeight() / 2);
      insideValveLabel(new Point(this.getX() + e.getX() - this.getWidth() / 2, this.getY() + e.getY() - this.getHeight() / 2));
      repaint();
    }

    void changeStat() {
      Color c = getBackground();
      int r=c.getRed(),g=c.getGreen(),b=c.getBlue();

      Color c1 = statusLabel[0].getBackground();
      int r1=c1.getRed(),g1=c1.getGreen(),b1=c1.getBlue();
      if (r==r1&&g==g1&&b==b1) {
        System.out.println(1);
        valveOpeningValue.set(valveIndex.get(getText()), 0);
        x.get(valveIndex.get(getText())).setBackground(this.getBackground());
        valveStat.set(valveIndex.get(getText()), "Off");
        Valve.setT( valveStat.get(valveIndex.get(focusedValve.getText())));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(focusedValve.getText())));
        j.setValue(0);
      }
      Color c2 = statusLabel[1].getBackground();
      int r2=c2.getRed(),g2=c2.getGreen(),b2=c2.getBlue();
      if (r==r2&&g==g2&&b==b2) {
        System.out.println(2);
        valveOpeningValue.set(valveIndex.get(getText()), 350);
        x.get(valveIndex.get(getText())).setBackground(this.getBackground());
        valveStat.set(valveIndex.get(getText()), "Slightly On");
        Valve.setT( valveStat.get(valveIndex.get(focusedValve.getText())));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(focusedValve.getText())));
        j.setValue(350);
      }
      Color c3 = statusLabel[2].getBackground();
      int r3=c3.getRed(),g3=c3.getGreen(),b3=c3.getBlue();
      if (r==r3&&g==g3&&b==b3) {
        System.out.println(3);
        valveOpeningValue.set(valveIndex.get(getText()), 650);
        x.get(valveIndex.get(getText())).setBackground(this.getBackground());
        valveStat.set(valveIndex.get(getText()), "Mostly On");
        Valve.setT( valveStat.get(valveIndex.get(focusedValve.getText())));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(focusedValve.getText())));
        j.setValue(650);
      }
      Color c4 = statusLabel[3].getBackground();
      int r4=c4.getRed(),g4=c4.getGreen(),b4=c4.getBlue();
      if (r==r4&&g==g4&&b==b4) {
        System.out.println(4);
        valveOpeningValue.set(valveIndex.get(getText()), 1000);
        x.get(valveIndex.get(getText())).setBackground(this.getBackground());
        valveStat.set(valveIndex.get(getText()), "All the way On");
        Valve.setT( valveStat.get(valveIndex.get(focusedValve.getText())));
        ValveManagement.status.setText("Status: " + valveStat.get(valveIndex.get(focusedValve.getText())));
        j.setValue(1000);
      }

    }

    public void mouseReleased(MouseEvent e) {
      if (isInsideValveLabel(new Point(this.getX() + e.getX() - this.getWidth() / 2, this.getY() + e.getY() - this.getHeight() / 2), focusedValve)) {
        setLocation(origPosStat.get(getText()));
        setDisableComponent(false);
        changeStat();
        focusedValve.setBorder(new LineBorder(Color.black, 1));
      } else {
        setLocation(origPosStat.get(getText()));
        focusedValve.setBorder(new LineBorder(Color.black, 1));
      }
      repaint();
    }

    void insideValveLabel(Point p) {
      if (isInsideValveLabel(p, focusedValve)) {
        focusedValve.setBorder(null);
      } else {
        focusedValve.setBorder(new LineBorder(Color.black, 1));
      }
    }


    boolean isInsideValveLabel(Point p, JLabel valve) {
      return (p.getX() + 50 >= valve.getX() && p.getX() + 50 <= valve.getX() + 50) && (p.getY() + 5 >= valve.getY() && p.getY() + 5 <= valve.getY() + 30);
    }

    public void mouseMoved(MouseEvent e) {
      //do nothing
    }

    public void mouseClicked(MouseEvent e) {
      //do nothing
    }

    public void mousePressed(MouseEvent e) {
      //do nothing
    }

    public void mouseEntered(MouseEvent e) {
      //do nothing
    }

    public void mouseExited(MouseEvent e) {
      //do nothing
    }
  }

  private static class RoundedBorder extends AbstractBorder {
    private final int radius;

    public RoundedBorder(int radius) {
      this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.setColor(Color.black);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
      g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
      return new Insets(10, 10, 10, 10);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
      insets.left = insets.right = insets.top = insets.bottom = 10;
      return insets;
    }
  }

  static class NumericSetUpStatus extends JSlider implements ChangeListener {
    public NumericSetUpStatus() {
      setMinimum(0);
      setMaximum(1000);
      setSize(160, 60);
      setPaintLabels(true);
      setMajorTickSpacing(250);
      setMinorTickSpacing(25);
      addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
      int value = getValue();
      if (value >= 0 && value <= 349) {
        Valve.status = "Off";
      } else if (value >= 350 && value <= 649) {
        Valve.status = "Slightly On";
      } else if (value >= 650 && value <= 999) {
        Valve.status = "Mostly On";
      } else if (value == 1000) {
        Valve.status = "All the way On";
      }
      jt.setText(String.valueOf(getValue()));
      preferredStatus.setText("Set status: " + Valve.status);
      repaint();
    }

    static class ManualInput extends JTextField implements KeyListener {
      public ManualInput() {
        setSize(40, 20);
        addKeyListener(this);
        setHorizontalAlignment(JTextField.CENTER);
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == '\b' && !getText().isEmpty()) {
          j.setValue(Integer.parseInt(getText()));
          return;
        }
        if (!(e.getKeyChar() == KeyEvent.VK_BACK_SPACE) && (!Character.isDigit(e.getKeyChar()) || Integer.parseInt(String.valueOf(getText()) + e.getKeyChar()) > 1000)) {
          e.consume();
        } else if (!getText().isEmpty()) {
          j.setValue(Integer.parseInt(getText() + e.getKeyChar()));
        }
        repaint();
      }

      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    }
  }

}