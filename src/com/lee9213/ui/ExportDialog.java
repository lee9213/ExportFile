package com.lee9213.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckBoxList;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.containers.HashMap;
import com.lee9213.bean.DirectoryBean;
import com.lee9213.bean.FileBean;
import com.lee9213.listener.CheckBoxListSelectListener;
import com.lee9213.listener.CheckedTreeNodeListener;
import com.lee9213.listener.SelectFileListener;
import com.lee9213.listener.TreeSelectedListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * 导出文件弹出框
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/25 16:50
 */
public class ExportDialog extends DialogWrapper {

    private Project project;
    private VirtualFile virtualFile;
    private Module module;

    //提示信息
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;

    //文件夹树菜单容器
    private JBScrollPane treePanel;
    //文件列表容器
    private JBScrollPane filePanel;
    //文件夹
    private CheckBoxList checkBoxList;
    //全选按钮
    private JButton btnSelectAll;
    //全不选按钮
    private JButton btnDeselectAll;
    //目录
    private TextFieldWithBrowseButton textFieldButton;
    //创建目录和文件
    private JBRadioButton radioButton1;
    //只创建文件
    private JBRadioButton radioButton2;

    //所有目录结构的文件列表
    private Map<CheckedTreeNode,DirectoryBean> fileMap = new HashMap<>();

    public ExportDialog(@NotNull Project project,@NotNull Module module,@Nullable VirtualFile virtualFile) {
        super(project);
        this.project = project;
        this.virtualFile = virtualFile;
        this.module = module;

        this.label1 = new JLabel("  File system");
        Font font = new Font("",1,12);
        this.label1.setFont(font);
        this.label2 = new JLabel("    Please enter a destination directory.");


        //右边文件列表
        this.checkBoxList = new CheckBoxList();

        //左边目录结构数据
        CheckBoxTreeData treeData = new CheckBoxTreeData(project,fileMap);
        CheckBoxTree checkBoxTree = new CheckBoxTree(treeData.treeNode(),virtualFile,fileMap);
        checkBoxTree.init(project.getName(),module.getModuleFilePath(),checkBoxList);

        //单个文件选择事件
        this.checkBoxList.setCheckBoxListListener(new CheckBoxListSelectListener(checkBoxList,fileMap,checkBoxTree));

        this.treePanel = new JBScrollPane(checkBoxTree);

        checkBoxTree.addCheckboxTreeListener(new CheckedTreeNodeListener(checkBoxList,fileMap,checkBoxTree));
        checkBoxTree.addTreeSelectionListener(new TreeSelectedListener(checkBoxList,fileMap));

        //左边滚动条
        this.treePanel.setPreferredSize(new Dimension(350,300));
        this.treePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.treePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.treePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //右边滚动条
        this.filePanel = new JBScrollPane(checkBoxList);
        this.filePanel.setPreferredSize(new Dimension(350,300));
        this.filePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.filePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.filePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //全选事件
        this.btnSelectAll = new JButton("Select All");
        this.btnSelectAll.addActionListener(new SelectFileListener(checkBoxTree,fileMap));
        //全不选事件
        this.btnDeselectAll = new JButton("Deselect All");
        this.btnDeselectAll.addActionListener(new SelectFileListener(checkBoxTree,fileMap));

        this.label3 = new JLabel("To directory: ");
        //目录选择按钮
        this.textFieldButton = new TextFieldWithBrowseButton();
        //获取系统桌面路径
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        String outputpath = desktopDir.getAbsolutePath();
        //设置初始值为桌面路径
        this.textFieldButton.setText(outputpath);
        this.textFieldButton.setPreferredSize(new Dimension(600,25));
        this.textFieldButton.addBrowseFolderListener("Select directory","",null, FileChooserDescriptorFactory.createSingleFolderDescriptor());

        this.radioButton1 = new JBRadioButton("Create directory structure for files");
        this.radioButton1.setSelected(true);
        this.radioButton2 = new JBRadioButton("Create only selected directories");

        this.setResizable(false);
        init();
    }


    @Override
    protected JComponent createCenterPanel() {
        JPanel main = new JPanel(new VerticalLayout(1));

        main.add(label1);
        main.add(label2);

        //数据展示
        JPanel column2 = new JPanel(new HorizontalLayout(5));
        column2.add(treePanel);
        column2.add(filePanel);
        main.add(column2);

        JPanel column3 = new JPanel(new HorizontalLayout(1));
        column3.add(btnSelectAll);
        column3.add(btnDeselectAll);
        main.add(column3);

        JPanel column4 = new JPanel(new HorizontalLayout(1));

        column4.add(label3);
        column4.add(textFieldButton);
        main.add(column4);

        JPanel column5 = new JPanel(new VerticalLayout(1));
        column5.add(radioButton1);
        column5.add(radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);
        main.add(column5);

        return main;
    }

    @Override
    protected void doOKAction() {
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module[]  modules = moduleManager.getModules();
        String path = textFieldButton.getText();
        try {
            for (Map.Entry entry : fileMap.entrySet()) {
                DirectoryBean directoryBean = (DirectoryBean) entry.getValue();
                java.util.List<FileBean> fileBeanList = directoryBean.getFiles();
                if (fileBeanList == null) continue;
                for (FileBean fileBean : fileBeanList) {
                    if (fileBean.isChecked()) {
                        String filepath = fileBean.getFile().getAbsolutePath();
                        for (Module module : modules) {
                            String modulePath = module.getModuleFilePath();
                            modulePath = modulePath.substring(0, modulePath.lastIndexOf("/")).replace("/", "\\");
                            if (filepath.startsWith(modulePath)) {
                                File file;
                                if(radioButton1.isSelected()) {
                                    filepath = filepath.substring(filepath.indexOf(modulePath) + modulePath.length());
                                    file = new File(path + "\\" + module.getName() + filepath);
                                }else{
                                    filepath = filepath.substring(filepath.lastIndexOf("\\"));
                                    file = new File(path + "\\" + module.getName() + filepath);
                                }
                                FileUtil.copy(fileBean.getFile(), file);
                            }
                        }
                    }
                }
            }
            this.close(-1);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
