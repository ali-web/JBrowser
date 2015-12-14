import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JEditorPane;
import javax.swing.text.Highlighter;

public class MyEditorPane extends JEditorPane  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String address;

	public MyEditorPane(String address) {
		this.address = address;
		final MyEditorPane p=this;
	
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Highlighter highlight = p.getHighlighter();

				Highlighter.Highlight[] hilites = highlight.getHighlights();

				for (int i = 0; i < hilites.length; i++) {
					if (hilites[i].getPainter() instanceof Painter) {
						highlight.removeHighlight(hilites[i]);
					}
				}
			
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}