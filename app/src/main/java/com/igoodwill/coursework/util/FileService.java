package com.igoodwill.coursework.util;

import com.igoodwill.coursework.elastic.model.HasFile;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.apache.commons.codec.CharEncoding.UTF_8;

@Service
public class FileService {

    public String toBase64(MultipartFile file) throws IOException {
        return Base64.encodeBase64String(file.getBytes());
    }

    public ResponseEntity<Resource> downloadFile(HasFile entity) throws UnsupportedEncodingException {
        return downloadFile(entity.getFile(), entity.getFilename(), entity.getAttachment().getContentType());
    }

    public ResponseEntity<Resource> downloadFile(String file, String filename, String contentType)
            throws UnsupportedEncodingException {
        String encodedFilename = URLEncoder
                .encode(filename, UTF_8)
                .replaceAll("\\+", " ");

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename*=" + UTF_8 + "''" + encodedFilename
                )
                .header(HttpHeaders.CONTENT_TYPE, contentType + ";charset=" + UTF_8)
                .body(new ByteArrayResource(Base64.decodeBase64(file)));
    }
}
