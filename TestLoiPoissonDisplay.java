import javax.swing.JFrame;

public class TestLoiPoissonDisplay {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Test loi de poisson");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        TestLoiPoisson testldp = new TestLoiPoisson();
        frame.add(testldp);
        testldp.test();
        testldp.repaint();
        frame.revalidate();
        frame.repaint();
    }
}
