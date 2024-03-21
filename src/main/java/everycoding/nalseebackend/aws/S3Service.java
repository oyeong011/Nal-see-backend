package everycoding.nalseebackend.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import everycoding.nalseebackend.api.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    private final String bucket = "nalsee-post-photos";

    public List<String> uploadS3(List<MultipartFile> files) throws IOException {
        List<String> photos = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            File file = convertMultipartFileToFile(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> file convert fail"));

            String key = "post-photos/" + UUID.randomUUID();

            try {
                amazonS3.putObject(new PutObjectRequest(bucket, key, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (Exception e) {
                throw new BaseException("사진 업로드 실패");
            } finally {
                file.delete();
            }

            photos.add(getS3(key));
        }
        return photos;
    }

    private String getS3(String key) {
        return amazonS3.getUrl(bucket, key).toString();
    }

    public void deleteS3(String url) {
        String key = "post-photos/"+url.substring(url.lastIndexOf('/')+1);
        System.out.println(key);
        amazonS3.deleteObject(bucket, key);
    }

    private Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)){
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }
}
