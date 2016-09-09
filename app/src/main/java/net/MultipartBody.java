package net;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by Magina on 16/9/8.
 */
public class MultipartBody {

    //上传文件时候请求头中的Content-Type的类型
    private final String TYPE_MULTIPART = "multipart/form-data";

    //
    private final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    //
    private final String NEW_LINE_STR = "\r\n";
    //
    private final String CONTENT_TYPE = "Content-Type: ";
    //
    private final String CONTENT_DISPOSITION = "Content-Disposition: ";
    //文本参数和字符集
    private final String TYPE_TEXT_CHARSET = "text/plain; charset=UTF-8";
    //字节流参数
    private final String TYPE_OCTET_STREAM = "application/octet-stream";
    //字节数组参数
    private final byte[] BINARY_ENCODING = "Content-Transfer-Encoding: binary\r\n\r\n".getBytes();
    //文本参数
    private final byte[] BIT_ENCODING = "Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes();

    //以上 文本参数对应的Encoding为8bit，字节流参数对应的Encoding为binary；

    //参数分隔符
    private String mBoundary = null;
    //输出流，用于缓存参数数据
    ByteArrayOutputStream mOutputStream = new ByteArrayOutputStream();

    public MultipartBody() {
        this.mBoundary = generateBoundary();
    }

    //生成并返回参数的boundary分隔符
    private final String generateBoundary() {
        final StringBuffer buf = new StringBuffer();
        final Random random = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        return buf.toString();
    }

    //参数开头的分隔符
    private void writeFirstBoundary() throws IOException {
        mOutputStream.write(("--" + mBoundary + NEW_LINE_STR).getBytes());
    }

    //
    public void addStringPart(String paramName, String value) {
        writeToOutputStream(paramName, value.getBytes(), TYPE_TEXT_CHARSET, BIT_ENCODING, "");
    }

    //
    public void addByteArrayPart(String paramName, byte[] rawData) {
        writeToOutputStream(paramName, rawData, TYPE_OCTET_STREAM, BINARY_ENCODING, "no-file");
    }

    //
    public void addFilePart(String key, File file) {
        InputStream is = null;
        try {
            writeFirstBoundary();
            mOutputStream.write(getContentDispositionBytes(key, file.getName()));
            final String type = CONTENT_TYPE + TYPE_OCTET_STREAM + NEW_LINE_STR;
            mOutputStream.write(type.getBytes());
            mOutputStream.write(BINARY_ENCODING);

            is = new FileInputStream(file);
            final byte[] tmp = new byte[4096];
            int len = 0;
            while ((len = is.read(tmp)) != -1) {
                mOutputStream.write(tmp, 0, len);
            }
            mOutputStream.flush();
            //这里是不是应该有个换行呢？？？
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(is);
        }
    }

    private void writeToOutputStream(String paramName, byte[] rawData, String type, byte[] encoding, String fileName) {
        try {
            writeFirstBoundary();
            mOutputStream.write(getContentDispositionBytes(paramName, fileName));
            mOutputStream.write((CONTENT_TYPE + type + NEW_LINE_STR).getBytes());
            mOutputStream.write(encoding);
            mOutputStream.write(rawData);
            mOutputStream.write(NEW_LINE_STR.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //
    private byte[] getContentDispositionBytes(String paramName, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(CONTENT_DISPOSITION).append("form-data; name=\"").append(paramName).append("\"");
        //文本参数没有fileName参数，设置为空即可
        if (!TextUtils.isEmpty(fileName)) {
            sb.append("; filename=\"").append(fileName).append("\"");
        }
        sb.append(NEW_LINE_STR);
        return sb.toString().getBytes();
    }


    public long getContentLength() {
        return mOutputStream.toByteArray().length;
    }

    public String getContentEncoding() {
        return null;
    }

    //这里指的是头文件
    public String getContentType() {
        return "Content-Type: multipart/form-data; boundary=" + mBoundary;
    }

    public void writeTo(OutputStream outStream) throws IOException {
        //参数最末尾的结束符
        final String endString = "--" + mBoundary + "--\r\n";
        //写入结束符
        mOutputStream.write(endString.getBytes());
        //将缓存在mOutputStream中的数据全部写入到outStream中
        outStream.write(mOutputStream.toByteArray());
    }

    public InputStream getContent() {
        return new ByteArrayInputStream(mOutputStream.toByteArray());
    }

}
