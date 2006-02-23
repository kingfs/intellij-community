package com.intellij.codeInspection.ex;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.InspectionsBundle;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

public class DisableInspectionToolAction implements IntentionAction {
  private final String myToolId;

  public DisableInspectionToolAction(LocalInspectionTool tool) {
    myToolId = tool.getID();
  }

  public DisableInspectionToolAction(final HighlightDisplayKey key) {
    myToolId = key.getID();
  }

  public String getText() {
    return InspectionsBundle.message("disable.inspection.action.name");
  }

  public String getFamilyName() {
    return getText();
  }

  public boolean isAvailable(Project project, Editor editor, PsiFile file) {
    return true;
  }

  public void invoke(Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    final InspectionProjectProfileManager profileManager = InspectionProjectProfileManager.getInstance(file.getProject());
    InspectionProfile inspectionProfile = profileManager.getInspectionProfile(file);
    ModifiableModel model = inspectionProfile.getModifiableModel();
    model.disableTool(myToolId);
    model.commit(profileManager);    
    DaemonCodeAnalyzer.getInstance(project).restart();
  }

  public boolean startInWriteAction() {
    return false;
  }
}
