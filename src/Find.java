import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
/**
 * 
 * @author mamali
 * search and highlight word
 *
 */
public class Find {
	public Find(MyEditorPane pane) {

		String word = JOptionPane.showInputDialog(null, "Enter Word To Find");
		Highlighter.HighlightPainter myhighlighter = new Painter(Color.cyan);

		Highlighter highlight = pane.getHighlighter();
		Highlighter.Highlight[] hilites = highlight.getHighlights();
		for (int i = 0; i < hilites.length; i++) {
			if (hilites[i].getPainter() instanceof Painter) {
				highlight.removeHighlight(hilites[i]);
			}
		}
		Document doc = pane.getDocument();
		String content = "";
		try {
			content = doc.getText(0, doc.getLength());
		} catch (BadLocationException e2) {
			e2.printStackTrace();
		}
		int index = 0;
		while ((index = content.indexOf(word, index)) >= 0) {
			try {
				highlight.addHighlight(index, index + word.length(),
						myhighlighter);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			index = index + word.length();
		}
	}

}
