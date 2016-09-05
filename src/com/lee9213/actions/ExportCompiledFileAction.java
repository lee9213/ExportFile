package com.lee9213.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.CompilerProjectExtension;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.lee9213.util.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * 导出编译后的文件action
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/21 17:45
 */
public class ExportCompiledFileAction extends AnAction {

    private static String compilerPath = "";//编译路径
    private static String moduleName = "";//选择moduleName
    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        DataContext dataContext = event.getDataContext();
        //获取选中的文件
        VirtualFile[] files = DataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        if(files == null || files.length == 0){
            Messages.showErrorDialog("未选中文件","error");
            return;
        }
        //获取当前module
        Module module = StringUtil.getModule(event.getProject(),dataContext,files[0].getPath());
        //获取module编译路径
        VirtualFile virtualFile = CompilerModuleExtension.getInstance(module).getCompilerOutputPath();
        if(virtualFile == null) {
            //获取project编译路径
            virtualFile = CompilerProjectExtension.getInstance(module.getProject()).getCompilerOutput();
        }
        compilerPath = virtualFile.getPath();
        moduleName = module.getName();
        try {
            //获取系统桌面路径
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            String outputpath = desktopDir.getAbsolutePath();
            for (int i = 0; i < files.length; i++) {
                VirtualFile file = files[i];
                exportVirtualFile(file, virtualFile.getChildren(), outputpath);
            }
            Messages.showInfoMessage("导出成功", "success");
        }catch (Exception ex){
            ex.printStackTrace();
            Messages.showErrorDialog("异常错误"+ex.getMessage(), "error");
        }
    }

    private void exportVirtualFile(VirtualFile file, VirtualFile[] virtualFile, String outputpath)
            throws Exception {
        if (!ArrayUtils.isEmpty(virtualFile)) {
            for (VirtualFile vf : virtualFile) {
                String filenpath = vf.toString();
                String srcName,dstName;
                if ("java".equalsIgnoreCase(file.getExtension())) {
                    if (StringUtils.indexOf(filenpath, "$") != -1) {
                        srcName = StringUtils.substring(filenpath, StringUtils.lastIndexOf(filenpath, "/") + 1, StringUtils.indexOf(filenpath, "$"));
                    } else {
                        srcName = StringUtils.substring(filenpath, StringUtils.lastIndexOf(filenpath, "/") + 1, StringUtils.length(filenpath) - 6);
                    }
                    dstName = StringUtils.substring(file.getName(), 0, StringUtils.length(file.getName()) - 5);
                }else{
                    srcName = StringUtils.substring(filenpath, StringUtils.lastIndexOf(filenpath, "/") + 1, StringUtils.length(filenpath));
                    dstName = file.getName();
                }
                if (StringUtils.equals(srcName, dstName)) {
                    String packagePath = StringUtils.substring(vf.getPath(), StringUtils.length(compilerPath), StringUtils.length(vf.getPath()));
                    File s = new File(vf.getPath());
                    File t = new File(outputpath +"/"+moduleName+"/WEB-INF/classes/"+ packagePath);
                    FileUtil.copy(s, t);
                    break;
                }
                if (!ArrayUtils.isEmpty(virtualFile)) {
                    exportVirtualFile(file, vf.getChildren(), outputpath);
                }
            }
        }
    }
}
