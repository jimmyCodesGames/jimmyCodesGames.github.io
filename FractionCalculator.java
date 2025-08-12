import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;

// Fraction calculator used to test student made Fraction class
public class FractionCalculator extends Frame implements ActionListener {

    private Fraction f1;
    private Fraction f2;
    private Fraction result;

    private Button f1NumBtn;
    private Button f1DenBtn;

    private Button f2NumBtn;
    private Button f2DenBtn;

    private Button addBtn = new Button("+");
    private Button subBtn = new Button("-");
    private Button mulBtn = new Button("×");
    private Button divBtn = new Button("÷");

    private Button zeroBtn = new Button("0");

    private Button clearBtn = new Button("Clear");
    private Button enterBtn = new Button("=");

    private Label resultLabel;
    
    private Label opsLabel = new Label("+", Label.CENTER);

    private Color numPadTextColor = Color.BLUE;
    private Color numPadBGColor = Color.LIGHT_GRAY;

    // Indicates which location is being modified/ highlighted.
    // 0 = NO HIGHLIGHTS, 1 = f1Num, 2 = f1Den, 3 = f2Num, 4 = f2Den 
    private int highlightedVal = 0; 

    // Sets default initial screen size
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    
    public FractionCalculator () {

        f1 = new Fraction();
        f2 = new Fraction();
        result = new Fraction();

        resultLabel = new Label("", Label.CENTER);
        
        f1NumBtn = new Button(""+f1.getNumerator());
        f1NumBtn.addActionListener(e -> selectItemToEdit(1));
        f1DenBtn = new Button(""+f1.getDenominator());
        f1DenBtn.addActionListener(e -> selectItemToEdit(2));

        f2NumBtn = new Button(""+f2.getNumerator());
        f2NumBtn.addActionListener(e -> selectItemToEdit(3));
        f2DenBtn = new Button(""+f2.getDenominator());
        f2DenBtn.addActionListener(e -> selectItemToEdit(4));

        addBtn.addActionListener(e -> setOps("+"));
        addBtn.setBackground(numPadBGColor);
        addBtn.setForeground(numPadTextColor);
        subBtn.addActionListener(e -> setOps("-"));
        subBtn.setBackground(numPadBGColor);
        subBtn.setForeground(numPadTextColor);
        mulBtn.addActionListener(e -> setOps("×"));
        mulBtn.setBackground(numPadBGColor);
        mulBtn.setForeground(numPadTextColor);
        divBtn.addActionListener(e -> setOps("÷"));
        divBtn.setBackground(numPadBGColor);
        divBtn.setForeground(numPadTextColor);

        clearBtn.setBackground(numPadBGColor);
        clearBtn.setForeground(numPadTextColor);
        enterBtn.setBackground(numPadBGColor);
        enterBtn.setForeground(numPadTextColor);

        // Allow blank buttons to clear highlighted buttons
        clearBtn.addActionListener(e -> clearAllValues());
        enterBtn.addActionListener(e -> calculateResult());

        zeroBtn.setBackground(numPadBGColor);
        zeroBtn.setForeground(numPadTextColor);

        // Calls actionPerformed() with button text as argument when button pressed 
        zeroBtn.addActionListener(this);

        setTitle("Fraction Calculator");
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());

        // Creates top panel to display user Fractions, Operator, and Result
        Panel topPanel = new Panel(new GridLayout(1, 5));
        topPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/4));

        Panel f1Panel = new Panel (new GridLayout(2,1));
        f1Panel.add(f1NumBtn);
        f1Panel.add(f1DenBtn);
        topPanel.add(f1Panel);

        Panel currentOpsPanel = new Panel (new GridLayout(1,1));
        currentOpsPanel.add(opsLabel);
        topPanel.add(currentOpsPanel);

        Panel f2Panel = new Panel (new GridLayout(2,1));
        f2Panel.add(f2NumBtn);
        f2Panel.add(f2DenBtn);
        topPanel.add(f2Panel);

        topPanel.add(new Label("=", Label.CENTER));

        topPanel.add(resultLabel);

        // Adds topPanel to screen
        add(topPanel, BorderLayout.NORTH);

        // Create numberPad panel
        Panel numPadPanel = new Panel(new GridLayout(4, 3));

        // Creates buttons 1-9
        for (int i = 1; i <= 9; i++) {
            Button b = new Button(""+i);
            b.setBackground(numPadBGColor);
            b.setForeground(numPadTextColor);
            
            // Calls actionPerformed() with button text as argument when button pressed 
            b.addActionListener(this);

            numPadPanel.add(b);
        }

        // Adds bottom numPad buttons: blank, zero, blank
        numPadPanel.add(clearBtn);
        numPadPanel.add(zeroBtn);
        numPadPanel.add(enterBtn);

        // Add numPadPanel to screen
        add(numPadPanel, BorderLayout.CENTER);

        // Create bottom panel to display operators
        Panel bottomPanel = new Panel(new GridLayout(2, 2));
        bottomPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/4));
        bottomPanel.add(addBtn);
        bottomPanel.add(subBtn);
        bottomPanel.add(mulBtn);
        bottomPanel.add(divBtn);

        // Add bottomPanel to screen
        add(bottomPanel, BorderLayout.SOUTH);

        // Impliments listener to end application when clicking x button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { System.exit(0); }
        });

        // displays calculator
        setVisible(true);
    }

    // Overrides abstract method from ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();

        // If numPad entry is made and highlighted section exists, update appropriate value with selected number entry
        if( Character.isDigit(cmd.charAt(0)) && highlightedVal != 0) {
            switch (highlightedVal) {
                case 1: 
            
                    f1NumBtn.setLabel("" +Integer.parseInt(cmd));
                    break;
                case 2: 
                    f1DenBtn.setLabel("" +Integer.parseInt(cmd));
                    break;
                case 3: 
                    f2NumBtn.setLabel("" +Integer.parseInt(cmd));
                    break;
                case 4: 
                    f2DenBtn.setLabel("" +Integer.parseInt(cmd));
                    break;
            }

            clearResult();
        }
    }

    // Updates current operator and clears any highlights
    private void setOps(String newOps) {
        clearResult();
        highlightedVal = 0;
        resetHighlights();
        opsLabel.setText(newOps);
    }

    // Update f1 and f2 to reflect screen
    private void updateFractions() {
        f1.updateDenominator(1);
        f2.updateDenominator(1);
        f1.updateNumerator(Integer.parseInt(f1NumBtn.getLabel()));
        f1.updateDenominator(Integer.parseInt(f1DenBtn.getLabel()));
        f2.updateNumerator(Integer.parseInt(f2NumBtn.getLabel()));
        f2.updateDenominator(Integer.parseInt(f2DenBtn.getLabel()));
    }

    // Updates display to reflect f1, f2, result, and current operator
    private void update() {
        f1NumBtn.setLabel("" + f1.getNumerator());
        f1DenBtn.setLabel("" + f1.getDenominator());
        f2NumBtn.setLabel("" + f2.getNumerator());
        f2DenBtn.setLabel("" + f2.getDenominator());
        resultLabel.setText(result.toString());
    }

    // Highlights selected region to be editted 
    private void selectItemToEdit(int newHighlightedVal) {

        resetHighlights();

        highlightedVal = newHighlightedVal;

        switch (highlightedVal) {
            case 1:
                f1NumBtn.setBackground(Color.YELLOW);
                break;
            case 2:
                f1DenBtn.setBackground(Color.YELLOW);
                break;
            case 3:
                f2NumBtn.setBackground(Color.YELLOW);
                break;
            case 4:
                f2DenBtn.setBackground(Color.YELLOW);
                break;
        }

    }

    // Display blank space as result
    private void clearResult() {
        resultLabel.setText("");
    }

    // Reset values to 1/1 and clear result
    private void clearAllValues() {
        f1.updateNumerator(1);
        f1.updateDenominator(1);
        f2.updateNumerator(1);
        f2.updateDenominator(1);

        update();
        resetHighlights();
        clearResult();
    }

    // Perform appropriate caluations based on current operator
    private void calculateResult() {

        resetHighlights();
        updateFractions();

        String cmd = opsLabel.getText();

        switch (cmd) {
            case "+":
                result = (f1.add(f2)).duplicate();
                break;
            case "-":
                result = (f1.subtract(f2)).duplicate();
                break;
            case "×":
                result = (f1.multiply(f2)).duplicate();
                break;
            case "÷":
                result = (f1.divide(f2)).duplicate();
                break;
        }

        // Update calculator
        update();

    }

    // Clear any selected region
    private void resetHighlights() {
        f1NumBtn.setBackground(UIManager.getColor("Button.background"));
        f1DenBtn.setBackground(UIManager.getColor("Button.background"));
        f2NumBtn.setBackground(UIManager.getColor("Button.background"));
        f2DenBtn.setBackground(UIManager.getColor("Button.background"));
    }
}
