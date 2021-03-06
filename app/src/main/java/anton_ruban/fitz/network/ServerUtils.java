package anton_ruban.fitz.network;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
@author antonruban on 25.04.2018.
 */

public class ServerUtils {

    private static final String SERVER_URL = "http://antonruban-001-site1.btempurl.com/";

    private static Retrofit retrofit = null;
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static Retrofit getClient(String baseUrl) {

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient.Builder httpClient = new okhttp3.OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static ServerApi serverApi(){
        return ServerUtils.getClient(SERVER_URL).create(ServerApi.class);
    }
}
