package com.hxw.wanandroid.base.cookies;

import com.hxw.core.utils.HexUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;
import timber.log.Timber;

/**
 * @author hxw
 * @date 2019/2/12
 */
public class SerializableOkHttpCookies implements Serializable {
    private transient final Cookie cookies;
    private transient Cookie clientCookies;
    SerializableOkHttpCookies(Cookie cookies) {
        this.cookies = cookies;
    }

    Cookie getCookies() {
        Cookie bestCookies = cookies;
        if (clientCookies != null) {
            bestCookies = clientCookies;
        }
        return bestCookies;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookies.name());
        out.writeObject(cookies.value());
        out.writeLong(cookies.expiresAt());
        out.writeObject(cookies.domain());
        out.writeObject(cookies.path());
        out.writeBoolean(cookies.secure());
        out.writeBoolean(cookies.httpOnly());
        out.writeBoolean(cookies.hostOnly());
        out.writeBoolean(cookies.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder()
                .name(name)
                .value(value)
                .expiresAt(expiresAt)
                .path(path);
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        if (secure) {
            builder.secure();
        }
        if (httpOnly) {
            builder.httpOnly();
        }
        clientCookies = builder.build();
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    public static String encodeCookie(SerializableOkHttpCookies cookie) {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            Timber.e(e, "IOException in encodeCookie");
            return null;
        }
        return HexUtils.bytes2HexStr2(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    public static Cookie decodeCookie(String cookieString) {
        byte[] bytes = HexUtils.hexStr2Bytes(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableOkHttpCookies) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
            Timber.e(e, "IOException in decodeCookie");
        } catch (ClassNotFoundException e) {
            Timber.e(e, "ClassNotFoundException in decodeCookie");
        }
        return cookie;
    }
}
