import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MYJButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int index;

	public MYJButton(ImageIcon closeXIcon, int index) {
		super(closeXIcon);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
