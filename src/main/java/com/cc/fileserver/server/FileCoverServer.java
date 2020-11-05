package com.cc.fileserver.server;

import com.aspose.words.UnsupportedFileFormatException;
import com.cc.fileserver.bean.entity.FileIndex;

/**
 * @InterfaceName FileCoverServer
 * @Description TODO
 * @Date 2020/8/6 19:00
 * @Created by Administrator
 */
public interface FileCoverServer {

    FileIndex word2pdf(String remoteFileUrl) throws UnsupportedFileFormatException;
}
