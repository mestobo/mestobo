package net.mestobo.form;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import net.mestobo.GUIFactory;
import net.mestobo.I18N;
import net.synedra.validatorfx.Check;

/** Base class for form fields.
 * @param <T> The input type (string, date, number, etc.) of the form field.
 * @param <SELF> The concrete type of a form field, required for fluent API
 */
public abstract class FormField<T, SELF extends FormField<T, SELF>> {
	private String label;
	private VBox presentation;
	protected Form form;
	
	FormField(String label) {
		this.label = label;
		createPresentation();
	}
	
	/** Factory method that must create the visual representation of the input part of this field.
	 * @param parent The parent region the input control will live in
	 * @return The visual representation of the input part of this field.
	 */
	abstract protected Node createInputControl(Region parent);
	
	/** Property that represents the current value of this field. */
	abstract protected Property<T> valueProperty();
	
	/** Property that is true, if this field is empty. */
	abstract protected ObservableBooleanValue emptyProperty();
	
	/** Return the FormField as instance of its own subclass (required for fluent API). */
	abstract protected SELF self();
	
	public void setForm(Form form) {
		this.form = form;
	}
	
	/** Set this field as required (i.e. field must not be empty). */
	public SELF required() {
		form.getValidator().createCheck()
			.dependsOn("isEmpty", emptyProperty())
			.withMethod(this::mustNotBeEmpty)
			.decorates(getPresentation())
			.immediate();
		return self();
	}
	
	private void mustNotBeEmpty(Check.Context context) {
		if (emptyProperty().get()) {
// TODO :: error or warning?			
			context.error(I18N.get("FieldMustNotBeEmpty", getLabel()));
		}
	}	
	
	public String getLabel() {
		return label;
	}
	
	/** Get presentation of whole field (label plus input control). */
	public Region getPresentation() {
		return presentation;
	}
	
	protected void createPresentation() {
		presentation = GUIFactory.create(VBox.class, this, "formfield-box");
		presentation.getChildren().add(new Label(getLabel()));
		
		Node control = createInputControl(presentation);
		presentation.getChildren().add(control);
	}

}
