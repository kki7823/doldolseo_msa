package com.doldolseo.api.util;

import com.doldolseo.common.UploadFileUtil;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadReviewFileUtil extends UploadFileUtil {
    private final String ROOT_PATH;
    private final String REVIEW_IMG_PATH;

    public UploadReviewFileUtil(String uploadPath) {
        super(uploadPath);
        this.ROOT_PATH = uploadPath;
        this.REVIEW_IMG_PATH = ROOT_PATH + "/review_image/";
    }

    public String saveReviewImg(String imageUUID, MultipartFile reviewImgFile) {
        if (reviewImgFile != null) {
            String originalImgFileName = reviewImgFile.getOriginalFilename();
            makeDirIfNoExist(REVIEW_IMG_PATH + imageUUID);
            Path savePath = Paths.get(REVIEW_IMG_PATH + imageUUID + "/" + originalImgFileName);
            trasferFile(reviewImgFile, savePath);

            return originalImgFileName;
        }
        return null;
    }

    public String saveCourseImg(String imageUUID, MultipartFile courseImgFile) {
        if (courseImgFile != null) {
            String coureseImgFileName = courseImgFile.getOriginalFilename();
            makeDirIfNoExist(REVIEW_IMG_PATH + imageUUID);
            Path savePath = Paths.get(REVIEW_IMG_PATH + imageUUID + "/" + coureseImgFileName);
            trasferFile(courseImgFile, savePath);
            return coureseImgFileName;
        }
        return "savefail";
    }

    public void deleteReviewImgs(String imageUUID) {
        Path path = Paths.get(REVIEW_IMG_PATH+imageUUID);
        File imageDir = new File(path.toString());
        deleteFilesInDir(imageDir);

        boolean isDeleted = imageDir.delete();
        logWhenDeleteFile(isDeleted, path.toString());
    }
}
