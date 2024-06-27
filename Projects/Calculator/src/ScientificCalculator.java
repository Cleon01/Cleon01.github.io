import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

class ScientificCalculator extends JFrame implements ActionListener {
    private static JFrame frame;
    private static JTextField textField;
    private String operand1, operand2, operator;

    public ScientificCalculator() {
        operand1 = operand2 = operator = "";
    }

    public static void main(String[] args) {
        frame = new JFrame("Scientific Calculator");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        ScientificCalculator calculator = new ScientificCalculator();
        textField = new JTextField(20);
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(new Font("Arial", Font.BOLD, 20));
        textField.setBackground(Color.WHITE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 6;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(textField, gbc);

        // "Annualized Return" button
        JButton annualizedReturnButton = new JButton("Annualized Return");
        annualizedReturnButton.setFont(new Font("Arial", Font.PLAIN, 16));
        annualizedReturnButton.setBackground(new Color(220, 220, 220));
        annualizedReturnButton.setOpaque(true);
        annualizedReturnButton.setBorderPainted(false);
        annualizedReturnButton.addActionListener(calculator);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(annualizedReturnButton, gbc);

        // "Monthly Payment" button
        JButton monthlyPaymentButton = new JButton("Monthly Payment");
        monthlyPaymentButton.setFont(new Font("Arial", Font.PLAIN, 16));
        monthlyPaymentButton.setBackground(new Color(220, 220, 220));
        monthlyPaymentButton.setOpaque(true);
        monthlyPaymentButton.setBorderPainted(false);
        monthlyPaymentButton.addActionListener(calculator);
        gbc.gridwidth = 3;
        gbc.gridx = 3;
        gbc.gridy = 1;
        panel.add(monthlyPaymentButton, gbc);

        String[] buttonLabels = {
            "7", "8", "9", "/", "sin", "asin",
            "4", "5", "6", "*", "cos", "acos",
            "1", "2", "3", "-", "tan", "atan",
            "0", ".", "=", "+", "log", "exp",
            "C", "√", "^", "ln", "!", "π"
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.setBackground(new Color(220, 220, 220));
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.addActionListener(calculator);
            gbc.gridwidth = 1;
            gbc.gridx = i % 6;
            gbc.gridy = (i / 6) + 2;
            panel.add(button, gbc);
        }

        frame.add(panel);
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ((command.charAt(0) >= '0' && command.charAt(0) <= '9') || command.equals(".")) {
            if (!operator.equals("")) {
                operand2 += command;
            } else {
                operand1 += command;
            }
            textField.setText(operand1 + operator + operand2);
        } else if (command.equals("C")) {
            operand1 = operand2 = operator = "";
            textField.setText("");
        } else if (command.equals("=")) {
            double result = performOperation();
            textField.setText(operand1 + operator + operand2 + "=" + result);
            operand1 = String.valueOf(result);
            operator = operand2 = "";
        } else if (command.equals("sin") || command.equals("cos") || command.equals("tan") ||
                   command.equals("log") || command.equals("ln") || command.equals("√") || command.equals("asin") ||
                   command.equals("acos") || command.equals("atan") || command.equals("exp") || command.equals("!")) {
            operator = command;
            double result = performScientificOperation(command, operand1);
            textField.setText(operator + "(" + operand1 + ")=" + result);
            operand1 = String.valueOf(result);
            operator = operand2 = "";
        } else if (command.equals("π")) {
            if (operator.equals("")) {
                operand1 = String.valueOf(Math.PI);
            } else {
                operand2 = String.valueOf(Math.PI);
            }
            textField.setText(operand1 + operator + operand2);
        } else if (command.equals("Annualized Return")) {
            performAnnualizedReturnCalculation();
        } else if (command.equals("Monthly Payment")) {
            performMonthlyPaymentCalculation();
        } else {
            if (operator.equals("") || operand2.equals("")) {
                operator = command;
            } else {
                double result = performOperation();
                operand1 = String.valueOf(result);
                operator = command;
                operand2 = "";
            }
            textField.setText(operand1 + operator + operand2);
        }
    }

    private double performOperation() {
        double num1 = Double.parseDouble(operand1);
        double num2 = Double.parseDouble(operand2);
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "/":
                return num1 / num2;
            case "*":
                return num1 * num2;
            case "^":
                return Math.pow(num1, num2);
            default:
                return 0;
        }
    }

    private double performScientificOperation(String operation, String operand) {
        double num = Double.parseDouble(operand);
        switch (operation) {
            case "sin":
                return Math.sin(Math.toRadians(num));
            case "cos":
                return Math.cos(Math.toRadians(num));
            case "tan":
                return Math.tan(Math.toRadians(num));
            case "log":
                return Math.log10(num);
            case "ln":
                return Math.log(num);
            case "√":
                return Math.sqrt(num);
            case "asin":
                return Math.toDegrees(Math.asin(num));
            case "acos":
                return Math.toDegrees(Math.acos(num));
            case "atan":
                return Math.toDegrees(Math.atan(num));
            case "exp":
                return Math.exp(num);
            case "!":
                return factorial(num);
            default:
                return 0;
        }
    }

    private double factorial(double num) {
        if (num == 0) return 1;
        double result = 1;
        for (int i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    private void performAnnualizedReturnCalculation() {
        try {
            double originalAmount = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Original Amount:"));
            double finalAmount = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Final Amount:"));
            double numYears = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Number of Years:"));
            double annualizedReturn = Math.pow((finalAmount / originalAmount), (1 / numYears)) - 1;
            textField.setText("Annualized Return: " + formatNumber(annualizedReturn));
        } catch (Exception e) {
            textField.setText("Error in input");
        }
    }

    private void performMonthlyPaymentCalculation() {
        try {
            double loanAmount = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Loan Amount:"));
            double annualRate = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Annual Interest Rate (%):"));
            double numYears = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Number of Years:"));
            double monthlyRate = annualRate / 12 / 100;
            double numMonths = numYears * 12;
            double monthlyPayment = (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -numMonths));
            textField.setText("Monthly Payment: " + formatNumber(monthlyPayment));
        } catch (Exception e) {
            textField.setText("Error in input");
        }
    }

    private String formatNumber(double number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.round(new MathContext(10, RoundingMode.HALF_UP));
        return bd.toPlainString();
    }
}
