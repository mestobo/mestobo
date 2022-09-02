package net.mestobo;

import java.time.LocalDate;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

public class SendADTModule extends WorkbenchModule {
	
	private VBox outer;
	private Form form;
	private FormRenderer formRenderer;

	protected SendADTModule() {
		super(I18N.get("SendADT"), MaterialDesignIcon.SEND);
	}
	
	@Override
	public void init(Workbench workbench) {
		outer = new VBox();
		form = Form.of(
			Group.of(
				Field.ofStringType("").label("FirstName").required("ValueRequired")
				, Field.ofStringType("").label("LastName")
				, Field.ofDate((LocalDate) null).label("BirthDate")
			), Section.of(
				Field.ofStringType("").label("VisitNumber")	
			).title("Visit")
		).i18n(I18N.getTranslationService());
		formRenderer = new FormRenderer(form);
		Button sendButton = new Button(I18N.get("Send"));
		sendButton.disableProperty().bind(form.validProperty().not()); // TODO :: button can not show tooltip as to why it is disabled ...		
		outer.getChildren().addAll(formRenderer, sendButton);
		outer.setAlignment(Pos.TOP_RIGHT);
		super.init(workbench);
	}

	@Override
	public Node activate() {
		return outer;
	}
	
	// TODO :: remove me, not needed
	@Override
	public boolean destroy() {
	  // Perform an asynchronous task (in our case showing a dialog)
	  getWorkbench().showDialog(WorkbenchDialog.builder(
	      "Save before closing?",
	      "Do you want to save your progress? Otherwise it will be lost.",
	      ButtonType.YES, ButtonType.NO, ButtonType.CANCEL)
	      .blocking(true)
	      .onResult(buttonType -> {
	        // If CANCEL was not pressed
	        if (!ButtonType.CANCEL.equals(buttonType)) {
	          if (ButtonType.YES.equals(buttonType)) {
	            // YES was pressed -> Proceed with saving
	          }
	          close(); // Close the module since CANCEL was not pressed 
	        }
	      })
	      .build());
	  
	  return false; // return false, because we're closing manually
	}	
	
}
