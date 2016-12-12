import java.util.ArrayList;

import javax.swing.JTextField;

public class Tab {
	private ArrayList<MyEditorPane> panes = new ArrayList<MyEditorPane>();
	private int index;
	private int size;
	private JTextField address;
	MyEditorPane mainPane;

	public Tab(MyEditorPane pane, JTextField addressBar) {
		panes.add(pane);
		index = 0;
		mainPane = pane;
		size = 1;
		address = addressBar;
	}

	public MyEditorPane getPane() {
		return panes.get(index);
	}

	@Override
	public String toString() {
		return address.getText();
	}

	public String getAdress() {
		return address.getText();
	}

	public void setAddress(String addres) {
		address.setText(addres);
	}

	public void addPane(MyEditorPane pane) {
		panes.add(pane);
		index++;
		size++;
	}

	public int getIndex() {
		return index;
	}

	public int size() {
		return size;
	}

	public MyEditorPane getBeforPane() {
		index--;
		return panes.get(index);
	}

	public MyEditorPane getAfterPane() {
		index++;
		return panes.get(index);
	}
}
