package hu.tilos.radio.backend.ttt;


import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TTTService {

    @Value("${couchdb.url}")
    private String couchDBurl;

    private Gson gson = new Gson();

    public TTTService() {
    }

    public byte[] getImage(String id) {
        try {
            if (!id.matches("\\w+")) {
                throw new IllegalArgumentException("Invalid id");
            }
            URL url = new URL(couchDBurl + "/ttt-business/" + id + "/photo.jpg");
            InputStream inputStream = url.openStream();
            byte[] result = IOUtils.toByteArray(inputStream);
            inputStream.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] getAllBusiness() {
        URL oracle = null;
        try {
            oracle = getUrl();
            try (BufferedReader in = createBufferedReader(oracle)) {
                Class<ArrayList> arrayListClass = ArrayList.class;

                Map<String, Object> result = (Map<String, Object>) gson.fromJson(in, Object.class);
                List<Map> rows = (List) result.get("rows");
                return rows.stream().map(o -> ((Map) o).get("doc")).map(o -> {
                    Map<String, Object> element = (Map<String, Object>) o;
                    element.put("id", element.get("_id"));
                    element.remove("_id");
                    element.remove("_rev");
                    Object attachments = element.get("_attachments");
                    if (attachments != null) {
                        System.out.println(attachments);
                        element.remove("_attachments");
                        element.put("photo", "/api/v1/ttt/business/" + element.get("id") + "/photo.jpg");
                    }
                    return element;
                }).toArray();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }


    }

    protected URL getUrl() throws MalformedURLException {
        return new URL(couchDBurl + "/ttt-business/_all_docs?include_docs=true");
    }

    protected BufferedReader createBufferedReader(URL oracle) throws IOException {
        return new BufferedReader(new InputStreamReader(oracle.openStream()));
    }

    public void setCouchDBurl(String couchDBurl) {
        this.couchDBurl = couchDBurl;
    }
}
