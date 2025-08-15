import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class Calendar extends Frame {

    private MyDate date;

    private Button nextDayBtn = new Button("Next Day");
    private Button nextMonthBtn = new Button("Next Month");
    private Button nextYearBtn = new Button("Next Year");

    private Button prevDayBtn = new Button("Previous Day");
    private Button prevMonthBtn = new Button("Previous Month");
    private Button prevYearBtn = new Button("Previous Year");

    private Label monthLabel;
    private Label dayLabel;
    private Label yearLabel;

    private Label monthTextLabel = new Label("Month", Label.CENTER);
    private Label dayTextLabel = new Label("Day", Label.CENTER);
    private Label yearTextLabel = new Label("Year", Label.CENTER);

    private Label leapyearTextLabel = new Label("Is this a leap year?   ", Label.RIGHT);
    private Label isLeapyearLabel;

    private Label daysSinceStartTextLabel = new Label("Days since the start:   ", Label.RIGHT);
    private Label daysSinceStartLabel;

    private static final String[] monthNames = {
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December"
    };

    // Sets default initial screen size
    private final int WIDTH = 300;
    private final int HEIGHT = 250;

    public Calendar () {

        // Parse local date from user's PC to set MyDate: date
        String[] localDate = (LocalDate.now()).toString().split("-");
        date = new MyDate(Integer.parseInt(localDate[1]), Integer.parseInt(localDate[2]), Integer.parseInt(localDate[0]));

        // Add functionality to Buttons
        nextMonthBtn.addActionListener(e -> addMonth());
        nextDayBtn.addActionListener(e -> addDay());
        nextYearBtn.addActionListener(e -> addYear());

        prevMonthBtn.addActionListener(e -> prevMonth());
        prevDayBtn.addActionListener(e -> prevDay());
        prevYearBtn.addActionListener(e -> prevYear());

        // Assign text and style for Labels
        monthLabel = new Label(date.getMonthName(), Label.CENTER);
        dayLabel = new Label(getDayStr(), Label.CENTER);
        yearLabel = new Label(getYearStr(), Label.CENTER);

        monthTextLabel.setBackground(Color.LIGHT_GRAY);
        dayTextLabel.setBackground(Color.LIGHT_GRAY);
        yearTextLabel.setBackground(Color.LIGHT_GRAY);

        isLeapyearLabel = new Label ((MyDate.isLeapYear((getYearInt())) ? "Yes" : "No"), Label.LEFT);
        daysSinceStartLabel = new Label (String.format("%,d", MyDate.getDaysSinceStart(date)) , Label.LEFT);

        // Set default window
        setTitle("Calendar");
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());

        // Organize window
        Panel topPanel = new Panel(new GridLayout(3,3));

        Panel monthPanel = new Panel(new GridLayout(2, 1));
        monthPanel.add(monthTextLabel); 
        monthPanel.add(monthLabel);

        Panel dayPanel = new Panel(new GridLayout(2, 3));
        dayPanel.add(dayTextLabel);
        dayPanel.add(dayLabel);
        

        Panel yearPanel = new Panel(new GridLayout(2, 3));
        yearPanel.add(yearTextLabel);
        yearPanel.add(yearLabel);

        topPanel.add(monthPanel);
        topPanel.add(dayPanel);
        topPanel.add(yearPanel);
        topPanel.add(nextMonthBtn);
        topPanel.add(nextDayBtn);
        topPanel.add(nextYearBtn);
        topPanel.add(prevMonthBtn);
        topPanel.add(prevDayBtn);
        topPanel.add(prevYearBtn);

        add(topPanel, BorderLayout.NORTH);

        Panel bottomPanel = new Panel(new GridLayout(2, 2));
        bottomPanel.add(leapyearTextLabel);
        bottomPanel.add(isLeapyearLabel);
        bottomPanel.add(daysSinceStartTextLabel);
        bottomPanel.add(daysSinceStartLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Impliments listener to end application when clicking x button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });

        // displays Calendar
        setVisible(true);
        
    }
 
    // Increases date.month by one, adjusts day and year if necessary, and updates display
    private void addMonth() {

        date.setMonth(getMonthInt()+1);

        date.setDay(getDayInt());

        if (getMonthInt() == 12) {
            addYear();
        }
 
        updateDisplay();
    }

    // Increases date.date by 1, changes to next month if necessary, and updates display
    private void addDay() {
        date.goToNextDay();
        updateDisplay();
    }

    // Increases date.year by 1 and adjusts day if necessary given new year and update display
    private void addYear() {
        date.setYear(getYearInt() + 1);
        date.setDay(getDayInt());  
        updateDisplay();
    }

    // Decreases date.month by one, adjusts day and year if necessary, and updates display
    private void prevMonth() {

        if (getMonthInt() == 1) {
            date.setMonth(12);
            prevYear();
        }
        else {
            date.setMonth(getMonthInt()-1);
        }

        date.setDay(getDayInt());  

        updateDisplay();
    }

    // Decreases date.date by 1, changes to previous month if necessary, and updates display
    private void prevDay() {
        if (getDayInt() == 1) {
            prevMonth();
            setMaxDay();
        } 
        else {
             date.setDay(getDayInt()-1);
        }
        updateDisplay();
    }

    // Decreases date.year by 1 and adjusts day if necessary given new year and update display
    private void prevYear() {
        date.setYear(getYearInt() - 1);
        date.setDay(getDayInt());  
        updateDisplay();
    }

    // Sets date.day to maximum # of days in current month given current year
    private void setMaxDay() {
        date.setDay(MyDate.getMaxDays(getYearInt(), getMonthInt()));
    }

    // Returns date.month as int
    private int getMonthInt() {
        int monthNum = 1;
        
        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equals(monthLabel.getText()))
                monthNum = i+1;
        }

        return monthNum;
    }

    // Returns date.year as int
    private int getYearInt() {
        return Integer.parseInt(date.toString().split(" ")[2]);
    }

    // Returns date.day as int
    private int getDayInt() {
        return Integer.parseInt(date.toString().split(" ")[1].replace(",", ""));
    }

    // Returns date.year as String
    private String getYearStr() {
        return date.toString().split(" ")[2];
    }

    // Returns date.day as String
    private String getDayStr() {
        return date.toString().split(" ")[1];
    }

    // Update all text based on date
    private void updateDisplay() {
        monthLabel.setText(date.getMonthName());
        dayLabel.setText(getDayStr());
        yearLabel.setText(getYearStr());
        isLeapyearLabel.setText((MyDate.isLeapYear((getYearInt())) ? "Yes" : "No"));
        daysSinceStartLabel.setText(String.format("%,d", MyDate.getDaysSinceStart(date)));
    }

}
