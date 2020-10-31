package com.cavin.culture.util;

import org.apache.http.entity.ContentType;
import org.neo4j.driver.v1.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

public class FlieToMultipartFile {

    static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678!a"));

    public MultipartFile getMultipartFile(File file){
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(),file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(),fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }
    /**
     * 执行cypher的通用方法
     */
    public StatementResult excuteCypherSql(String cypherSql) {
        StatementResult result = null;
        try (Session session = driver.session()) {
            System.out.println(cypherSql);
            result = session.run(cypherSql);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

}
