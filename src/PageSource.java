import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PageSource extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageSource(MyEditorPane pane) {
		super("Page Source");
		setSize(800, 600);

		setLayout(new BorderLayout());
		setVisible(true);
		JTextArea tex = new JTextArea(pane.getText());
		JScrollPane sd = new JScrollPane(tex);
		add(sd);
	}
}
