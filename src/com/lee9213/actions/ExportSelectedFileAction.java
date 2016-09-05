package com.lee9213.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.lee9213.ui.ExportDialog;
import com.lee9213.util.StringUtil;

/**
 * 导出选择的文件action
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/19 12:50
 */
public class ExportSelectedFileAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here

        Project project = event.getData(PlatformDataKeys.PROJECT);

        DataContext dataContext = event.getDataContext();
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        if(file != null){
            //获取选中的文件
            file = DataKeys.VIRTUAL_FILE.getData(dataContext);
            if(file == null){
                Messages.showErrorDialog("未选中文件","error");
                return;
            }
        }
        Module module = StringUtil.getModule(event.getProject(),dataContext,file.getPath());
        ExportDialog exportDialog = new ExportDialog(project,module,file);
        exportDialog.setTitle("Export Files");
        exportDialog.show();
    }
}
