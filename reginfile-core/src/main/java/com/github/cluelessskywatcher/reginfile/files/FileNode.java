package com.github.cluelessskywatcher.reginfile.files;

import java.io.File;

import lombok.Getter;

public class FileNode {
    private @Getter File file;

    public FileNode(File file) {
        this.file = file;
    }

    public String toString() {
        return file.getName();
    }
}
