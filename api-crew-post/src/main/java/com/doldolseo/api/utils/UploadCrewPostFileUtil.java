package com.doldolseo.api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadCrewPostFileUtil extends UploadFileUtil {
    private final String ROOT_PATH;
    private final String CREW_POST_IMG_PATH;

    public UploadCrewPostFileUtil(String uploadPath) {
        super(uploadPath);
        this.ROOT_PATH = uploadPath;
        this.CREW_POST_IMG_PATH = ROOT_PATH + "/crew_post_image/";
    }

    public String saveCrewPostImg(String uuid, MultipartFile imgFile) {
        if (imgFile != null) {
            String originalImgFileName = imgFile.getOriginalFilename();
            makeDirIfNoExist(CREW_POST_IMG_PATH + uuid);
            Path savePath = Paths.get(CREW_POST_IMG_PATH + uuid + "/" + originalImgFileName);
            trasferFile(imgFile, savePath);

            return originalImgFileName;
        }
        return null;
    }

}
