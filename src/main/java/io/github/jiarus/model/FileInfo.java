package io.github.jiarus.model;

/**
 * @author zhangjiaru
 * @date: 2019-10-08
 * @Time: 15:34
 */
public class FileInfo {
    
    private String name;
    
    private String path;
    
    private Long size;
    
    private Long lineCount;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public Long getLineCount() {
        return lineCount;
    }
    
    public void setLineCount(Long lineCount) {
        this.lineCount = lineCount;
    }
}
