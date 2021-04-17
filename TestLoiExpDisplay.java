import javax.swing.JFrame;

public class TestLoiExpDisplay {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Test loi exponentielle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        TestLoiExp testloiexp = new TestLoiExp();
        frame.add(testloiexp);
        testloiexp.test();
        testloiexp.repaint();
        frame.revalidate();
        frame.repaint();
    }
}
