package doldolseo_msa_crew_board.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class UploadFileUtil {
    public UploadFileUtil(String uploadPath) {
    }

    public void trasferFile(MultipartFile multipartFile, Path savepath) {
        try {
            multipartFile.transferTo(new File(savepath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeDirIfNoExist(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void moveFile(Path src, Path dst) {
        try {
            Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logWhenDeleteFile(boolean isDeleted, String filePath) {
        if (isDeleted) {
            System.out.printf("[%s] %s 삭제 완료", LocalDateTime.now(), filePath);
        } else {
            System.out.printf("[%s] %s 삭제 실패", LocalDateTime.now(), filePath);
        }
    }
}
