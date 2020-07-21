import javax.swing.*;
import java.awt.*;

class WordLabel extends JLabel {
  private final Color _color;

  WordLabel(String word, Color color) {
    super(String.format(" %s ", word));
    _color = color;

    setOpaque(true);
    setBackground(_color);
    setForeground(_color);
  }

  void reset() {
      setForeground(_color);
  }
}
