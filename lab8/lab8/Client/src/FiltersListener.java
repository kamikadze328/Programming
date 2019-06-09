import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
interface FiltersListener extends DocumentListener {

    default void insertUpdate(DocumentEvent e) {
        doSomething();
    }

    default void removeUpdate(DocumentEvent e) {
        doSomething();
    }

    default void changedUpdate(DocumentEvent e) {
        doSomething();
    }

    void doSomething();
}