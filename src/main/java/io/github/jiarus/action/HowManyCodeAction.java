package io.github.jiarus.action;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import io.github.jiarus.Version;
import io.github.jiarus.model.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiaru
 * @date: 2019-09-25
 * @Time: 14:10
 */
public class HowManyCodeAction extends AnAction {
    
    public static List<FileInfo> fileInfoArr = new ArrayList<>(24);
    
    @Override
    public void actionPerformed(AnActionEvent e) {
        
        fileInfoArr = new ArrayList<>(24);
        
        Project project = e.getData(PlatformDataKeys.PROJECT);
        
        VirtualFile projectFile = e.getData(PlatformDataKeys.PROJECT_FILE_DIRECTORY);
        VirtualFile[] files = projectFile.getChildren();
        for (int i = 0; i < files.length; i++) {
            VirtualFile file = files[i];
            recursionFileTree(file, e.getDataContext());
        }
        long totalLineCount = 0L;
        long totalSize = 0L;
        long fileCount = 0L;
        for (int i = 0; i < fileInfoArr.size(); i++) {
            FileInfo fileInfo = fileInfoArr.get(i);
            totalLineCount += fileInfo.getLineCount();
            totalSize += fileInfo.getSize();
            fileCount++;
        }
        
        String result = String.format("文件总数:%d,总文件大小:%d kb,总行数:%d", fileCount, totalSize / 1024, totalLineCount);
        NotificationGroup group = new NotificationGroup(Version.PLUGIN_NAME, NotificationDisplayType.STICKY_BALLOON, true);
        Notification notification = group.createNotification(
                "统计结果",
                result,
                NotificationType.INFORMATION,
                new NotificationListener.UrlOpeningListener(false)
        );
        
        Notifications.Bus.notify(notification, project);
    }
    
    /**
     * 递归读取文件
     *
     * @return
     */
    private void recursionFileTree(VirtualFile virtualFile, DataContext dataContext) {
        if (virtualFile.isDirectory()) {
            VirtualFile[] dicFiles = virtualFile.getChildren();
            for (int i = 0; i < dicFiles.length; i++) {
                VirtualFile fileTree = dicFiles[i];
                recursionFileTree(fileTree, dataContext);
            }
        } else {
            String fileName = virtualFile.getName();
            if (!(fileName.endsWith(".java") || fileName.endsWith(".yml") || fileName.endsWith(".xml"))) {
                return;
            }
            
            Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
            if (document == null) {
                System.out.println("file not exist:" + virtualFile.getPath());
                return;
            }
            long size = document.getTextLength();
            String path = virtualFile.getPath();
            long lineCount = document.getLineCount();
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(fileName);
            fileInfo.setSize(size);
            fileInfo.setLineCount(lineCount);
            fileInfo.setPath(path);
            fileInfoArr.add(fileInfo);
        }
    }
}
