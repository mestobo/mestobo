package net.mestobo;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import net.mestobo.form.Form;
import net.mestobo.form.TextFormField;

/** DicomGeneratorPage allows to generate fictitious DICOM studies. */
public class DicomGeneratorPage extends MenuPage {
	
	@Inject
	private BackgroundTaskExecutor backgroundTaskExecutor;
	
	public DicomGeneratorPage() {
		super(I18N.get("GenerateDicomStudy"));
	}

	@Override
	public String getMenuLabel() {
		return I18N.get("Dicom");
	}
	
	@Override
	public String getMenuCategory() {
		return "dicom";
	}

	@Override
	public String getMenuItemLabel() {
		return I18N.get("GenerateDicomStudy");
	}

	@Override
	protected Node createPresentation() {
		Form form = new Form();
		form.addField("destination_directory", new TextFormField(I18N.get("DestinationDirectory"))).required();
		form.addButton(I18N.get("Generate"), "generate", this::generateDicom).withIcon("dashicons-controls-play");
		
		return form;
	}
	
	private void generateDicom(ActionEvent e) {
		File templateFile = new File("/home/rli/test.ftl"); // todo :: get template from template library, etc.
		backgroundTaskExecutor.submitTask(new GenerateDicomTask(templateFile));
	}
	
	private class GenerateDicomTask extends Task<Void> {
		
		File templateFile;

		public GenerateDicomTask(File templateFile) {
			this.templateFile = templateFile;
			updateTitle(I18N.get("GenerateDicomStudy"));
		}

		@Override
		protected Void call() throws Exception {
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
			cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
			cfg.setLocalizedLookup(false);
			Template template = cfg.getTemplate(templateFile.getName());
			
			Map<String, Object> data = Map.of("foo", "fighter", "bar", "tender");
			try(Writer out = new StringWriter()) {
				template.process(data, out);
				System.out.println(out.toString());
			} catch (Exception e) { // ToDo this is duplicated from SendADTTask; How to DRY ?
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				updateMessage(sw.toString());
				throw e;
			}
			return null;
		}		
	}
	
	 /** https://stackoverflow.com/questions/1919189/how-can-i-introspect-a-freemarker-template-to-find-out-what-variables-it-uses
	 * Find all the variables used in the Freemarker Template
	 * @param template The template to get variables for
	 * @return The 
	 */
	public Set<String> getTemplateVariables(Template template) {
	    StringWriter stringWriter = new StringWriter();
	    Map<String, Object> dataModel = new HashMap<>();
	    boolean exceptionCaught;

	    do {
	        exceptionCaught = false;
	        try {
	            template.process(dataModel, stringWriter);
	        } catch (InvalidReferenceException e) {
	            exceptionCaught = true;
	            dataModel.put(e.getBlamedExpressionString(), "");
	        } catch (IOException | TemplateException e) {
	            throw new IllegalStateException("Failed to load template", e);
	        }
	    } while (exceptionCaught);

	    return dataModel.keySet();
	}
	
//	public void someTest() throws IOException {
//		Attributes ds = new Attributes();
//		DicomOutputStream os = null;
//		os.writeFileMetaInformation(ds);
//		os.writeDataset(ds, ds);
//		ds.setString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 100");
////		(0008,0008) CS [ORIGINAL\PRIMARY\OTHER\R\IR]            #  28, 5 ImageType
//		ds.setString(Tag.ImageType, VR.CS, "ORIGINAL\\PRIMARY\\OTHER\\R\\IR");
//		ds.setDate(Tag.InstanceCreationDate, new Date());
////		(0008,0013) TM [093801]                                 #   6, 1 InstanceCreationTime
//	}
}
