package Prototype2;

import javax.swing.*;
import java.awt.*;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class calendar extends JPanel {
    private JLabel monthLabel;
    private JPanel daysPanel;
    static Calendar calendar;

    public static String getMonth() {
        return month;
    }

    public static String getYear() {
        return year;
    }

    static String month= "";
    static String year="";
    public calendar(int width, int height) {
        calendar = Calendar.getInstance();
        setSize(width, height);
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.white);
        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        monthLabel.setOpaque(true);
        monthLabel.setBackground(Color.white);
        add(monthLabel, BorderLayout.NORTH);

        daysPanel = new JPanel();
        daysPanel.setLayout(new GridLayout(0, 7)); // 7 columns for days of the week
        daysPanel.setOpaque(true);
        daysPanel.setBackground(Color.white);
        add(daysPanel, BorderLayout.CENTER);

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        updateCalendar();
    }

    private void updateCalendar() {
        daysPanel.removeAll(); // Clear previous days
        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, getLocale()) + " " + calendar.get(Calendar.YEAR));
        year= String.valueOf(calendar.get(Calendar.YEAR));
        month =calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, getLocale());
        setMonth(month,year);
        // Get first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty labels for days before the first day of the month
        for (int i = 1; i < firstDayOfWeek; i++) {
            daysPanel.add(new CustomLabel("")); // Empty label
        }

        // Add labels for each day in the month
        for (int day = 1; day <= daysInMonth; day++) {
            daysPanel.add(new CustomLabel(String.valueOf(day)/*, JLabel.CENTER*/));
        }

        daysPanel.revalidate(); // Refresh the panel
        daysPanel.repaint(); // Repaint the panel
    }
    static void setMonth(String mon,String yr){
        month = mon;
        year= yr;
    }
}
